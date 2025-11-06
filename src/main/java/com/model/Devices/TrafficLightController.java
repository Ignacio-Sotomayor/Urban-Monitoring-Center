package com.model.Devices;

import com.model.UrbanMonitoringCenter;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;


public class TrafficLightController extends FineIssuerDevice {
    @Serial
    private static final long serialVersionUID = -2100951556457963217L;

    private @Nullable LocalTime intermittentStartTime;
    private @Nullable LocalTime intermittentEndTime;
    private boolean forceIntermittent = false;

    private Duration redLightDuration;
    private Duration yellowLightDuration;
    private Duration greenLightDuration;
    private Duration bothRedLightsDuration;
    private long initialDelay = 0;

    private final ArrayList<TrafficLight> intersectionLights = new ArrayList<>(2);
    // mainLight -> index=0
    // secondaryLight -> index=1

    public TrafficLightController( String address, Location location,boolean state, Duration redLightDuration, Duration yellowLightDuration, Duration greenLightDuration, Duration bothRedLightsDuration, TrafficLight mainLight, TrafficLight secondaryLight) {
        super(address, location, state,UrbanMonitoringCenter.getUrbanMonitoringCenter().getSpecificInfractionType("CrossingRedLight"));
        this.redLightDuration = redLightDuration;
        this.yellowLightDuration = yellowLightDuration;
        this.greenLightDuration = greenLightDuration;
        this.bothRedLightsDuration = bothRedLightsDuration;
        intersectionLights.add(0, mainLight);
        intersectionLights.add(1, secondaryLight);
    }

    public TrafficLightController(String address, Location location,boolean state, TrafficLight mainLight, TrafficLight secondaryLight){
        super(address, location,state, UrbanMonitoringCenter.getUrbanMonitoringCenter().getSpecificInfractionType("CrossingRedLight"));
        redLightDuration = Duration.ofSeconds(5);
        yellowLightDuration = Duration.ofSeconds(2);
        greenLightDuration = Duration.ofSeconds(5);
        bothRedLightsDuration = Duration.ofSeconds(1);
        intersectionLights.add(0, mainLight);
        intersectionLights.add(1, secondaryLight);
        this.intermittentStartTime = LocalTime.of(22, 0); // Example: 10 PM
        this.intermittentEndTime = LocalTime.of(6, 0);   // Example: 6 AM
    }

    //getters
    public long getTotalCycleMillis() {
        return (greenLightDuration.toMillis() +
                yellowLightDuration.toMillis() +
                bothRedLightsDuration.toMillis() +
                redLightDuration.toMillis() + // This is green for the other light
                yellowLightDuration.toMillis() +
                bothRedLightsDuration.toMillis());
    }
    public ArrayList<TrafficLight> getIntersectionLights() { return intersectionLights; }
    public long getInitialDelay() { return initialDelay; }

    public @Nullable LocalTime getIntermittentEndTime() {
        return intermittentEndTime;
    }

    public @Nullable LocalTime getIntermittentStartTime() {
        return intermittentStartTime;
    }

    public Duration getRedLightDuration() {
        return redLightDuration;
    }

    public Duration getYellowLightDuration() {
        return yellowLightDuration;
    }

    public Duration getGreenLightDuration() {
        return greenLightDuration;
    }

    public Duration getBothRedLightsDuration() {
        return bothRedLightsDuration;
    }

    //setters
    public void setInitialDelay(long initialDelay) { this.initialDelay = initialDelay; }
    public void setForceIntermittent(boolean forceIntermittent) { this.forceIntermittent = forceIntermittent; }

    public boolean isIntermittentTime() {
        if (forceIntermittent) return true;
        else if (intermittentStartTime == null || intermittentEndTime == null)
            return false;
        else {
            LocalTime now = LocalTime.now();
            if (intermittentStartTime.isAfter(intermittentEndTime))
                return now.isAfter(intermittentStartTime) || now.isBefore(intermittentEndTime);
            else
                return now.isAfter(intermittentStartTime) && now.isBefore(intermittentEndTime);
        }
    }

    public void setGreenWaveState(TrafficLightState mainState, TrafficLightState secondaryState) {
        intersectionLights.get(0).changeState(mainState);
        intersectionLights.get(1).changeState(secondaryState);
    }
}