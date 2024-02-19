package com.appagility.shipping;

public class DockShipRequest {

    private String shipId;
    private String location;

    public DockShipRequest() {
    }

    public DockShipRequest(String shipId, String location) {

        this.shipId = shipId;
        this.location = location;
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
