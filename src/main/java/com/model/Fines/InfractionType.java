package com.model.Fines;

import com.model.Automobile.Automobile;

public class InfractionType {
    private String name;
    private String description;
    private double amount;
    private int scoring;

    public InfractionType(String name,String description, double amount, int scoring) {
        this.name = name;
        this.description = description;
        this.amount = amount;
        this.scoring = scoring;
    }

    //getters
    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getAmount() { return amount; }
    public int getScoring() { return scoring; }

    //setters
    public void setDescription(String description) { this.description = description; }
    public void setAmount(double amount) { this.amount = amount; }
    public void setScoring(int scoring) { this.scoring = scoring; }

}