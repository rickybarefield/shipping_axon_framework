package com.appagility.shipping;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.Objects;

public class CreateShipCommand {

    @TargetAggregateIdentifier
    private final String shipId;
    private final String shipName;

    public CreateShipCommand(String shipId, String shipName) {

        this.shipId = shipId;
        this.shipName = shipName;
    }

    public String getShipId() {

        return shipId;
    }

    public String getShipName() {

        return shipName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CreateShipCommand that = (CreateShipCommand) o;

        if (!Objects.equals(shipId, that.shipId)) return false;
        return Objects.equals(shipName, that.shipName);
    }

    @Override
    public int hashCode() {
        int result = shipId != null ? shipId.hashCode() : 0;
        result = 31 * result + (shipName != null ? shipName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {

        return "CreateShipCommand{" +
                "shipId='" + shipId + '\'' +
                ", shipName='" + shipName + '\'' +
                '}';
    }
}
