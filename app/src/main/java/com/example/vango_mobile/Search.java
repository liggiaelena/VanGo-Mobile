package com.example.vango_mobile;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.*;

import com.google.android.material.internal.TextWatcherAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

public class Search extends AppCompatActivity {

    TextView tvDestination , tvLocation, tvDate ;
    RadioGroup frequencyGroup;
    RadioButton rbSingle, rbRecurring;
    Button btnSearch;
    RecyclerView recyclerTrips;

    String selectedFromId = "";
    String selectedToId = "";
    boolean isOneWay = true;
    String selectedDate = "";

    private static final int REQUEST_CODE_LOCATION = 1001;
    private static final int REQUEST_CODE_DESTINATION = 1002;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // No onCreate
        initViews();
        setupListeners();
        recyclerTrips.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initViews() {
        tvLocation  = findViewById(R.id.tvLocation);
        tvDestination  = findViewById(R.id.tvDestiny);
        tvDate = findViewById(R.id.tvDate);
        frequencyGroup = findViewById(R.id.frequencyGroup);
        rbSingle = findViewById(R.id.rbSingle);
        rbRecurring = findViewById(R.id.rbRecurring);
        btnSearch = findViewById(R.id.btnSearch);
        recyclerTrips = findViewById(R.id.recyclerTrips);
    }

    private void setupListeners() {

        tvLocation.setOnClickListener(v -> {
            Intent intent = new Intent(Search.this, SearchLocationActivity.class);
            intent.putExtra("isFromField", true);
            startActivityForResult(intent, REQUEST_CODE_LOCATION);
        });

        tvDestination.setOnClickListener(v -> {
            Intent intent = new Intent(Search.this, SearchLocationActivity.class);
            intent.putExtra("isFromField", false);
            startActivityForResult(intent, REQUEST_CODE_DESTINATION);
        });

        tvDate.setOnClickListener(v -> openDatePicker());

        frequencyGroup.setOnCheckedChangeListener((group, checkedId) -> {
            isOneWay = (checkedId == R.id.rbSingle);
        });

        btnSearch.setOnClickListener(v -> {
            if (selectedFromId.isEmpty() || selectedToId.isEmpty() || selectedDate.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!isOneWay) showModalBeforeRecurringSearch();
            fetchTripsFromAPI();
        });
    }

    private void openDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    Calendar selected = Calendar.getInstance();
                    selected.set(year, month, dayOfMonth);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    selectedDate = sdf.format(selected.getTime());
                    tvDate.setText(selectedDate);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void showModalBeforeRecurringSearch() {
        new AlertDialog.Builder(this)
                .setTitle("Viagem Recorrente")
                .setMessage("As viagens recorrentes são agrupadas em ida e volta semanalmente.")
                .setCancelable(false)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void fetchTripsFromAPI() {
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        int userId = prefs.getInt("userId", -1);
        if (userId == -1) return;

        String url = String.format(
                "http://10.0.2.2:3008/v1/trips/passenger?id=%d&date=%s&destination_to=%s&destination_from=%s&one-way-trip=%b",
                userId, selectedDate, selectedToId, selectedFromId, isOneWay
        );

        HttpRequestHelper.makeRequest(url, "GET", null, new HttpRequestHelper.ResponseCallback() {
            @Override
            public void onSuccess(String response) {
                runOnUiThread(() -> {
                    try {
                        JSONArray tripsArray = new JSONArray(response);
                        List<Trip> trips = new ArrayList<>();

                        for (int i = 0; i < tripsArray.length(); i++) {
                            JSONObject tripJson = tripsArray.getJSONObject(i);

                            String origin = tripJson.getJSONObject("destiny_from").getString("label");
                            String destination = tripJson.getJSONObject("destiny_to").getString("label");
                            String arrivalTime = tripJson.getString("horario_de_chegada");
                            int spotsAvailable = tripJson.getInt("spots_available");
                            double price = tripJson.getDouble("price");
                            String tripType = tripJson.getString("trip_type");

                            trips.add(new Trip(origin, destination, arrivalTime, spotsAvailable, price, tripType));
                        }

                        TripAdapter tripAdapter = new TripAdapter(trips);
                        recyclerTrips.setAdapter(tripAdapter);

                    } catch (Exception e) {
                        Toast.makeText(Search.this, "Erro ao processar viagens", Toast.LENGTH_SHORT).show();
                        Log.e("TRIPS_PARSE", "Erro no JSON", e);
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(() -> Toast.makeText(Search.this, "Erro ao buscar viagens", Toast.LENGTH_SHORT).show());
                Log.e("TRIPS_API", "Erro na requisição", e);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            String id = data.getStringExtra("id");
            String label = data.getStringExtra("label");
            boolean isFromField = data.getBooleanExtra("isFromField", true);

            if (isFromField) {
                selectedFromId = id;
                tvLocation.setText(label);
            } else {
                selectedToId = id;
                tvDestination.setText(label);
            }
        }
    }
}