package model.Automobile;
import model.Fines.*;

import java.util.*;

public class MotorVehicleRegistry {
    private String name;
    private Set<Brand> brands;
    private List<Automobile> automobiles;
    private Map<String,List<Fine>> finesMap;

    public MotorVehicleRegistry(String name) {
        this.name = name;
        this.brands = new TreeSet<>();
        this.automobiles = new ArrayList<>();
        this.finesMap = new TreeMap<String,List<Fine>>();
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

        // Automobiles
        Automobile car1 = new Automobile("ABC123", toyota, corolla, alice, 2020);
        Automobile car2 = new Automobile("XYZ789", ford, ranger, bob, 2022);
        Automobile car3 = new Automobile("LMN456", chevrolet, onix, carol, 2019);
        Automobile car4 = new Automobile("QWE987", toyota, hilux, alice, 2021);

        registry.addAutomobile(car1);
        registry.addAutomobile(car2);
        registry.addAutomobile(car3);
        registry.addAutomobile(car4);

        ExcessiveSpeedFine f1 = new ExcessiveSpeedFine(1000, 1,
                new EventGeolocation("A001", "2025/05/06 08:23",
                "Ituzaingo 1320"),
                new ExcessiveSpeed("Excessive speed", 1000,1,100),
                car1,70,60);
        ExcessiveSpeedFine f2 = new ExcessiveSpeedFine(3000, 1,
                new EventGeolocation("A001", "2025/05/06 08:23",
                        "Ituzaingo 1320"),
                new ExcessiveSpeed("Excessive speed", 1000,1,100),
                car1,80,60);

        registry.addFineToAutomobil(car1.getLicensePlate(),f1);
        registry.addFineToAutomobil(car1.getLicensePlate(),f2);


        // Show all Automobiles
        registry.showAllAutomobiles();
        //Show the Scoring of a particular automobile
        System.out.println("The scoring of the car with license Plate: "+car1.getLicensePlate()+", is: "+registry.getScoring(car1.getLicensePlate()));
    }

    public void addBrand(Brand brand) {
        brands.add(brand);
    }


    public void addAutomobile(Automobile a) {
        automobiles.add(a);
    }


    public void showAllAutomobiles() {
        for (Automobile a : automobiles) {
            System.out.println(a);
        }
    }

    public void addFineToAutomobil(String licensePlate,Fine f) {
         List<Fine> fines = finesMap.get(licensePlate);
         if (fines == null) {
             fines = new ArrayList<>();
             finesMap.put(licensePlate,fines);
         }
         fines.add(f);
    }

    public int getScoring(String licensePlate) {
        int scoring = 20;
        List<Fine> fines = finesMap.get(licensePlate);

        if (fines != null) {
            for (Fine f : fines) {
                scoring-=f.getScoring();
            }
        }
        return scoring;
    }
}