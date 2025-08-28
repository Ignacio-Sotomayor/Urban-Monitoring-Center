package com.model;

import com.model.Fines.EventGeolocation;
import org.jetbrains.annotations.NotNull;

public class SecurityNotice implements Comparable<SecurityNotice>{
    private String description;
    private EventGeolocation eventGeolocation;


    public SecurityNotice(String description, EventGeolocation eventGeolocation){
        this.description = description;
        this.eventGeolocation= eventGeolocation;
    }

    public String getDescription() {
        return description;
    }
    public EventGeolocation getEventGeolocation(){ return eventGeolocation; }

    @Override
    public int compareTo(@NotNull SecurityNotice o) {
        return  this.eventGeolocation.getDateHour().compareTo(((SecurityNotice)o).getEventGeolocation().getDateHour()) ;
    }
}