package com.model.Fines;

import com.model.Devices.Device;
import com.model.Devices.Location;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EventGeolocation {
    private LocalDateTime dateTime;
    private String address;
    private Device device;
    private Location location;


    public EventGeolocation(LocalDateTime dateTime, String address, Location location, Device device) {
        this.dateTime = dateTime;
        this.address = address;
        this.device = device;
        this.location = location;
    }

    //getters
    public LocalDateTime getDateTime() {
  return dateTime;
 }
    public void setDateTime(LocalDateTime dateTime) {
  this.dateTime = dateTime;
 }
    public String getAddress() {
  return address;
 }

    //setters
    public void setAddress(String address) {
  this.address = address;
 }

    @Override
    public String toString() {
        return String.format("%s \t %s \t %s", dateTime.format(DateTimeFormatter.ISO_DATE_TIME),address,location.toString());
    }
}