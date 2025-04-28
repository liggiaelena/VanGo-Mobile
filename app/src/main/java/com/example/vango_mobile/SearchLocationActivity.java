package com.example.vango_mobile;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class SearchLocationActivity extends AppCompatActivity {
    EditText editSearch;
    RecyclerView recyclerSuggestions;
    LocationAdapter adapter;
    List<LocationSuggestion> suggestions = new ArrayList<>();
    boolean isFromField = true; // saber se é origem ou destino

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search_location);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        editSearch = findViewById(R.id.editSearch);
        recyclerSuggestions = findViewById(R.id.recyclerSuggestions);

        isFromField = getIntent().getBooleanExtra("isFromField", true);

        adapter = new LocationAdapter(suggestions, selected -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("id", selected.id);
            resultIntent.putExtra("label", selected.label);
            resultIntent.putExtra("isFromField", isFromField);
            setResult(RESULT_OK, resultIntent);
            finish();
        });

        recyclerSuggestions.setLayoutManager(new LinearLayoutManager(this));
        recyclerSuggestions.setAdapter(adapter);

        editSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 2) {
                    fetchSuggestions(s.toString());
                }
            }
        });
    }

    private void fetchSuggestions(String query) {
        String url = "http://10.0.2.2:3000/destination?q=" + query;

        HttpRequestHelper.makeRequest(url, "GET", null, new HttpRequestHelper.ResponseCallback() {
            @Override
            public void onSuccess(String response) {
                runOnUiThread(() -> {
                    try {
                        JSONArray array = new JSONArray(response);
                        suggestions.clear();

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);
                            String label = obj.getString("label");
                            String id = obj.has("id") ? "f-" + obj.getString("id") : "f-null";
                            suggestions.add(new LocationSuggestion(id, label));
                        }

                        adapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        Log.e("SEARCH_LOC", "Erro no parsing JSON", e);
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                Log.e("SEARCH_LOC", "Erro na busca de sugestões", e);
            }
        });
    }
}