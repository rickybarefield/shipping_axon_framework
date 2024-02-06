package com.appagility.shipping;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
public class ShippingRestEndpoint {

    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;


    public ShippingRestEndpoint(CommandGateway commandGateway, QueryGateway queryGateway) {

        this.commandGateway = commandGateway;
        this.queryGateway = queryGateway;
    }

    @PostMapping("/ships")
    public CompletableFuture<Void> createShip(@RequestBody CreateShipRequest createShipRequest) {

        String shipId = UUID.randomUUID().toString();
        return commandGateway.send(new CreateShipCommand(shipId, createShipRequest.getName()));
    }

    @GetMapping("/ships")
    public CompletableFuture<List<Ship>> getAllShips() {

        return queryGateway.query(new FindAllShipsQuery(), ResponseTypes.multipleInstancesOf(Ship.class));
    }
}
