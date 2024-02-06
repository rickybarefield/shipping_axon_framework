package com.appagility.shipping;

import java.util.Objects;

public class Ship {

    private final String shipId;
    private String shipName;

    public Ship(String shipId, String shipName) {

        this.shipId = shipId;
        this.shipName = shipName;
    }

    public String getShipId() {
        return shipId;
    }

    public String getShipName() {
        return shipName;
    }

    public void setShipName(String shipName) {
        this.shipName = shipName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ship ship = (Ship) o;

        if (!Objects.equals(shipId, ship.shipId)) return false;
        return Objects.equals(shipName, ship.shipName);
    }

    @Override
    public int hashCode() {
        int result = shipId != null ? shipId.hashCode() : 0;
        result = 31 * result + (shipName != null ? shipName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Ship{" +
                "shipId='" + shipId + '\'' +
                ", shipName='" + shipName + '\'' +
                '}';
    }
}
