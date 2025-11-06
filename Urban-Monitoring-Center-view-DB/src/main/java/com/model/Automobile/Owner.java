package com.model.Automobile;

public class Owner {
    private String legalId;
    private String fullName;
    private String address;

    public Owner( String legalIid, String fullName, String address) {
        this.legalId = legalIid;
        this.fullName = fullName;
        this.address = address;
    }

    //getters
    public String getFullName() {
        return fullName;
    }
    public String getLegalIid() {
        return legalId;
    }
    public String getAddress() {
        return address;
    }

    public boolean equals(Owner other) {
        return this.legalId == other.getLegalIid();
    }

    @Override
    public String toString() {
        return fullName + " (ID: " + legalId + ")";
    }
}