package model;

public class SecurityCamera extends Device{

    public SecurityCamera() {
        super();
    }

    public SecurityCamera(int ID, String address, State state, Location location) {
        super(ID, address, state, location);
    }
}