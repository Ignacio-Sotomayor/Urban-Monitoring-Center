package model.Fines;

public class ExcessiveSpeed extends InfractionType {
    private double surchargePer10PercentExcess;

    public ExcessiveSpeed(String description, double amount, int scoring, double surchargePer10PercentExcess) {
        super(description, amount, scoring);
        this.surchargePer10PercentExcess = surchargePer10PercentExcess;
    }

    public double getSurchargePer10PercentExcess() {
        return surchargePer10PercentExcess;
    }

    public void setSurchargePer10PercentExcess(double surchargePer10PercentExcess) {
        this.surchargePer10PercentExcess = surchargePer10PercentExcess;
    }

    @Override
    public String toString() {
        return super.toString() + ", ExcessiveSpeed [surchargePer10PercentExcess=" + surchargePer10PercentExcess + "]";
    }
}