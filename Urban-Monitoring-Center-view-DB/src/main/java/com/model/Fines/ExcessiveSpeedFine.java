package com.model.Fines;

import com.model.Automobile.Automobile;

import java.util.Set;

public class ExcessiveSpeedFine extends Fine {
    private final int automobileSpeed;
    private final int speedLimit;

    public ExcessiveSpeedFine(Integer FineID, EventGeolocation eventGeolocation, InfractionType infractionType, Automobile automobile, Set<Photo> photoSet, int speedLimit, int automobileSpeed) {
        super(FineID, eventGeolocation, infractionType, automobile, photoSet);
        this.automobileSpeed = automobileSpeed;
        this.speedLimit = speedLimit;
    }
    public ExcessiveSpeedFine(EventGeolocation eventGeolocation, InfractionType infractionType, Automobile automobile, int speedLimit, int automobileSpeed) {
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