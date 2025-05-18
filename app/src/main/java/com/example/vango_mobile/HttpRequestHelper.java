package com.example.vango_mobile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpRequestHelper {

    public interface ResponseCallback {
        void onSuccess(String response);
        void onError(Exception e);
    }

    public static void makeRequest(String urlString, String method, String jsonBody, ResponseCallback callback) {
        new Thread(() -> {
            HttpURLConnection connection = null;
            try {
                URL url = new URL(urlString);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod(method);
                connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                connection.setRequestProperty("Accept", "application/json");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);
                connection.setDoInput(true);

                if ("POST".equalsIgnoreCase(method) && jsonBody != null) {
                    connection.setDoOutput(true);
                    try (OutputStream os = connection.getOutputStream()) {
                        byte[] input = jsonBody.getBytes("utf-8");
                        os.write(input, 0, input.length);
                    }
                }

                int responseCode = connection.getResponseCode();
                InputStream is = (responseCode >= 200 && responseCode < 300)
                        ? connection.getInputStream()
                        : connection.getErrorStream();

                StringBuilder response = new StringBuilder();
                try (BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        response.append(line.trim());
                    }
                }

                callback.onSuccess(response.toString());

            } catch (Exception e) {
                callback.onError(e);
            } finally {
                if (connection != null) connection.disconnect();
            }
        }).start();
    }
}
