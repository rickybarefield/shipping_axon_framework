package com.appagility.shipping;

import com.appagility.shipping.command.CreateShipCommand;
import com.appagility.shipping.command.DockShipCommand;
import com.appagility.shipping.command.ReadyForSailingCommand;
import com.appagility.shipping.command.SailShipCommand;
import com.appagility.shipping.query.FindAllSailingsQuery;
import com.appagility.shipping.query.FindAllShipsQuery;
import com.appagility.shipping.query.Sailing;
import com.appagility.shipping.query.Ship;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    @ResponseBody
    public CompletableFuture<Ship> createShip(@RequestBody CreateShipRequest createShipRequest) {

        CompletableFuture<String> result =  commandGateway.send(new CreateShipCommand(createShipRequest.getName()));
        return result.thenApply(r -> new Ship(r, createShipRequest.getName()));
    }

    @PostMapping("/shipReadiers")
    public CompletableFuture<Void> readyShip(@RequestBody ShipReadierRequest shipReadierRequest) {

        return commandGateway.send(new ReadyForSailingCommand(shipReadierRequest.getShipId()));
    }

    @GetMapping("/ships")
    public CompletableFuture<List<Ship>> getAllShips(@RequestParam(name = "onlyReadyForSailing", required = false) Boolean onlyReadyForSailing) {

        var query = new FindAllShipsQuery(Boolean.TRUE.equals(onlyReadyForSailing));

        return queryGateway.query(query, ResponseTypes.multipleInstancesOf(Ship.class));
    }

    @GetMapping("/sailings")
    public CompletableFuture<List<Sailing>> getSailings(@RequestParam(name = "onlyUnintendedDestinations", required = false) Boolean onlyUnintendedDestinations) {

        return queryGateway.query(new FindAllSailingsQuery(Boolean.TRUE.equals(onlyUnintendedDestinations)), ResponseTypes.multipleInstancesOf(Sailing.class));
    }

    @PostMapping("/sailings")
    public CompletableFuture<Void> createSailing(@RequestBody SailShipRequest sailShipRequest) {

        return commandGateway.send(new SailShipCommand(sailShipRequest.getShipId(), sailShipRequest.getDestination()));
    }

    @PostMapping("/dockers")
    public CompletableFuture<Void> dockShip(@RequestBody DockShipRequest dockShipRequest) {

        return commandGateway.send(new DockShipCommand(dockShipRequest.getShipId(), dockShipRequest.getLocation()));
    }
}
