package com.model.Fines;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import com.model.Automobile.Automobile;

public class Fine implements Serializable {
    private double amount;
    private int scoring;
    private Set<Photo> photos; 
    private EventGeolocation eventGeolocation;
    private InfractionType infractionType;
    private Automobile automobile;

    public Fine( EventGeolocation eventGeolocation, InfractionType infractionType,Automobile automobile) {
        this.infractionType = infractionType;
        this.amount = infractionType.getAmount();
        this.scoring = infractionType.getScoring();
        this.photos = new HashSet<>();
        this.eventGeolocation = eventGeolocation;
        this.automobile=automobile;
    }

    //getters
    public Set<Photo> getPhotos() { return photos;}
    public double getAmount() {
        return amount;
    }
    public int getScoring() {
        return scoring;
    }
    public EventGeolocation getEventGeolocation() {return eventGeolocation;}
    public InfractionType getInfractionType() {
        return infractionType;
    }
    public Automobile getAutomobile(){ return automobile;}

    //setters
    public void setAmount(double amount) {
        this.amount = amount;
    }
    public void setScoring(int scoring) {
        this.scoring = scoring;
    }
    public void setEventGeolocation(EventGeolocation eventGeolocation) {
        this.eventGeolocation = eventGeolocation;
    }
    public void setInfractionType(InfractionType infractionType) {this.infractionType = infractionType;}
    public void setAutomobile(Automobile automobile){ this.automobile = automobile;}

    public void addPhoto(Photo photo) {
        photos.add(photo);
    }


    @Override
    public String toString() {
        return String.format(" %s \t Amount: %f \t Scoring:%d",eventGeolocation.toString(),amount,scoring);
    }
}