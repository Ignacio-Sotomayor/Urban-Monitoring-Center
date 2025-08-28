package com.model.Fines;

public class ExcessiveSpeed extends InfractionType {
    private double surchargePer10PercentExcess;

    public ExcessiveSpeed(String description, double amount, int scoring,double surchargePer10PercentExcess) {
        super(description, amount, scoring);
        this.surchargePer10PercentExcess = surchargePer10PercentExcess;
    }

    public double getSurchargePer10PercentExcess() {
        return surchargePer10PercentExcess;
    }

    public void setSurchargePer10PercentExcess(double surchargePer10PercentExcess) {
        this.surchargePer10PercentExcess = surchargePer10PercentExcess;
    }

    public void calculateImportScoring(double automobileSpeed, double speedLimit){
        double imp = getAmount();
        int score = getScoring(), Excess = (int)((automobileSpeed - speedLimit)/(speedLimit * 0.1));

        for(int i=0; i<Excess; i++){
          imp += this.surchargePer10PercentExcess;
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