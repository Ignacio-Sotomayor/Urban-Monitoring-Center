package com.model.Devices;

import com.model.Automobile.Automobile;
import com.model.UrbanMonitoringCenter;

public class Radar extends FineIssuerDevice {
    private double speedLimit;

    public Radar( String address, Location location,boolean state, double speedLimit) {
        super(address, location,state, UrbanMonitoringCenter.getUrbanMonitoringCenter().getSpecificInfractionType("ExcessiveSpeed"));
        this.speedLimit = speedLimit;
    }

    public void setSpeedLimit(double speedLimit) {
        this.speedLimit = speedLimit;
    }
    public void issueFine(Automobile a, double speed) {
        if (speed > speedLimit) {
            super.issueFine(a);
        }
    }
}