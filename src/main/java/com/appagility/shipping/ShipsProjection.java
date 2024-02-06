package com.appagility.shipping;

import jakarta.persistence.EntityManager;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShipsProjection {

    private final EntityManager entityManager;

    public ShipsProjection(EntityManager entityManager) {

        this.entityManager = entityManager;
    }

    @EventHandler
    public void on(ShipCreatedEvent shipCreatedEvent) {

        entityManager.persist(new Ship(shipCreatedEvent.getShipId(), shipCreatedEvent.getShipName()));
    }

    @EventHandler
    public void on(ShipReadyForSailingEvent shipReadyEvent) {

        var ship = entityManager.find(Ship.class, shipReadyEvent.getShipId());
        ship.setReadyForSailing(true);
    }

    @QueryHandler
    public List<Ship> handle(FindAllShipsQuery findAllShipsQuery) {

        return entityManager.createQuery("SELECT s FROM Ships").getResultList();
    }

    @QueryHandler
    public List<Ship> handle(FindAllShipsReadyForSailingQuery findAllShipsReadyQuery) {

        return entityManager.createQuery("SELECT s FROM Ships WHERE s.isReadyForSailing = true").getResultList();
    }
}
