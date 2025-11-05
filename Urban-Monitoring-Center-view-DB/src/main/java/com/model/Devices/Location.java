package com.model.Devices;

import java.io.Serial;
import java.io.Serializable;

import java.math.BigDecimal;

public class Location implements Serializable {
    @Serial
    private static final long serialVersionUID = -2663931308894801511L;
    private BigDecimal latitude, longitude;

    public Location(BigDecimal latitude, BigDecimal longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public double distanceTo(Location other) {
        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(other.latitude.doubleValue() - this.latitude.doubleValue());
        double lonDistance = Math.toRadians(other.longitude.doubleValue() - this.longitude.doubleValue());

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(this.latitude.doubleValue())) * Math.cos(Math.toRadians(other.latitude.doubleValue()))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c * 1000; // convert to meters
    }

    public String toString(){
        StringBuilder builder = new StringBuilder();
        builder.append(latitude);
        builder.append(longitude);
        return builder.toString();
    }
}