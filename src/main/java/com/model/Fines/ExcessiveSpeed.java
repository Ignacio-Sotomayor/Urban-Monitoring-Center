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


    public Object[] calculateFineForSpeed(double automobileSpeed, double speedLimit) {
        BigDecimal amount = getAmount();
        int score = getScoring();
        int excessSteps = (int)((automobileSpeed - speedLimit) / (speedLimit * 0.1));

        for (int i = 0; i < excessSteps; i++) {
            amount = amount.add(surchargePer10PercentExcess);
            score += 1;
        }

        return new Object[]{amount, score};
    }


    @Override
    public String toString() {
        return super.toString() + ", ExcessiveSpeed [surchargePer10PercentExcess=" + surchargePer10PercentExcess + "]";
    }
}