package model.Automobile;

public class Car {
    private String licensePlate;
    private Person owner;
    private Brand brand;
    private Model model;
    private int year;

    public Car(String licensePlate, Brand brand, Model model, Person owner, int year) {
        this.licensePlate = licensePlate;
        this.brand = brand;
        this.model = model;
        this.owner = owner;
        this.year = year;
    }

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
        return "License plate: " + licensePlate + ", owner=" + owner + ", brand=" + brand.getName() + ", model=" + model.getName() +
                ", year=" + year;
    }
}