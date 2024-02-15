package com.appagility.shipping;

public class SailShipRequest {

    private String shipId;
    private String destination;

    public SailShipRequest() {
    }

    public String getShipId() {
        return shipId;
    }

    public void setShipId(String shipId) {
        this.shipId = shipId;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}
