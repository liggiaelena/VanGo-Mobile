package com.example.vango_mobile;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MyTrips extends AppCompatActivity {
    private RecyclerView recyclerTrips;
    private TripAdapter tripAdapter;
    private List<Trip> tripList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_trips);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        recyclerTrips = findViewById(R.id.recyclerMyTrips);
        recyclerTrips.setLayoutManager(new LinearLayoutManager(this));
        tripAdapter = new TripAdapter(tripList, TripDetailsPassagerActivity.class);
        recyclerTrips.setAdapter(tripAdapter);

        setupBottomNavigation();
        fetchTripsFromAPI();
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_trips);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_search) {
                startActivity(new Intent(this, Search.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }
            return true;
        });
    }

    private void fetchTripsFromAPI() {
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        int userId = prefs.getInt("userId", -1);
        if (userId == -1) {
            Toast.makeText(this, "ID do passageiro não encontrado", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = "http://10.0.2.2:3008/v1/passenger/" + userId + "/trips";

        HttpRequestHelper.makeRequest(url, "GET", null, new HttpRequestHelper.ResponseCallback() {
            @Override
            public void onSuccess(String response) {
                runOnUiThread(() -> {
                    try {
                        JSONArray array = new JSONArray(response);
                        tripList.clear();

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);

                            String origin = obj.getString("departureLabel");
                            String destination = obj.getString("arrivalLabel");
                            String arrivalTime = obj.getString("arrivalTime");
                            int spots = obj.getInt("spotsAvailable");
                            double price = obj.getDouble("price");
                            String tripType = obj.getString("tripType");

                            tripList.add(new Trip(origin, destination, arrivalTime, spots, price, tripType));
                        }

                        tripAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        Log.e("TRIPS_PARSE", "Erro ao processar JSON", e);
                        Toast.makeText(MyTrips.this, "Erro ao carregar viagens", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                Log.e("TRIPS_API", "Erro na requisição", e);
                runOnUiThread(() -> Toast.makeText(MyTrips.this, "Falha ao buscar viagens", Toast.LENGTH_SHORT).show());
            }
        });
    }
}