package com.example.getawaydrives.entities;

public enum DocumentType {
    DL(1, "Drivers License"),
    INSURANCE(2, "Insurance"),
    VEHICLE_REGISTRATION(3, "Vehicle Registration"),
    VEHICLE_PHOTOS(4, "Vehicle Photos");

    private int id;
    private String name;

    DocumentType(int id, String name) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}
