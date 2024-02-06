package com.appagility.shipping;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/shipReadier")
    public CompletableFuture<Void> readyShip(@RequestBody ShipReadierRequest shipReadierRequest) {

        return commandGateway.send(new ReadyForSailingCommand(shipReadierRequest.getShipId()));
    }

    @GetMapping("/ships")
    public CompletableFuture<List<Ship>> getAllShips(@RequestParam(name = "onlyReadyForSailing", required = false) Boolean onlyReadyForSailing) {

        var command = Boolean.TRUE.equals(onlyReadyForSailing)
                ? new FindAllShipsReadyForSailingQuery()
                : new FindAllShipsQuery();

        return queryGateway.query(command, ResponseTypes.multipleInstancesOf(Ship.class));
    }
}
