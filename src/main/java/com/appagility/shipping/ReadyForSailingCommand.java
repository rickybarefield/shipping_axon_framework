package com.appagility.shipping;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.Objects;

public class ReadyForSailingCommand {

    @TargetAggregateIdentifier
    private final String shipId;


    public ReadyForSailingCommand(String shipId) {

        this.shipId = shipId;
    }

    public String getShipId() {
        return shipId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReadyForSailingCommand that = (ReadyForSailingCommand) o;

        return Objects.equals(shipId, that.shipId);
    }

    @Override
    public int hashCode() {
        return shipId != null ? shipId.hashCode() : 0;
    }

    @Override
    public String toString() {

        return "ReadyForSailingCommand{" +
                "shipId='" + shipId + '\'' +
                '}';
    }
}
