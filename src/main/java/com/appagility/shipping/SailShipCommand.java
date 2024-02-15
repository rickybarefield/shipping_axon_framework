package com.appagility.shipping;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.Objects;

public class SailShipCommand {

    @TargetAggregateIdentifier
    private final String shipId;

    private final String destination;

    public SailShipCommand(String shipId, String destination) {
        this.shipId = shipId;
        this.destination = destination;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SailShipCommand that = (SailShipCommand) o;

        if (!Objects.equals(shipId, that.shipId)) return false;
        return Objects.equals(destination, that.destination);
    }

    @Override
    public int hashCode() {
        int result = shipId != null ? shipId.hashCode() : 0;
        result = 31 * result + (destination != null ? destination.hashCode() : 0);
        return result;
    }

    public String getShipId() {
        return shipId;
    }

    public String getDestination() {
        return destination;
    }

    @Override
    public String toString() {
        return "SailShipCommand{" +
                "shipId='" + shipId + '\'' +
                ", destination='" + destination + '\'' +
                '}';
    }
}
