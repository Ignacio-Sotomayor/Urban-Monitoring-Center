package com.model;

import com.model.Fines.EventGeolocation;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class SecurityNotice implements Comparable<SecurityNotice>{
    private String description;
    private EventGeolocation eventGeolocation;
    private Set<Service> calledServices;


    public SecurityNotice(String description, EventGeolocation eventGeolocation){
        this.description = description;
        this.eventGeolocation= eventGeolocation;
        calledServices = new HashSet();
    }

    public SecurityNotice(String description, EventGeolocation eventGeolocation, Set<Service> calledServices){
        this.description = description;
        this.eventGeolocation= eventGeolocation;
        this.calledServices = calledServices;
    }

    public void addCalledService(Service service){
        calledServices.add(service);
    }
    public Iterator getCalledServices(){
        return calledServices.iterator();
    }
    public String getDescription() {
        return description;
    }
    public EventGeolocation getEventGeolocation(){ return eventGeolocation; }

    @Override
    public int compareTo(@NotNull SecurityNotice o) {
        return  this.eventGeolocation.getDateTime().compareTo(((SecurityNotice)o).getEventGeolocation().getDateTime()) ;
    }
}