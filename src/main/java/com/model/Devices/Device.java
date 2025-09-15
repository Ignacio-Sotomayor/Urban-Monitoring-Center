package com.model.Devices;

import com.model.UrbanMonitoringCenter;

import java.util.Objects;
import java.util.UUID;

public abstract class Device {
    private UUID id;
    private String address;
    private DeviceState state;
    private Location location;

    public Device (String address, Location location){
        this.id = UUID.randomUUID();
        this.address = address;
        this.state = DeviceState.OPERATIVE;
        this.location = location;
    }

    public Device(String ID, String address, DeviceState state, Location location){
        this.id = UUID.fromString(ID);
        this.address = address;
        this.state = state;
        this.location = location;
    }

    //getters
    public Location getLocation() { return this.location; }
    public String getAddress() { return this.address; }
    public String getId() { return id.toString(); }
    public DeviceState getState(){ return state; }

    public void fail(){
        this.state = DeviceState.INOPERATIVE;
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
}