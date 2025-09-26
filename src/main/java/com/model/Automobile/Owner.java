package com.model.Automobile;

public class Owner {
    private String fullName;
    private String legalIid;
    private String address;

    public Owner(String fullName, String legalIid, String address) {
        this.fullName = fullName;
        this.legalIid = legalIid;
        this.address = address;
    }

    //getters
    public String getFullName() {
        return fullName;
    }
    public String getLegalIid() {
        return legalIid;
    }
    public String getAddress() {
        return address;
    }

    public boolean equals(Owner other) {
        return this.legalIid == other.getLegalIid();
    }

    @Override
    public String toString() {
        return fullName + " (ID: " + legalIid + ")";
    }
}