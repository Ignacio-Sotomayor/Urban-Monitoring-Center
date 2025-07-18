package model.Fines;

import model.Automobile.Automobile;

public class InfractionType {
    private String description;
    private double amount;
    private int scoring;
    private Automobile automobile;

    public InfractionType(String description, double amount, int scoring) {
        this.description = description;
        this.amount = amount;
        this.scoring = scoring;
    }

    //getters
    public String getDescription() { return description; }
    public double getAmount() { return amount; }
    public int getScoring() { return scoring; }

    //setters
    public void setDescription(String description) { this.description = description; }
    public void setAmount(double amount) { this.amount = amount; }
    public void setScoring(int scoring) { this.scoring = scoring; }

    @Override
    public String toString() {
        return "InfractionType [description=" + description + ", amount=" + amount + ", scoring=" + scoring + "]";
    }
}