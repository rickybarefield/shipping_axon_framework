package com.appagility.shipping.query;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.Objects;
import java.util.UUID;

@Entity(name = "Sailing")
public class Sailing {

    @Id
    private String id;

    private String shipId;

    private String source;

    private String destination;

    private String statedDestination;

    public Sailing(String shipId, String source, String destination, String statedDestination) {

        this.id = UUID.randomUUID().toString();
        this.shipId = shipId;
        this.source = source;
        this.destination = destination;
        this.statedDestination = statedDestination;
    }

    public Sailing() {
    }

    public String getShipId() {
        return shipId;
    }

    public void setShipId(String shipId) {
        this.shipId = shipId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getStatedDestination() {
        return statedDestination;
    }

    public void setStatedDestination(String statedDestination) {
        this.statedDestination = statedDestination;
    }

    public boolean arrivedInUnintendedDestination() {

        return destination != null && !destination.equals(statedDestination);
    }

    @Override
    public String toString() {
        return "Sailing{" +
                "id='" + id + '\'' +
                ", shipId='" + shipId + '\'' +
                ", source='" + source + '\'' +
                ", destination='" + destination + '\'' +
                ", statedDestination='" + statedDestination + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sailing sailing = (Sailing) o;
        return Objects.equals(id, sailing.id) && Objects.equals(shipId, sailing.shipId) && Objects.equals(source, sailing.source) && Objects.equals(destination, sailing.destination) && Objects.equals(statedDestination, sailing.statedDestination);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, shipId, source, destination, statedDestination);
    }
}
