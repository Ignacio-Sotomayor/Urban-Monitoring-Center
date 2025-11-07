package com.model.Devices;
import com.model.Devices.TrafficLightState;
import com.model.UnrepairableDeviceException;
import com.model.DisconnectedTrafficLightException;

import java.io.Serial;
import java.io.Serializable;
import java.net.URL;
import java.util.Random;

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
        this.operative = true; // Por defecto, la luz está operativa
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
        // Si el estado actual es UNKNOWN, la luz está en un error fatal y no puede cambiar de estado.
        if (this.currentState == TrafficLightState.UNKNOWN) {
            return; // No se puede cambiar el estado de una luz en error fatal
        }

        if (newState == TrafficLightState.UNKNOWN) {
            this.operative = false;
            this.currentState = TrafficLightState.UNKNOWN;
        } else if (newState == TrafficLightState.INOPERATIVE) {
            this.operative = false;
            this.currentState = TrafficLightState.INOPERATIVE;
        } else {
            // Si estaba inoperativa y se le pide un estado operativo, se considera reparada
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