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

    public String toString(){
        StringBuilder builder = new StringBuilder();
        builder.append(latitude);
        builder.append(longitude);
        return builder.toString();
    }
}