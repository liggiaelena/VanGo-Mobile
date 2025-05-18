package com.example.vango_mobile;
public class Trip {
    public String origin;
    public String destination;
    public String arrivalTime;
    public int spotsAvailable;
    public double price;
    public String tripType;

    public Trip(String origin, String destination, String arrivalTime, int spotsAvailable, double price, String tripType) {
        this.origin = origin;
        this.destination = destination;
        this.arrivalTime = arrivalTime;
        this.spotsAvailable = spotsAvailable;
        this.price = price;
        this.tripType = tripType;
    }
}
