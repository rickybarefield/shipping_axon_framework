package com.appagility.shipping;

public class ShipReadierRequest {

    private String shipId;

    public ShipReadierRequest() {

    }


    public ShipReadierRequest(String shipId) {

        this.shipId = shipId;
    }

    public String getShipId() {
        return shipId;
    }

    public void setShipId(String shipId) {
        this.shipId = shipId;
    }
}
