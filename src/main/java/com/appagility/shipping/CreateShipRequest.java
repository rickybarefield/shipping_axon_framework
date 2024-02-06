package com.appagility.shipping;

import com.fasterxml.jackson.annotation.JsonCreator;

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
