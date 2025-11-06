package com.model.Devices;

import com.controller.UrbanMonitoringCenter;
import com.model.DisconnectedTrafficLightException;
import com.model.UnrepairableDeviceException;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;


public class TrafficLightController extends FineIssuerDevice implements Runnable{
    @Serial
    private static final long serialVersionUID = -2100951556457963217L;

    private @Nullable LocalDateTime intermittentStartTime;
    private @Nullable LocalDateTime intermittentEndTime;

    private Duration redLightDuration;
    private Duration yellowLightDuration;
    private Duration greenLightDuration;
    private Duration bothRedLightsDuration;

    private boolean repairable;

    private final ArrayList<TrafficLight> intersectionLights = new ArrayList<>(2);
    // mainLight -> index=0
    // secondaryLight -> index=1

    public TrafficLightController( String address, Location location,boolean state, Duration redLightDuration, Duration yellowLightDuration, Duration greenLightDuration, Duration bothRedLightsDuration, TrafficLight mainLight, TrafficLight secondaryLight) {
        super(address, location, state,UrbanMonitoringCenter.getUrbanMonitoringCenter().getSpecificInfractionType("RedLightViolation"));
        this.redLightDuration = redLightDuration;
        this.yellowLightDuration = yellowLightDuration;
        this.greenLightDuration = greenLightDuration;
        this.bothRedLightsDuration = bothRedLightsDuration;
        intersectionLights.add(0, mainLight);
        intersectionLights.add(1, secondaryLight);
        repairable = mainLight.getCurrentState()!=TrafficLightState.UNKNOWN && secondaryLight.getCurrentState()!=TrafficLightState.UNKNOWN;
    }

    public TrafficLightController(String address, Location location,boolean state, TrafficLight mainLight, TrafficLight secondaryLight){
        super(address, location,state, UrbanMonitoringCenter.getUrbanMonitoringCenter().getSpecificInfractionType("CrossingRedLight"));
        redLightDuration = Duration.ofSeconds(30);
        yellowLightDuration = Duration.ofSeconds(4);
        greenLightDuration = Duration.ofSeconds(40);
        bothRedLightsDuration = Duration.ofSeconds(3);
        intersectionLights.add(0, mainLight);
        intersectionLights.add(1, secondaryLight);
        repairable = mainLight.getCurrentState()!=TrafficLightState.UNKNOWN && secondaryLight.getCurrentState()!=TrafficLightState.UNKNOWN;
    }

    public TrafficLightController(String address, Location location, boolean state, LocalDateTime intermittentStartTime, LocalDateTime intermittentEndTime, Duration redLightDuration, Duration yellowLightDuration, Duration greenLightDuration, Duration bothRedLightsDuration, TrafficLight mainLight, TrafficLight secondaryLight) {
        super(address, location,state, UrbanMonitoringCenter.getUrbanMonitoringCenter().getSpecificInfractionType("CrossingRedLight"));
        this.intermittentStartTime = intermittentStartTime;
        this.intermittentEndTime = intermittentEndTime;
        this.redLightDuration = redLightDuration;
        this.yellowLightDuration = yellowLightDuration;
        this.greenLightDuration = greenLightDuration;
        this.bothRedLightsDuration = bothRedLightsDuration;
        intersectionLights.add(mainLight);
        intersectionLights.add(secondaryLight);
        repairable = mainLight.getCurrentState()!=TrafficLightState.UNKNOWN && secondaryLight.getCurrentState()!=TrafficLightState.UNKNOWN;
    }

    //getters
    public LocalDateTime getIntermittentStartTime() { return intermittentStartTime; }
    public LocalDateTime getIntermittentEndTime() { return intermittentEndTime; }
    public Duration getRedLightDuration() { return redLightDuration; }
    public Duration getYellowLightDuration() { return yellowLightDuration; }
    public Duration getGreenLightDuration() { return greenLightDuration; }
    public Duration getBothRedLightsDuration() {
        return bothRedLightsDuration;
    }

    //setters
    public void setIntermittentStartTime(LocalDateTime intermittentStartTime) { this.intermittentStartTime = intermittentStartTime; }
    public void setIntermittentEndTime(LocalDateTime intermittentEndTime) { this.intermittentEndTime = intermittentEndTime; }
    public void setRedLightDuration(Duration redLightDuration) { this.redLightDuration = redLightDuration; }
    public void setYellowLightDuration(Duration yellowLightDuration){ this.yellowLightDuration = yellowLightDuration; }
    public void setGreenLightDuration(Duration greenLightDuration) {this.greenLightDuration = greenLightDuration; }
    public void setBothRedLightsDuration(Duration bothRedLightsDuration) { this.bothRedLightsDuration = bothRedLightsDuration; }

    public void changeTrafficLightsIntermittent(){
        try{
        intersectionLights.get(0).changeState(TrafficLightState.INTERMITTENT);
        intersectionLights.get(1).changeState(TrafficLightState.INTERMITTENT);
        } catch (DisconnectedTrafficLightException e) {
            breakDevice();
            repairable= false;
        }
    }

    public void startTrafficLights(){
        intersectionLights.get(0).setState(TrafficLightState.GREEN);
        intersectionLights.get(1).setState(TrafficLightState.RED);
    }

    @Override
    public void repair() throws UnrepairableDeviceException{
        if(repairable)
            super.repair();
        else
            throw new UnrepairableDeviceException(this,"One of the traffic lights lost communication");
    }

    @Override
    public void run() {

    }
}