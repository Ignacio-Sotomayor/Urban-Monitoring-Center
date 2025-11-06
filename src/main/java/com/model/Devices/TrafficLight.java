package com.model.Devices;
import com.model.Devices.TrafficLightState;
import com.model.UnrepairableDeviceException;
import com.model.DisconnectedTrafficLightException;

import java.io.Serial;
import java.io.Serializable;
import java.util.Random;

public class TrafficLight implements Serializable {
    @Serial
    private static final long serialVersionUID = -179790607119410917L;
    private String street;
    private String orientation;
    private boolean isMain;
    private TrafficLightState currentState;

    public TrafficLight(String street, String orientation, boolean isMain) {
        this.street = street;
        this.orientation = orientation;
        this.isMain = isMain;
        currentState = (isMain)?TrafficLightState.GREEN:TrafficLightState.RED;
    }

    //getters
    public boolean getIsMain(){ return isMain; }
    public String getStreet() { return street; }
    public String getOrientation() { return orientation; }
    public TrafficLightState getCurrentState(){ return currentState; }

    public void changeState(TrafficLightState newState) throws DisconnectedTrafficLightException{
        Random random = new Random();
        this.currentState = (random.nextDouble()>0.005 )?newState:TrafficLightState.UNKNOWN;
        if(currentState == TrafficLightState.UNKNOWN)
            throw new DisconnectedTrafficLightException("");
    }

    public void setState(TrafficLightState trafficLightState) {
        this.currentState= trafficLightState;
    }
}