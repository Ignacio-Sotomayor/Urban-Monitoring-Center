package model;

public class Radar extends Device implements fineIssuer{
    private double speedLimit;

    public Radar() {
    }

    public Radar(int ID, String address, State state, Location location, double speedLimit) {
        super(ID, address, state, location);
        this.speedLimit = speedLimit;
    }

    public void setSpeedLimit(double speedLimit) {
        this.speedLimit = speedLimit;
    }

    @Override
    public void issueFine() {

    }
}