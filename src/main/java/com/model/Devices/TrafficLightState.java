package com.model.Devices;

public enum TrafficLightState {
    RED("TrafficLightRed.png"),
    GREEN("TrafficLightGreen.png"),
    INTERMITTENT("TrafficLightYelloW.png"),
    UNKNOWN("FatalErrorTrafficLight.png"),
    YELLOW("TrafficLightYelloW.png"),
    INOPERATIVE("InoperativeTrafficLight.png");
    private final String iconPath;

    TrafficLightState(String iconPath) {
        this.iconPath = iconPath;
    }

    public String getIconPath() {
        return "/Icons/" + iconPath;
    }
}