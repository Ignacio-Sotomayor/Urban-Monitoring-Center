package com.model.Devices;

import com.model.UrbanMonitoringCenter;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public abstract class Device implements Serializable {
    private UUID id;
    private String address;
    private boolean state;
    private Location location;

    public Device (String address, Location location,boolean state ){
        this.id = UUID.randomUUID();
        this.address = address;
        this.state = state;
        this.location = location;
    }

    public Device(String ID, String address, boolean state, Location location){
        this.id = UUID.fromString(ID);
        this.address = address;
        this.state = state;
        this.location = location;
    }

    //getters
    public Location getLocation() { return this.location; }
    public String getAddress() { return this.address; }
    public UUID getId() { return this.id; }
    public boolean getState(){ return state; }

    public void fail(){
        this.state = false;
        System.out.format("The device %d stopped working \n",id);
        UrbanMonitoringCenter.getUrbanMonitoringCenter().issuedDevices(this);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Device device = (Device) o;
        return Objects.equals(id, device.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public String stateToString(boolean state){
        if (state){
            return "Operative";
        }
        else {
            return "Inoperative";
        }
    }
    @Override
    public String toString() {
        return "id: " + id + ", Device Type: "+ getClass().getSimpleName()+ "address: " + address + ", state: " + stateToString(state) + ", location: " + location;
    }

}