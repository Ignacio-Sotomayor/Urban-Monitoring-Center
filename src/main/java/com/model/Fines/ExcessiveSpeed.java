package com.model.Fines;

import java.math.BigDecimal;

public class ExcessiveSpeed extends InfractionType {
    private final BigDecimal surchargePer10PercentExcess;

    public ExcessiveSpeed(String description, BigDecimal amount, int scoring,BigDecimal surchargePer10PercentExcess) {
        super("Speeding",description, amount, scoring);
        this.surchargePer10PercentExcess = surchargePer10PercentExcess;
    }

    public BigDecimal getSurchargePer10PercentExcess() {
        return surchargePer10PercentExcess;
    }

    @Override
    public String toString() {
        return super.toString() + ", ExcessiveSpeed [surchargePer10PercentExcess=" + surchargePer10PercentExcess + "]";
    }
}