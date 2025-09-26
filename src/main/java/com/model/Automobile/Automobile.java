package com.model.Automobile;

public class Automobile implements Comparable<Automobile>{
    private final String licensePlate;
    private Owner owner;
    private final Brand brand;
    private final Model model;
    private final int year;

    public Automobile(String licensePlate, Brand brand, Model model, Owner owner, int year) {
        this.licensePlate = licensePlate;
        this.brand = brand;
        this.model = model;
        this.owner = owner;
        this.year = year;
    }

    //getters
    public int getYear() {
        return year;
    }
    public String getLicensePlate() {
        return licensePlate;
    }
    public Owner getOwner() {
        return owner;
    }
    public Brand getBrand() {
        return brand;
    }
    public Model getModel() {
        return model;
    }

    //setter
    public void setOwner(Owner owner) { this.owner = owner; }

    @Override
    public String toString() {
        return String.format("%s model %s %d of %s with the license plate: %s", brand.getName(),model.toString(),year,owner.getFullName(),licensePlate);
    }

    public boolean equals(Automobile other){
        return this.licensePlate.equalsIgnoreCase(other.getLicensePlate());
    }

    public int compareTo(Automobile other){
        return this.licensePlate.compareTo(other.licensePlate);
    }
}