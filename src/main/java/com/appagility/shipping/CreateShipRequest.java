package com.appagility.shipping;

public class CreateShipRequest {

    private String name;

    public CreateShipRequest() {
    }

    public CreateShipRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
