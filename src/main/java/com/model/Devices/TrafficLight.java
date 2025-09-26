package com.model.Devices;
import com.model.Devices.TrafficLightState;

public class TrafficLight {
    private String street;
    private String orientation;
    private boolean isMain;
    private TrafficLightState currentState;

    public TrafficLight(String street, String orientation, boolean isMain) {
        this.street = street;
        this.orientation = orientation;
        this.isMain = isMain;
    }

    //getters
    public boolean getIsMain(){ return isMain; }
    public String getStreet() { return street; }
    public String getOrientation() { return orientation; }
    public TrafficLightState getCurrentState(){ return currentState; }

    public void changeState(TrafficLightState newState){ this.currentState = newState; }

}