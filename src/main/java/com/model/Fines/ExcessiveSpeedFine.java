package com.model.Fines;

import com.model.Automobile.Automobile;

import java.util.Set;

public class ExcessiveSpeedFine extends Fine {
    private final double automobileSpeed;
    private final double speedLimit;

    public ExcessiveSpeedFine(Integer FineID, EventGeolocation eventGeolocation, InfractionType infractionType, Automobile automobile, Set<Photo> photoSet, double speedLimit, double automobileSpeed) {
        super(FineID, eventGeolocation, infractionType, automobile, photoSet);
        this.speedLimit = speedLimit;
        this.automobileSpeed = automobileSpeed;
    }
    public ExcessiveSpeedFine(EventGeolocation eventGeolocation, InfractionType infractionType, Automobile automobile, double speedLimit, double automobileSpeed) {
        super( eventGeolocation, infractionType, automobile);
        this.speedLimit = speedLimit;
        this.automobileSpeed = automobileSpeed;
    }

    //getters
    public double getAutomobileSpeed() {
        return automobileSpeed;
    }
    public double getSpeedLimit() {
        return speedLimit;
    }

    @Override
    public String toString() {
        return super.toString() + "automobileSpeed =" + automobileSpeed + ", speedLimit =" + speedLimit;
    }
}