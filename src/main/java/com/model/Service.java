package com.model;

public enum Service {
    Police("911"),
    FireFighters("100"),
    Ambulance("107"),
    CivilDefense("105");

    private final String phoneNumber;

    Service(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public String getPhoneNumber(){
        return this.phoneNumber;
    }
    public static Service getService(String number){
        for(Service s : Service.values()){
            if(s.getPhoneNumber().equalsIgnoreCase(number))
                return s;
        }
        return null;
    }
    public String getName(){ return this.name();}
}