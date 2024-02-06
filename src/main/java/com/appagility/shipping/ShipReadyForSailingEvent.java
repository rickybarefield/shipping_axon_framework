package com.appagility.shipping;

import java.util.Objects;

public class ShipReadyForSailingEvent {

    private final String shipId;

    public ShipReadyForSailingEvent(String shipId) {
        this.shipId = shipId;
    }

    public String getShipId() {
        return shipId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ShipReadyForSailingEvent that = (ShipReadyForSailingEvent) o;

        return Objects.equals(shipId, that.shipId);
    }

    @Override
    public int hashCode() {
        return shipId != null ? shipId.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ShipReadyForSailingEvent{" +
                "shipId='" + shipId + '\'' +
                '}';
    }
}
