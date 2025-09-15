package com.model.Fines;

import com.model.Automobile.Automobile;

public class ExcessiveSpeedFine extends Fine {
    private int automobileSpeed;
    private int speedLimit;

    public ExcessiveSpeedFine(double amount, int scoring, EventGeolocation eventGeolocation, InfractionType infractionType, Automobile automobile, int automobileSpeed, int speedLimit) {
        super( eventGeolocation, infractionType, automobile);
        this.automobileSpeed = automobileSpeed;
        this.speedLimit = speedLimit;
    }

    //getters
    public int getAutomobileSpeed() {
        return automobileSpeed;
    }
    public int getSpeedLimit() {
        return speedLimit;
    }

    @Override
    public String toString() {
        return super.toString() + ", ExcessiveSpeedFine [automobileSpeed=" + automobileSpeed + ", speedLimit=" + speedLimit + "]";
    }
}