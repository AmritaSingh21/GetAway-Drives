package com.example.getawaydrives.entities;

public enum TransmissionType {
    MANUAL(1, "Manual"),
    AUTOMATIC(2, "Automatic");

    private int id;
    private String name;

    TransmissionType(int id, String name) {
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
