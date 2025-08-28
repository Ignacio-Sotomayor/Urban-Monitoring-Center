package com.model.Devices;

import com.model.UrbanMonitoringCenter;

public abstract class Device {
    private static int lastId=0;
    private int id;
    private String address;
    private State state;
    private Location location;

    public Device (String address, Location location){
        this.id = lastId++;
        this.address = address;
        this.state = State.OPERATIVE;
        this.location = location;
    }

    //getters
    public Location getLocation() { return this.location; }
    public String getAddress() { return this.address; }
    public int getId() { return id; }
    public State getState(){ return state; }

    public void fail(){
        this.state = State.INOPERATIVE;
        System.out.format("The device %d stopped working \n",id);
        UrbanMonitoringCenter.getUrbanMonitoringCenter().issuedDevices(this);
    }
}