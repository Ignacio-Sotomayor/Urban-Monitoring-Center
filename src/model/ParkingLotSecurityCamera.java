package model;

public class ParkingLotSecurityCamera extends Device{ //implementa emisor multa
    private double ToleranceTime;

    public ParkingLotSecurityCamera(int ID, String address, State state, Location location, double toleranceTime) {
        super(ID, address, state, location);
        ToleranceTime = toleranceTime;
    }
}
