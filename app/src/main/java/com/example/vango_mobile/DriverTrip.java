package com.example.vango_mobile;

public class DriverTrip {
    private String tripId;
    private String departureLabel;
    private String arrivalLabel;
    private String departureTime;
    private String pngRoute;

    public DriverTrip(String tripId, String departureLabel, String arrivalLabel, String departureTime, String pngRoute) {
        this.tripId = tripId;
        this.departureLabel = departureLabel;
        this.arrivalLabel = arrivalLabel;
        this.departureTime = departureTime;
        this.pngRoute = pngRoute;
    }

    public String getTripId() {
        return tripId;
    }

    public String getDepartureLabel() {
        return departureLabel;
    }

    public String getArrivalLabel() {
        return arrivalLabel;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public String getPngRoute() {
        return pngRoute;
    }
}

