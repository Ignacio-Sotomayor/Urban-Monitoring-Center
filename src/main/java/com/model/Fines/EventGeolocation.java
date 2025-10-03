package com.model.Fines;

import com.model.Devices.Device;
import com.model.Devices.Location;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EventGeolocation {
    private final LocalDateTime dateTime;
    private final String address;
    private final Device device;
    private final Location location;


    public EventGeolocation(LocalDateTime dateTime, String address, Location location, Device device) {
        this.dateTime = dateTime;
        this.address = address;
        this.device = device;
        this.location = location;
    }

    //getters
    public LocalDateTime getDateTime() {
        return this.dateTime;
    }

    public Location getLocation() {
        return location;
    }

    public String getAddress() {
        return address;
    }

    public Device getDevice(){  return this.device; }

    @Override
    public String toString() {
        return String.format("%s \t %s \t %s", dateTime.format(DateTimeFormatter.ISO_DATE_TIME),address,location.toString());
    }
}