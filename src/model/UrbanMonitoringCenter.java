package model;

import java.util.*;

public class UrbanMonitoringCenter {
    private List<Device> devices;
    private Set<Service> services;
    private Set<SecurityNotice> securityNotices;

    private UrbanMonitoringCenter instance=null;

    private UrbanMonitoringCenter(){
        devices = new ArrayList<>();
        services = new HashSet<>();
        securityNotices = new TreeSet<>();
    }

    public UrbanMonitoringCenter getUrbanMonitoringCenter(){
        if(instance == null)
            instance = new UrbanMonitoringCenter();
        return instance;
    }

    public List<Device> getDevices() {
        return devices;
    }

    public Iterator<Service> getServices() {
        return services.iterator();
    }

    public Iterator<SecurityNotice> getSecurityNotices() {
        return securityNotices.iterator();
    }
}