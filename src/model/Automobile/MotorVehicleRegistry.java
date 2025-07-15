package model.Automobile;
import java.util.*;

public class MotorVehicleRegistry {
    private String name;
    private Set<Brand> brands;
    private List<Car> cars;

    public MotorVehicleRegistry(String name) {
        this.name = name;
        this.brands = new TreeSet<>();
        this.cars = new ArrayList<>();
    }

    public void addBrand(Brand brand) {
        brands.add(brand);
    }

    public void addCar(Car car) {
        cars.add(car);
    }

    public void showAllCars() {
        for (Car car : cars) {
            System.out.println(car);
        }
    }

    public static void main(String[] args) {
        MotorVehicleRegistry registry = new MotorVehicleRegistry("Mar del Plata Car Dealership");

        Brand toyota = new Brand("Toyota");
        Model corolla = new Model("Corolla");
        Model hilux = new Model("Hilux");
        toyota.addModel(corolla);
        toyota.addModel(hilux);

        Brand ford = new Brand("Ford");
        Model focus = new Model("Focus");
        Model ranger = new Model("Ranger");
        ford.addModel(focus);
        ford.addModel(ranger);

        Brand chevrolet = new Brand("Chevrolet");
        Model cruze = new Model("Cruze");
        Model onix = new Model("Onix");
        chevrolet.addModel(cruze);
        chevrolet.addModel(onix);

        registry.addBrand(toyota);
        registry.addBrand(ford);
        registry.addBrand(chevrolet);

        // People
        Person alice = new Person("Alice Johnson", "32145678", "123 Elm Street");
        Person bob = new Person("Bob Smith", "23456789", "456 Oak Avenue");
        Person carol = new Person("Carol White", "34567890", "789 Maple Blvd");

        // Cars
        Car car1 = new Car("ABC123", toyota, corolla, alice, 2020);
        Car car2 = new Car("XYZ789", ford, ranger, bob, 2022);
        Car car3 = new Car("LMN456", chevrolet, onix, carol, 2019);
        Car car4 = new Car("QWE987", toyota, hilux, alice, 2021);

        registry.addCar(car1);
        registry.addCar(car2);
        registry.addCar(car3);
        registry.addCar(car4);

        // Show all cars
        registry.showAllCars();
    }
}