package com.model.Devices;

public class Location {
    private double latitude, longitude;

    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String toString(){
        StringBuilder builder = new StringBuilder();
        builder.append(latitude);
        builder.append(longitude);
        return builder.toString();
    }
}