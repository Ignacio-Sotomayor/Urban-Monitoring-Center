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


    public void calculateImportScoring(double automobileSpeed, double speedLimit){
        BigDecimal imp = getAmount();
        int score = getScoring();
        int Excess = (int)((automobileSpeed - speedLimit)/(speedLimit * 0.1));

        for(int i=0; i<Excess; i++){
            imp.add(surchargePer10PercentExcess);
            score += 1;
        }

        setAmount(imp);
        setScoring(score);
    }

    @Override
    public String toString() {
        return super.toString() + ", ExcessiveSpeed [surchargePer10PercentExcess=" + surchargePer10PercentExcess + "]";
    }
}