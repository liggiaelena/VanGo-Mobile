package com.example.vango_mobile;

import android.os.Bundle;

import android.content.Intent;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {
    private EditText editUser, editPassword;
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

        editUser = findViewById(R.id.edtEmail);
        editPassword = findViewById(R.id.edtSenha);
        btnLogin = findViewById(R.id.btnEntrarLogin);

        btnLogin.setOnClickListener(v -> {
            String user = editUser.getText().toString();
            String pass = editPassword.getText().toString();

            if ("admin".equals(user) && "123".equals(pass)) {
                Intent intent = new Intent(Login.this, Home.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Usuário ou senha inválidos", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

