package com.example.getawaydrives.entities;

public enum RentStatus {
    ACTIVE(1, "Active"),
    RETURN(2, "Returned");

    private int id;
    private String name;

    RentStatus(int id, String name) {
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
