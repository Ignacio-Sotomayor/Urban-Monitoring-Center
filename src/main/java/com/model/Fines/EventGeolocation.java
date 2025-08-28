package com.model.Fines;

import com.model.Devices.Device;
import com.model.Devices.Location;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EventGeolocation {
    private int id;
    private LocalDateTime dateHour;
    private String address;
    private Device device;
    private Location location;
    private static int lastID = -1;


    public EventGeolocation( LocalDateTime dateHour, String address, Location location, Device device) {
        this.id = ++lastID;
        this.dateHour = dateHour;
        this.address = address;
        this.device = device;
        this.location = location;
    }

    //getters
    public int getId() {return id;}
    public LocalDateTime getDateHour() {
  return dateHour;
 }
    public void setDateHour(LocalDateTime dateHour) {
  this.dateHour = dateHour;
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
        return String.format("%s %s",dateHour.format(DateTimeFormatter.ISO_DATE_TIME),address);
    }
}