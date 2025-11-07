package com.model.Devices;

import com.controller.UrbanMonitoringCenter;

import java.io.Serial;
import java.net.URL;
import java.time.Duration;

public class ParkingLotSecurityCamera extends FineIssuerDevice {
    @Serial
    private static final long serialVersionUID = -8597720654876321354L;
    private Duration toleranceTime;

    public ParkingLotSecurityCamera( String address, Location location,boolean state, Duration toleranceTime) {
        super(address, location,state, UrbanMonitoringCenter.getUrbanMonitoringCenter().getSpecificInfractionType("ParkingOverTime"));
        this.toleranceTime = toleranceTime;
    }

    @Override
    public String getIconPath() {
        String path = (getState())?"/Icons/OperativeParkingLotCamera.png" : "/Icons/InoperativeParkingLotCamera.png";
        URL resource = getClass().getResource(path);
        return resource != null ? resource.toExternalForm() : "";
    }

    @Override
    public String getDeviceTypeName() {
        return "ParkingLotSecurityCamera";
    }

    @Override
    public String getDeviceSpecificInfo() {
        return "<br>Tiempo de tolerancia: " + toleranceTime.toMinutes() + " minutos";
    }
}