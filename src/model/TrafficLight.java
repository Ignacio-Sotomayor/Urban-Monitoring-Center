package model;

public class TrafficLight {
    private String street;
    private String orientation;
    private boolean isMain;

    public TrafficLight(String street, String orientation, boolean isMain) {
        this.street = street;
        this.orientation = orientation;
        this.isMain = isMain;
    }

    public boolean getIsMain(){return isMain;}
    public String getStreet() {
        return street;
    }
    public String getOrientation() {
        return orientation;
    }

    public void changeColor(){}

    public void startIntermittentState(){}
}