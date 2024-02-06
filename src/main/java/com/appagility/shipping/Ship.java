package com.appagility.shipping;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Ship {

    @Id
    private String shipId;
    private String shipName;

    private boolean isReadyForSailing;

    public Ship() {
    }

    public Ship(String shipId, String shipName) {

        this.shipId = shipId;
        this.shipName = shipName;
        this.isReadyForSailing = false;
    }

    public String getShipId() {
        return shipId;
    }

    public void setShipId(String shipId) {
        this.shipId = shipId;
    }

    public String getShipName() {
        return shipName;
    }

    public void setShipName(String shipName) {
        this.shipName = shipName;
    }


    public boolean isReadyForSailing() {
        return isReadyForSailing;
    }

    public void setReadyForSailing(boolean readyForSailing) {
        isReadyForSailing = readyForSailing;
    }
}
