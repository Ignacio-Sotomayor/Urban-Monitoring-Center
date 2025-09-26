package com.model.Devices;

import java.io.Serializable;

import java.math.BigDecimal;

public class Location implements Serializable {
    private BigDecimal latitude, longitude;

    public Location(BigDecimal latitude, BigDecimal longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String toString(){
        StringBuilder builder = new StringBuilder();
        builder.append(latitude);
        builder.append(longitude);
        return builder.toString();
    }
}