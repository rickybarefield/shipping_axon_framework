package com.appagility.shipping.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.Objects;

public class CreateShipCommand {

    private final String shipName;

    public CreateShipCommand(String shipName) {

        this.shipName = shipName;
    }

    public String getShipName() {

        return shipName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateShipCommand that = (CreateShipCommand) o;
        return Objects.equals(shipName, that.shipName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shipName);
    }

    @Override
    public String toString() {
        return "CreateShipCommand{" +
                "shipName='" + shipName + '\'' +
                '}';
    }
}
