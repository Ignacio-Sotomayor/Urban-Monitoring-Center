package com.model.Devices;

import com.model.Devices.TrafficLightState;
import com.model.Fines.InfractionType;
import com.model.UrbanMonitoringCenter;
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

    private final ArrayList<TrafficLight> intersectionLights = new ArrayList<>(2);
    // mainLight -> index=0
    // secondaryLight -> index=1

    public TrafficLightController( String address, Location location,boolean state, Duration redLightDuration, Duration yellowLightDuration, Duration greenLightDuration,
                                   Duration bothRedLightsDuration, TrafficLight mainLight, TrafficLight secondaryLight) {
        super(address, location, state,UrbanMonitoringCenter.getUrbanMonitoringCenter().getSpecificInfractionType("RedLightViolation"));
        this.redLightDuration = redLightDuration;
        this.yellowLightDuration = yellowLightDuration;
        this.greenLightDuration = greenLightDuration;
        this.bothRedLightsDuration = bothRedLightsDuration;
        intersectionLights.add(0, mainLight);
        intersectionLights.add(1, secondaryLight);
    }


    public TrafficLightController(String address, Location location, boolean state,
                                  InfractionType infractionType,
                                  TrafficLight mainLight, TrafficLight secondaryLight) {
        super(address, location, state, infractionType);
        redLightDuration = Duration.ofSeconds(30);
        yellowLightDuration = Duration.ofSeconds(4);
        greenLightDuration = Duration.ofSeconds(40);
        bothRedLightsDuration = Duration.ofSeconds(3);
        intersectionLights.add(0, mainLight);
        intersectionLights.add(1, secondaryLight);
    }

    public TrafficLightController(String address, Location location, boolean state, LocalDateTime intermittentStartTime, LocalDateTime intermittentEndTime, Duration redLightDuration, Duration yellowLightDuration, Duration greenLightDuration, Duration bothRedLightsDuration, TrafficLight mainLight, TrafficLight secondaryLight) {
        super(address, location,state, UrbanMonitoringCenter.getUrbanMonitoringCenter().getSpecificInfractionType("RedLightViolation"));
        this.intermittentStartTime = intermittentStartTime;
        this.intermittentEndTime = intermittentEndTime;
        this.redLightDuration = redLightDuration;
        this.yellowLightDuration = yellowLightDuration;
        this.greenLightDuration = greenLightDuration;
        this.bothRedLightsDuration = bothRedLightsDuration;
        intersectionLights.add(mainLight);
        intersectionLights.add(secondaryLight);
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
        intersectionLights.get(0).changeState(TrafficLightState.INTERMITTENT);
        intersectionLights.get(1).changeState(TrafficLightState.INTERMITTENT);
    }

    public void startTrafficLights(){
        intersectionLights.get(0).changeState(TrafficLightState.GREEN);
        intersectionLights.get(1).changeState(TrafficLightState.RED);
    }

    @Override
    public void run() {

    }

    @Override
    public String getDefaultInfractionTypeName() {
        return "RedLightViolation";
    }

}