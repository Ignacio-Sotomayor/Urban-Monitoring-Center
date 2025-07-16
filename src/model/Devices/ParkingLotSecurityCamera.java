package model;

public class ParkingLotSecurityCamera extends Device implements fineIssuer{
    private double ToleranceTime;

    public ParkingLotSecurityCamera(int ID, String address, State state, Location location, double toleranceTime) {
        super(ID, address, state, location);
        ToleranceTime = toleranceTime;
    }

    @Override
    public void issueFine() {

    }
}