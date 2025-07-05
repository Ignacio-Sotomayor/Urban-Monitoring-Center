package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class TrafficLightController{
    private LocalDateTime intermittentStartTime;
    private LocalDateTime intermittentEndTime;

    private Duration redLightDuration;
    private Duration yellowLightDuration;
    private Duration greenLightDuration;
    private Duration bothRedLightsDuration;

    private final Set<TrafficLight> intersections = new HashSet<>();


    public TrafficLightController(Duration redLightDuration, Duration yellowLightDuration, Duration greenLightDuration, Duration bothRedLightsDuration, TrafficLight mainLight, TrafficLight secondaryLight) {
        this.redLightDuration = redLightDuration;
        this.yellowLightDuration = yellowLightDuration;
        this.greenLightDuration = greenLightDuration;
        this.bothRedLightsDuration = bothRedLightsDuration;
        intersections.add(mainLight);
        intersections.add(secondaryLight);
    }

    public TrafficLightController(LocalDateTime intermittentStartTime, LocalDateTime intermittentEndTime, Duration redLightDuration, Duration yellowLightDuration, Duration greenLightDuration, Duration bothRedLightsDuration, TrafficLight mainLight, TrafficLight secondaryLight) {
        this.intermittentStartTime = intermittentStartTime;
        this.intermittentEndTime = intermittentEndTime;
        this.redLightDuration = redLightDuration;
        this.yellowLightDuration = yellowLightDuration;
        this.greenLightDuration = greenLightDuration;
        this.bothRedLightsDuration = bothRedLightsDuration;
        intersections.add(mainLight);
        intersections.add(secondaryLight);
    }

    public LocalDateTime getIntermittentStartTime() {
        return intermittentStartTime;
    }

    public void setIntermittentStartTime(LocalDateTime intermittentStartTime) {
        this.intermittentStartTime = intermittentStartTime;
    }

    public LocalDateTime getIntermittentEndTime() {
        return intermittentEndTime;
    }

    public void setIntermittentEndTime(LocalDateTime intermittentEndTime) {
        this.intermittentEndTime = intermittentEndTime;
    }

    public Duration getRedLightDuration() {
        return redLightDuration;
    }

    public void setRedLightDuration(Duration redLightDuration) {
        this.redLightDuration = redLightDuration;
    }

    public Duration getYellowLightDuration() {
        return yellowLightDuration;
    }

    public void setYellowLightDuration(Duration yellowLightDuration) {
        this.yellowLightDuration = yellowLightDuration;
    }

    public Duration getGreenLightDuration() {
        return greenLightDuration;
    }

    public void setGreenLightDuration(Duration greenLightDuration) {
        this.greenLightDuration = greenLightDuration;
    }

    public Duration getBothRedLightsDuration() {
        return bothRedLightsDuration;
    }

    public void setBothRedLightsDuration(Duration bothRedLightsDuration) {
        this.bothRedLightsDuration = bothRedLightsDuration;
    }

    public void changeTrafficLightsIntermittent(){
        for(TrafficLight f : intersections)
            f.startIntermittentState();
    }

}