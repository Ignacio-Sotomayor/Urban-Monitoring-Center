package com.model.Automobile;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class Brand implements Comparable<Brand> {
    private final String name;
    private final Set<Model> models;

    public Brand(String name) {
        this.name = name;
        this.models = new TreeSet<>();
    }

    public void addModel(Model model) {
        models.add(model);
    }

    //getters
    public String getName() {
        return name;
    }
    public Iterator getModels() {
        return models.iterator();
    }

    @Override
    public int compareTo(Brand other) {
        return this.name.compareToIgnoreCase(other.name);
    }

    @Override
    public String toString() {
        return "Brand: " + name + "', models=" + models;
    }
}