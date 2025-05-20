package com.example.vango_mobile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.content.Intent;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

public class Login extends AppCompatActivity {
    EditText editTextEmail, editTextPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ImageView button1 = findViewById(R.id.btnVoltar);
        button1.setOnClickListener(view -> {
            Intent intent = new Intent(Login.this, InitialPage.class);
            startActivity(intent);
        });

        editTextEmail = findViewById(R.id.edtEmail);
        editTextPassword = findViewById(R.id.edtSenha);
        btnLogin = findViewById(R.id.btnEntrarLogin);

        btnLogin.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            String json = "{\"email\":\"" + email + "\",\"senha\":\"" + password + "\"}";

            HttpRequestHelper.makeRequest("http://10.0.2.2:3008/login", "POST", json, new HttpRequestHelper.ResponseCallback() {
                @Override
                public void onSuccess(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        int userId = jsonObject.getInt("id");
                        String typeEntity = jsonObject.getString("typeEntity");

                        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
                        prefs.edit()
                                .putInt("userId", userId)
                                .putString("userType", typeEntity)
                                .apply();

                        runOnUiThread(() -> {
                            Toast.makeText(Login.this, "Login successful!", Toast.LENGTH_SHORT).show();
                            Intent intent;
                            if (typeEntity.equals("D")) {
                                intent = new Intent(Login.this, MainMotoristActivity.class);
                            } else {
                                intent = new Intent(Login.this, Search.class);
                            }
                            startActivity(intent);
                            finish();
                        });

                    } catch (Exception e) {
                        Log.e("LOGIN_PARSE", "Error parsing login response", e);
                        runOnUiThread(() ->
                                Toast.makeText(Login.this, "Invalid response from server", Toast.LENGTH_SHORT).show()
                        );
                    }
                }

                @Override
                public void onError(Exception e) {
                    Log.e("LOGIN_ERROR", "Login request failed", e);
                    runOnUiThread(() ->
                            Toast.makeText(Login.this, "Login failed", Toast.LENGTH_SHORT).show()
                    );
                }
            });
        });
    }
}

