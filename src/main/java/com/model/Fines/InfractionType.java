package com.model.Fines;

import java.math.BigDecimal;

public class InfractionType {
    private String name;
    private String description;
    private BigDecimal amount;
    private int scoring;

    public InfractionType(String name,String description, BigDecimal amount, int scoring) {
        this.name = name;
        this.description = description;
        this.amount = amount;
        this.scoring = scoring;
    }

    //getters
    public String getName() { return name; }
    public String getDescription() { return description; }
    public BigDecimal getAmount() { return amount; }
    public int getScoring() { return scoring; }

    //setters
    public void setDescription(String description) { this.description = description; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public void setScoring(int scoring) { this.scoring = scoring; }

}