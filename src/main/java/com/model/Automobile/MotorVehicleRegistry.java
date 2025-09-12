package com.model.Automobile;
import com.model.Fines.*;

import java.util.*;

public class MotorVehicleRegistry {
    private static String name = "Mar del Plata Car Dealership";
    private Set<Brand> brands;
    private Map<Automobile,List<Fine>> automobilesInformation;
    private static MotorVehicleRegistry instance = null;

    private MotorVehicleRegistry(){
        this.brands = new TreeSet<>();
        this.automobilesInformation = new TreeMap<Automobile,List<Fine>>();
    }

    public static MotorVehicleRegistry getMotorVehicleRegistry(){
        if(instance == null)
            instance = new MotorVehicleRegistry();
        return instance;
    }

    public static void Initialize() {
        MotorVehicleRegistry registry = getMotorVehicleRegistry();

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
        Owner alice = new Owner("Alice Johnson", "32145678", "123 Elm Street");
        Owner bob = new Owner("Bob Smith", "23456789", "456 Oak Avenue");
        Owner carol = new Owner("Carol White", "34567890", "789 Maple Blvd");

        // Automobiles
        Automobile car1 = new Automobile("ABC123", toyota, corolla, alice, 2020);
        Automobile car2 = new Automobile("XYZ789", ford, ranger, bob, 2022);
        Automobile car3 = new Automobile("LMN456", chevrolet, onix, carol, 2019);
        Automobile car4 = new Automobile("QWE987", toyota, hilux, alice, 2021);

        registry.addAutomobile(car1);
        registry.addAutomobile(car2);
        registry.addAutomobile(car3);
        registry.addAutomobile(car4);
    }

    public Automobile getRandomAutomobile(){
        Random random = new Random();
        ArrayList<Automobile> automobiles = new ArrayList<>(automobilesInformation.keySet());
        return automobiles.get(random.nextInt(automobiles.size()));
    }

    public void addBrand(Brand brand) { brands.add(brand); }
    public void addAutomobile(Automobile a) { automobilesInformation.put(a,new ArrayList<>()); }
    public void addFineToAutomobile(Automobile a, Fine f) { automobilesInformation.get(a).add(f); }

    public void showAllAutomobiles() {
        for (Automobile a : automobilesInformation.keySet()) {
            System.out.println(a);
        }
    }

    public int getScoring(Automobile a) {
        int scoring = 20;
        List<Fine> fines = automobilesInformation.get(a);
        for (Fine f : fines) {
            scoring-=f.getScoring();
        }
        return scoring;
    }

    public void informFines() {
        Iterator<Automobile> it = automobilesInformation.keySet().iterator();
        while(it.hasNext()) {
            Automobile actualAutomobile = it.next();
            System.out.format("THE %s has the next fines: \n",actualAutomobile.toString() );
            for(Fine f : automobilesInformation.get(actualAutomobile)){
                System.out.format("\t %s \n", f.toString());
            }
            System.out.println('\n');
        }
    }
}