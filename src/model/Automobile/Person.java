package model.Automobile;

import model.SecurityNotice;

public class Person {
    private String fullName;
    private String id;
    private String address;

    public Person(String fullName, String id, String address) {
        this.fullName = fullName;
        this.id = id;
        this.address = address;
    }

    //getters
    public String getFullName() {
        return fullName;
    }
    public String getId() {
        return id;
    }
    public String getAddress() {
        return address;
    }

    public boolean equals(Person other) {
        return this.id == other.getId();
    }

    @Override
    public String toString() {
        return fullName + " (ID: " + id + ")";
    }
}