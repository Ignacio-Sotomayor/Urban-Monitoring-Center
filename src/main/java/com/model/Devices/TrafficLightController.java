package com.model.Devices;

import com.controller.UrbanMonitoringCenter;
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
        repairable = mainLight.isOperative() && secondaryLight.isOperative() &&
                     mainLight.getCurrentState() != TrafficLightState.UNKNOWN &&
                     secondaryLight.getCurrentState() != TrafficLightState.UNKNOWN;
    }

    public TrafficLightController(String address, Location location,boolean state, TrafficLight mainLight, TrafficLight secondaryLight){
        super(address, location,state, UrbanMonitoringCenter.getUrbanMonitoringCenter().getSpecificInfractionType("RedLightViolation"));
        redLightDuration = Duration.ofSeconds(30);
        yellowLightDuration = Duration.ofSeconds(4);
        greenLightDuration = Duration.ofSeconds(40);
        bothRedLightsDuration = Duration.ofSeconds(3);
        intersectionLights.add(0, mainLight);
        intersectionLights.add(1, secondaryLight);
        repairable = mainLight.isOperative() && secondaryLight.isOperative() &&
                     mainLight.getCurrentState() != TrafficLightState.UNKNOWN &&
                     secondaryLight.getCurrentState() != TrafficLightState.UNKNOWN;
    }

    public TrafficLightController(String address, Location location, boolean state, @Nullable LocalTime intermittentStartTime, @Nullable LocalTime intermittentEndTime, Duration redLightDuration, Duration yellowLightDuration, Duration greenLightDuration, Duration bothRedLightsDuration, TrafficLight mainLight, TrafficLight secondaryLight) {
        super(address, location,state, UrbanMonitoringCenter.getUrbanMonitoringCenter().getSpecificInfractionType("RedLightViolation"));
        this.intermittentStartTime = intermittentStartTime;
        this.intermittentEndTime = intermittentEndTime;
        this.redLightDuration = redLightDuration;
        this.yellowLightDuration = yellowLightDuration;
        this.greenLightDuration = greenLightDuration;
        this.bothRedLightsDuration = bothRedLightsDuration;
        intersectionLights.add(mainLight);
        intersectionLights.add(secondaryLight);
        repairable = mainLight.isOperative() && secondaryLight.isOperative() &&
                     mainLight.getCurrentState() != TrafficLightState.UNKNOWN &&
                     secondaryLight.getCurrentState() != TrafficLightState.UNKNOWN;
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

    public void setEmittedInfractionType(){
        super.setEmittedInfractionType(UrbanMonitoringCenter.getUrbanMonitoringCenter().getSpecificInfractionType("RedLightViolation"));
    }

    public void changeTrafficLightsIntermittent(){
        intersectionLights.get(0).changeState(TrafficLightState.INTERMITTENT);
        intersectionLights.get(1).changeState(TrafficLightState.INTERMITTENT);
    }
    public void changeTrafficLights(TrafficLightState mainLightState, TrafficLightState secondaryLightState){
        intersectionLights.get(0).changeState(mainLightState);
        intersectionLights.get(1).changeState(secondaryLightState);
    }
    public boolean isIntermittentTime() {
        LocalTime now = LocalTime.now();
        boolean isIntermittentPeriod = false;
        if (intermittentStartTime != null && intermittentEndTime != null) {
            if (intermittentStartTime.isAfter(intermittentEndTime)) {
                isIntermittentPeriod = now.isAfter(intermittentStartTime) || now.isBefore(intermittentEndTime);
            } else {
                isIntermittentPeriod = now.isAfter(intermittentStartTime) && now.isBefore(intermittentEndTime);
            }
        }
        return forceIntermittent || isIntermittentPeriod;
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
        // Priority: Controller Fatal Error > Controller Inoperative > Intermittent Time > Individual Light State
        if (UrbanMonitoringCenter.getUrbanMonitoringCenter().isFatalError(this)) { // Check controller fatal error
            URL resource = getClass().getResource("/Icons/FatalErrorTrafficLight.png");
            return resource != null ? resource.toExternalForm() : "";
        }
        if (!getState()) { // Check controller inoperative (not fatal)
            URL resource = getClass().getResource("/Icons/InoperativeTrafficLight.png");
            return resource != null ? resource.toExternalForm() : "";
        }
        // If no controller-level issues, check individual main light state
        TrafficLight main = intersectionLights.get(0);
        if (main.getCurrentState() == TrafficLightState.UNKNOWN || main.getCurrentState() == TrafficLightState.INOPERATIVE) {
            URL resource = getClass().getResource(main.getCurrentState().getIconPath());
            return resource != null ? resource.toExternalForm() : "";
        }
        // If intermittent time, show yellow icon
        if (isIntermittentTime()) {
            URL resource = getClass().getResource(TrafficLightState.INTERMITTENT.getIconPath());
            return resource != null ? resource.toExternalForm() : "";
        }
        URL resource = getClass().getResource(main.getCurrentState().getIconPath());
        return resource != null ? resource.toExternalForm() : "";
    }

    @Override
    public String getDeviceTypeName() {
        return "TrafficLightController";
    }

    @Override
    public String getDeviceSpecificInfo() {
        TrafficLight main = intersectionLights.get(0);
        TrafficLight secondary = intersectionLights.get(1);

        String info = "";
        if (isIntermittentTime()) {
            info += "<br><b>MODO INTERMITTENTE</b>";
        } else {
            info += "<br>Principal: " + main.getCurrentState();
            if (!main.isOperative()) info += " (Inoperativa)";
            info += "<br>Secundario: " + secondary.getCurrentState();
            if (!secondary.isOperative()) info += " (Inoperativa)";
        }
        return info;
    }

    @Override
    public void run() {
        TrafficLight main = intersectionLights.get(0);
        TrafficLight secondary = intersectionLights.get(1);
        try {
            Thread.sleep(initialDelay);

            while (true) {

                // If controller is in fatal error, stop its cycle
                if (UrbanMonitoringCenter.getUrbanMonitoringCenter().isFatalError(this)) {
                    Thread.sleep(5000); // Wait if in fatal error
                    continue;
                }

                // Handle Intermittent Time (sustained yellow)
                if (isIntermittentTime()) {
                    changeTrafficLightsIntermittent(); // This method sets lights to INTERMITTENT
                    Thread.sleep(10000); // Stay in intermittent mode for a while
                    continue; // Skip the normal cycle
                }

                // Ensure lights are operative before starting cycle if they were inoperative (but not UNKNOWN)
                // If a light is INOPERATIVE (not UNKNOWN), setting a new state will make it operative again
                if (main.getCurrentState() == TrafficLightState.UNKNOWN || secondary.getCurrentState() == TrafficLightState.UNKNOWN) {
                    Thread.sleep(5000); // Wait if in fatal error
                    continue;
                }

                // Step 1: Semaforo A en verde y B en rojo durante 40 seg
                main.changeState(TrafficLightState.GREEN);
                secondary.changeState(TrafficLightState.RED);
                // Removed checkFaults() call
                Thread.sleep(greenLightDuration.toMillis());

                // Step 2: Semaforo A en amarillo y B en rojo durante 4 seg
                main.changeState(TrafficLightState.YELLOW); // Use YELLOW for transition
                secondary.changeState(TrafficLightState.RED);
                // Removed checkFaults() call
                Thread.sleep(yellowLightDuration.toMillis());

                // Step 3: Ambos en rojo durante 3 seg (si el tiempo es mayor se reporta como fallo)
                main.changeState(TrafficLightState.RED);
                secondary.changeState(TrafficLightState.RED);
                // Removed checkFaults() call
                Thread.sleep(bothRedLightsDuration.toMillis());
                // Removed both lights RED for too long fault detection

                // Step 4: Semaforo A en rojo y B en verde durante 30 seg
                main.changeState(TrafficLightState.RED);
                secondary.changeState(TrafficLightState.GREEN);
                // Removed checkFaults() call
                Thread.sleep(redLightDuration.toMillis()); // redLightDuration is for the other light's green

                // Step 5: Semaforo A en rojo y B en amarillo durante 4 seg
                main.changeState(TrafficLightState.RED);
                secondary.changeState(TrafficLightState.YELLOW); // Use YELLOW for transition
                // Removed checkFaults() call
                Thread.sleep(yellowLightDuration.toMillis());

                // Step 6: Ambos en rojo durante 3 seg (si el tiempo es mayor se reporta como fallo)
                main.changeState(TrafficLightState.RED);
                secondary.changeState(TrafficLightState.RED);
                // Removed checkFaults() call
                Thread.sleep(bothRedLightsDuration.toMillis());
                // Removed both lights RED for too long fault detection
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}