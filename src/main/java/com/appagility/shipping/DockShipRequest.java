package com.appagility.shipping;

public class DockShipRequest {

    private String shipId;
    private String location;

    public DockShipRequest() {
    }

    public String getShipId() {
        return shipId;
    }

    public void setShipId(String shipId) {
        this.shipId = shipId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
