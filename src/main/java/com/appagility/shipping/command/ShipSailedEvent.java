package com.appagility.shipping.command;

import java.util.Objects;

public class ShipSailedEvent {

    private final String shipId;

    private final String source;
    private final String destination;

    public ShipSailedEvent(String shipId, String source, String destination) {
        this.shipId = shipId;
        this.source = source;
        this.destination = destination;
    }

    public String getShipId() {
        return shipId;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ShipSailedEvent that = (ShipSailedEvent) o;

        if (!Objects.equals(shipId, that.shipId)) return false;
        if (!Objects.equals(source, that.source)) return false;
        return Objects.equals(destination, that.destination);
    }

    @Override
    public int hashCode() {
        int result = shipId != null ? shipId.hashCode() : 0;
        result = 31 * result + (source != null ? source.hashCode() : 0);
        result = 31 * result + (destination != null ? destination.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ShipSailedEvent{" +
                "shipId='" + shipId + '\'' +
                ", source='" + source + '\'' +
                ", destination='" + destination + '\'' +
                '}';
    }
}
