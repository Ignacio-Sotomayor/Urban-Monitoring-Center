package com.model.Devices;

import java.io.Serial;

public class SecurityCamera extends Device{

    @Serial
    private static final long serialVersionUID = 8877992666445631151L;

    public SecurityCamera(String address, Location location, boolean state) {
        super(address, location,state);
    }
}