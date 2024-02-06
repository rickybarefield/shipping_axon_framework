package com.appagility.shipping;

import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ShipsEventHandler {

    private final Map<String, Ship> ships = new HashMap<>();

    @EventHandler
    public void on(ShipCreatedEvent shipCreatedEvent) {

        ships.put(shipCreatedEvent.getShipId(), new Ship(shipCreatedEvent.getShipId(), shipCreatedEvent.getShipName()));
    }

    @QueryHandler
    public List<Ship> handle(FindAllShipsQuery findAllShipsQuery) {

        return new ArrayList<>(ships.values());
    }
}
