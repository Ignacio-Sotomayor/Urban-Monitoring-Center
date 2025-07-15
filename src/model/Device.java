package model;

public abstract class Device {
    private int ID;
    private String address;
    private State state;
    private Location location;
    //Tambien tiene un tipo de infraccion ??

    public Device() {
    } //como no estamos seguros como lo vamos a usar dejo un constructor vacio por las dudas

    public Device(int ID, String address, State state, Location location) {
        this.ID = ID;
        this.address = address;
        this.state = state;
        this.location = location;
    }

    public State getState(){
        return state;
    }
}