package com.example.vango_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class TripConfirmationActivity extends AppCompatActivity {

    TextView txtResumo;
    Button btnConfirmar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_trip_confirmation);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        txtResumo = findViewById(R.id.txtResumo);
        btnConfirmar = findViewById(R.id.btnConfirmar);

        Intent intent = getIntent();
        String rota = intent.getStringExtra("route");
        String horario = intent.getStringExtra("arrivalTime");
        int vagas = intent.getIntExtra("spots", 0);
        double preco = intent.getDoubleExtra("price", 0.0);
        String tipo = intent.getStringExtra("tripType");


        txtResumo.setText(
                "Você está prestes a contratar:\n\n" +
                        "Rota: " + rota + "\n" +
                        "Chegada: " + horario + "\n" +
                        "Vagas: " + vagas + "\n" +
                        "Preço: R$ " + String.format("%.2f", preco) + "\n" +
                        "Tipo: " + (tipo.equals("one-way") ? "Só ida" : "Ida e volta") + "\n"
        );

        btnConfirmar.setOnClickListener(v -> {
            Toast.makeText(this, "Viagem contratada com sucesso!", Toast.LENGTH_LONG).show();
            finish(); // ou navegar para uma tela de sucesso
        });
    }
}