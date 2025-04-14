package com.example.vango_mobile;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Home extends AppCompatActivity {

    private RecyclerView recyclerTrips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE dd/MM", new Locale("pt", "BR"));

        Calendar calendar = Calendar.getInstance();
        String hoje = dateFormat.format(calendar.getTime());

        calendar.add(Calendar.DAY_OF_MONTH, 1);
        String amanha = dateFormat.format(calendar.getTime());

        calendar.add(Calendar.DAY_OF_MONTH, 1);
        String depois = dateFormat.format(calendar.getTime());

        recyclerTrips = findViewById(R.id.recyclerTrips);
        recyclerTrips.setLayoutManager(new LinearLayoutManager(this));

        List<Trip> fakeTrips = Arrays.asList(
                new Trip("Facens", "Home", "22:50"),
                new Trip("Facens", "Home", "23:10"),
                new Trip("Home", "Facens", "07:30")
        );

        TripAdapter adapter = new TripAdapter(fakeTrips);
        recyclerTrips.setAdapter(adapter);
    }
}