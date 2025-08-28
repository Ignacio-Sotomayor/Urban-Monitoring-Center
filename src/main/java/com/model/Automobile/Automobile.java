package com.model.Automobile;

public class Automobile implements Comparable<Automobile>{
    private String licensePlate;
    private Person owner;
    private Brand brand;
    private Model model;
    private int year;

    public Automobile(String licensePlate, Brand brand, Model model, Person owner, int year) {
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
    public Person getOwner() {
        return owner;
    }
    public Brand getBrand() {
        return brand;
    }
    public Model getModel() {
        return model;
    }

    @Override
    public String toString() {
        return String.format("%s model %s %d of %s with the license plate: %s",brand,model,year,owner.getFullName(),licensePlate);
    }

    public boolean equals(Automobile other){
        return this.licensePlate.equalsIgnoreCase(other.getLicensePlate());
    }

    public int compareTo(Automobile other){
        return this.licensePlate.compareTo(((Automobile)other).licensePlate);
    }
}