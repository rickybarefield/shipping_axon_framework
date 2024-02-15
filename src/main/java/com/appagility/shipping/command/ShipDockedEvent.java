package com.appagility.shipping.command;

import java.util.Objects;

public class ShipDockedEvent {

    private final String shipId;
    private final String location;

    public ShipDockedEvent(String shipId, String location) {
        this.shipId = shipId;
        this.location = location;
    }

    public String getShipId() {
        return shipId;
    }

    public String getLocation() {
        return location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ShipDockedEvent that = (ShipDockedEvent) o;

        if (!Objects.equals(shipId, that.shipId)) return false;
        return Objects.equals(location, that.location);
    }

    @Override
    public int hashCode() {
        int result = shipId != null ? shipId.hashCode() : 0;
        result = 31 * result + (location != null ? location.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ShipDockedEvent{" +
                "shipId='" + shipId + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
