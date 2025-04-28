package com.example.vango_mobile;

import android.os.AsyncTask;
import android.util.Log;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
public class HttpRequestHelper {

    public interface ResponseCallback {
        void onSuccess(String response);
        void onError(Exception e);
    }

    public static void makeRequest(String urlString, String method, String jsonBody, ResponseCallback callback) {
        new AsyncTask<Void, Void, String>() {
            Exception exception = null;

            @Override
            protected String doInBackground(Void... voids) {
                StringBuilder response = new StringBuilder();
                try {
                    URL url = new URL(urlString);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod(method);
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(5000);

                    if (method.equals("POST") && jsonBody != null) {
                        connection.setDoOutput(true);
                        OutputStream os = connection.getOutputStream();
                        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                        writer.write(jsonBody);
                        writer.flush();
                        writer.close();
                        os.close();
                    }

                    int responseCode = connection.getResponseCode();
                    InputStream inputStream = responseCode >= 400 ? connection.getErrorStream() : connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    reader.close();
                    connection.disconnect();
                } catch (Exception e) {
                    exception = e;
                }

                return response.toString();
            }

            @Override
            protected void onPostExecute(String result) {
                if (exception != null) {
                    callback.onError(exception);
                } else {
                    callback.onSuccess(result);
                }
            }
        }.execute();
    }
}
