package model.Fines;

public abstract InfractionType {
 private String description;
 private double amount;
 private int scoring;

 public InfractionType(String description, double amount, int scoring) {
  this.description = description;
  this.amount = amount;
  this.scoring = scoring;
 }

 public String getDescription() {
  return description;
 }

 public void setDescription(String description) {
  this.description = description;
 }

 public double getAmount() {
  return amount;
 }

 public void setAmount(double amount) {
  this.amount = amount;
 }

 public int getScoring() {
  return scoring;
 }

 public void setScoring(int scoring) {
  this.scoring = scoring;
 }

 @Override
 public String toString() {
  return "InfractionType [description=" + description + ", amount=" + amount + ", scoring=" + scoring + "]";
 }
}