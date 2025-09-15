package com.model.Automobile;

public class Owner {
    private String fullName;
    private String legalId;
    private String address;

    public Owner(String fullName, String legalIid, String address) {
        this.fullName = fullName;
        this.legalId = legalIid;
        this.address = address;
    }

    //getters
    public String getFullName() {
        return fullName;
    }
    public String getLegalId() {
        return legalId;
    }
    public String getAddress() {
        return address;
    }

    public boolean equals(Owner other) {
        return this.legalId == other.getLegalId();
    }

    @Override
    public String toString() {
        return fullName + " (ID: " + legalId + ")";
    }
}