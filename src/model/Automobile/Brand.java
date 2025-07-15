package model.Automobile;
import java.util.Set;
import java.util.TreeSet;

public class Brand implements Comparable<Brand> {
    private String name;
    private Set<Model> models;

    public Brand(String name) {
        this.name = name;
        this.models = new TreeSet<>();
    }

    public void addModel(Model model) {
        models.add(model);
    }

    public String getName() {
        return name;
    }

    public Set<Model> getModels() {
        return models;
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