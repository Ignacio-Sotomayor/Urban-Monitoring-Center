package com.model.Devices;

import com.model.UrbanMonitoringCenter;

import java.time.Duration;

public class ParkingLotSecurityCamera extends FineIssuerDevice {
    private Duration toleranceTime;

    public ParkingLotSecurityCamera( String address, Location location, Duration toleranceTime) {
        super(address, location, UrbanMonitoringCenter.getUrbanMonitoringCenter().getSpecificInfractionType("ParkingOvertime"));
        this.toleranceTime = toleranceTime;
    }

}