package model.Fines;

import model.Automobile.Automobile;

public class ExcessiveSpeedFine extends Fine {
    private int automobileSpeed;
    private int speedLimit;

    public ExcessiveSpeedFine(double amount, int scoring, EventGeolocation eventGeolocation,
                              InfractionType infractionType, Automobile automobile, int automobileSpeed, int speedLimit) {
        super(amount, scoring, eventGeolocation, infractionType, automobile);
        this.automobileSpeed = automobileSpeed;
        this.speedLimit = speedLimit;
    }

    public int getAutomobileSpeed() {
        return automobileSpeed;
    }

    public void setAutomobileSpeed(int automobileSpeed) {
        this.automobileSpeed = automobileSpeed;
    }

    public int getSpeedLimit() {
        return speedLimit;
    }

    public void setSpeedLimit(int speedLimit) {
        this.speedLimit = speedLimit;
    }

    @Override
    public String toString() {
        return super.toString() + ", ExcessiveSpeedFine [automobileSpeed=" + automobileSpeed + ", speedLimit=" + speedLimit + "]";
    }
}