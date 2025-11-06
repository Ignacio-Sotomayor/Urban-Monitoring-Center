package com.model.Devices;

import com.controller.UrbanMonitoringCenter;
import com.model.DisconnectedTrafficLightException;
import com.model.UnrepairableDeviceException;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;
import java.net.URL;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;


public class TrafficLightController extends FineIssuerDevice implements Runnable{
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

    public TrafficLightController(String address, Location location, boolean state, @Nullable LocalTime intermittentStartTime, @Nullable LocalTime intermittentEndTime, Duration redLightDuration, Duration yellowLightDuration, Duration greenLightDuration, Duration bothRedLightsDuration, TrafficLight mainLight, TrafficLight secondaryLight) {
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
    public @Nullable LocalTime getIntermittentStartTime() { return intermittentStartTime; }
    public @Nullable LocalTime getIntermittentEndTime() { return intermittentEndTime; }
    public Duration getRedLightDuration() { return redLightDuration; }
    public Duration getYellowLightDuration() { return yellowLightDuration; }
    public Duration getGreenLightDuration() { return greenLightDuration; }
    public Duration getBothRedLightsDuration() {
        return bothRedLightsDuration;
    }

    //setters
    public void setIntermittentStartTime(LocalTime intermittentStartTime) { this.intermittentStartTime = intermittentStartTime; }
    public void setIntermittentEndTime(LocalTime intermittentEndTime) { this.intermittentEndTime = intermittentEndTime; }
    public void setRedLightDuration(Duration redLightDuration) { this.redLightDuration = redLightDuration; }
    public void setYellowLightDuration(Duration yellowLightDuration){ this.yellowLightDuration = yellowLightDuration; }
    public void setGreenLightDuration(Duration greenLightDuration) {this.greenLightDuration = greenLightDuration; }
    public void setBothRedLightsDuration(Duration bothRedLightsDuration) { this.bothRedLightsDuration = bothRedLightsDuration; }
    public void setInitialDelay(long initialDelay) { this.initialDelay = initialDelay; }
    public void setForceIntermittent(boolean forceIntermittent) { this.forceIntermittent = forceIntermittent; }

    public void changeTrafficLightsIntermittent(){
        try{
        intersectionLights.get(0).changeState(TrafficLightState.INTERMITTENT);
        intersectionLights.get(1).changeState(TrafficLightState.INTERMITTENT);
        } catch (DisconnectedTrafficLightException e) {
            breakDevice();
            repairable= false;
        }
    }
    public void changeTrafficLights(TrafficLightState mainLightState, TrafficLightState secondaryLightState){
        intersectionLights.get(0).changeState(mainLightState);
        intersectionLights.get(1).changeState(secondaryLightState);
    }
    public void startTrafficLights(){
        intersectionLights.get(0).setState(TrafficLightState.GREEN);
        intersectionLights.get(1).setState(TrafficLightState.RED);
    }
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

    @Override
    public void repair() throws UnrepairableDeviceException{
        if(repairable)
            super.repair();
        else
            throw new UnrepairableDeviceException(this,"One of the traffic lights lost communication");
    }

    @Override
    public String getIconPath() {
        String path;
        if(! getState() && !repairable)
            path="/Icons/FatalErrorTrafficLight.png";
        else if (!getState())
            path = "/Icons/InoperativeTrafficLight.png";
        else
            path = (this.intersectionLights.getFirst().getCurrentState()!=null)?this.intersectionLights.getFirst().getCurrentState().getIconPath():TrafficLightState.UNKNOWN.getIconPath();

        URL resource = getClass().getResource(path);
        return resource != null ? resource.toExternalForm() : "";
    }

    @Override
    public void run() {
        TrafficLight main = intersectionLights.get(0);
        TrafficLight secondary = intersectionLights.get(1);
        try {
            while (true){
            if(isIntermittentTime())
                changeTrafficLightsIntermittent();
            else{
                startTrafficLights();
                Thread.sleep(greenLightDuration);
                main.changeState(TrafficLightState.INTERMITTENT);
                Thread.sleep(yellowLightDuration);
                main.changeState(TrafficLightState.RED);
                Thread.sleep(bothRedLightsDuration);
                secondary.changeState(TrafficLightState.GREEN);
                Thread.sleep(redLightDuration);
                secondary.changeState(TrafficLightState.INTERMITTENT);
                Thread.sleep(yellowLightDuration);
                secondary.changeState(TrafficLightState.RED);
                Thread.sleep(bothRedLightsDuration);
                }
                }
            }catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}