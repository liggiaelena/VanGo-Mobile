package com.example.vango_mobile;

public class LocationSuggestion {
    public String id;       // ex: f-1 ou p-3
    public String label;    // ex: "Facens"

    public LocationSuggestion(String id, String label) {
        this.id = id;
        this.label = label;
    }

    @Override
    public String toString() {
        return label; // isso que aparece no dropdown
    }
}