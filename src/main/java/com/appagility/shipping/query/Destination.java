package com.appagility.shipping.query;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity(name = "Destination")
public class Destination {

    @Id
    private String name;

    private int numberOfSVisits;

    public Destination(String name) {
        this.name = name;
    }

    public Destination() {
    }

    public void addVisit() {

        this.numberOfSVisits++;
    }
}
