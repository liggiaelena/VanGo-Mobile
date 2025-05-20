package com.example.vango_mobile;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainMotoristActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DriverTripAdapter tripAdapter;
    private List<DriverTrip> tripList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_motorist);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.recyclerDriverTrips);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        tripAdapter = new DriverTripAdapter(this, tripList);
        recyclerView.setAdapter(tripAdapter);

        setupBottomNavigation();
        fetchDriverTrips();
    }
    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setSelectedItemId(R.id.nav_driver_trips);

        bottomNavigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_driver_history) {
                startActivity(new Intent(this, HistoryMotorist.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (id == R.id.nav_driver_profile) {
                startActivity(new Intent(this, ProfileMotoristActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }
            return true; // já está na tela de viagens
        });
    }
    private void fetchDriverTrips() {
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        int driverId = prefs.getInt("userId", -1);
        if (driverId == -1) {
            Toast.makeText(this, "ID do motorista não encontrado", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = "http://10.0.2.2:8080/v1/driver/trips/" + driverId;

        HttpRequestHelper.makeRequest(url, "GET", null, new HttpRequestHelper.ResponseCallback() {
            @Override
            public void onSuccess(String response) {
                runOnUiThread(() -> {
                    try {
                        JSONArray array = new JSONArray(response);
                        tripList.clear();

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);

                            String tripId = obj.getString("tripId");
                            String departureLabel = obj.getString("departureLabel");
                            String arrivalLabel = obj.getString("arrivalLabel");
                            String departureTime = obj.getString("departureTime");
                            String pngRoute = obj.getString("pngRoute");

                            tripList.add(new DriverTrip(tripId, departureLabel, arrivalLabel, departureTime, pngRoute));
                        }

                        tripAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        Log.e("DRIVER_TRIP_PARSE", "Erro ao processar JSON", e);
                        Toast.makeText(MainMotoristActivity.this, "Erro ao carregar viagens", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                Log.e("DRIVER_TRIP_API", "Erro na requisição", e);
                runOnUiThread(() -> Toast.makeText(MainMotoristActivity.this, "Falha ao buscar viagens", Toast.LENGTH_SHORT).show());
            }
        });
    }
}