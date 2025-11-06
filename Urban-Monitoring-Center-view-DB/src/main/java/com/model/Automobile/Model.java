package com.model.Automobile;

public class Model implements Comparable<Model> {
    private final String name;

    public Model(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public int compareTo(Model other) {
        return this.name.compareToIgnoreCase(other.name);
    }

    @Override
    public String toString() {
        return name;
    }
}