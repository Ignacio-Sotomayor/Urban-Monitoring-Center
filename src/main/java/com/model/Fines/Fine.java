package com.model.Fines;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import com.model.Automobile.Automobile;

public class Fine {
    private Integer fine_Id;
    private final BigDecimal amount;
    private final int scoring;
    private final Set<Photo> photos;
    private final EventGeolocation eventGeolocation;
    private final InfractionType infractionType;
    private final Automobile automobile;

    public Fine(Integer fine_Id, EventGeolocation eventGeolocation, InfractionType infractionType,Automobile automobile,Set<Photo> photos) {
        this.fine_Id = fine_Id;
        this.infractionType = infractionType;
        this.amount = infractionType.getAmount();
        this.scoring = infractionType.getScoring();
        this.photos = photos;
        this.eventGeolocation = eventGeolocation;
        this.automobile=automobile;
    }
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
    public BigDecimal getAmount() {
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
    public Integer getFine_Id() {
        return fine_Id;
    }

    public void addPhoto(Photo photo) {
        photos.add(photo);
    }


    @Override
    public String toString() {
        return String.format(" %s \t Amount: %f \t Scoring:%d",eventGeolocation.toString(),amount,scoring);
    }
}