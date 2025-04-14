package com.example.vango_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class InitialPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_initial_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button button1 = findViewById(R.id.btnEntrar);
        button1.setOnClickListener(view -> {
            Intent intent = new Intent(InitialPage.this, Login.class);
            startActivity(intent);
        });

        Button button2 = findViewById(R.id.btnCriarConta);
        button2.setOnClickListener(view -> {
            Intent intent = new Intent(InitialPage.this, Cadastro.class);
            startActivity(intent);
        });
    }
}