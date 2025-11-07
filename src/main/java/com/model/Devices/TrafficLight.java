package com.model.Devices;


import java.io.Serial;
import java.io.Serializable;

public class TrafficLight implements Serializable {
    @Serial
    private static final long serialVersionUID = -179790607119410917L;
    private String street;
    private String orientation;
    private boolean isMain;
    private TrafficLightState currentState;
    private boolean operative;

    public TrafficLight(String street, String orientation, boolean isMain, TrafficLightState currentState) {
        this.street = street;
        this.orientation = orientation;
        this.isMain = isMain;
        this.currentState = currentState;
        this.operative = true;
    }

    public TrafficLight(String street, String orientation, boolean isMain) {
        this.street = street;
        this.orientation = orientation;
        this.isMain = isMain;
        this.operative = true; // Operative by default
        currentState = (isMain)?TrafficLightState.GREEN:TrafficLightState.RED;
    }

    //getters
    public boolean getIsMain(){ return isMain; }
    public String getStreet() { return street; }
    public String getOrientation() { return orientation; }
    public TrafficLightState getCurrentState(){ return currentState; }
    public boolean isOperative() { return operative; }

    //setters
    public void setOperative(boolean operative) {
        this.operative = operative;
    }

    public void changeState(TrafficLightState newState) {
        // If traffic light state is UNKNOWN, it is a fatal error and cannot be repaired
        if (this.currentState == TrafficLightState.UNKNOWN) {
            return;
        }

        if (newState == TrafficLightState.UNKNOWN) {
            this.operative = false;
            this.currentState = TrafficLightState.UNKNOWN;
        } else if (newState == TrafficLightState.INOPERATIVE) {
            this.operative = false;
            this.currentState = TrafficLightState.INOPERATIVE;
        } else {
            // If it was INOPERATIVE, it is repaired when told
            if (!this.operative && (newState == TrafficLightState.RED || newState == TrafficLightState.GREEN || newState == TrafficLightState.YELLOW || newState == TrafficLightState.INTERMITTENT)) {
                this.operative = true;
            }
            this.currentState = newState;
        }
    }

    public void setState(TrafficLightState trafficLightState) {
        changeState(trafficLightState);
    }
}