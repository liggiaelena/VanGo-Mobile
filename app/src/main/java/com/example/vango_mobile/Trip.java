package com.example.vango_mobile;
public class Trip {
    private String origin;
    private String destination;
    private String arrivalTime;

    public Trip(String origin, String destination, String arrivalTime) {
        this.origin = origin;
        this.destination = destination;
        this.arrivalTime = arrivalTime;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }
}
