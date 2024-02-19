package com.appagility.shipping.query;

import com.appagility.shipping.command.ShipCreatedEvent;
import com.appagility.shipping.command.ShipReadyForSailingEvent;
import jakarta.persistence.EntityManager;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ShipsProjection {

    private final EntityManager entityManager;
    private final QueryUpdateEmitter queryUpdateEmitter;

    public ShipsProjection(EntityManager entityManager, QueryUpdateEmitter queryUpdateEmitter) {

        this.entityManager = entityManager;
        this.queryUpdateEmitter = queryUpdateEmitter;
    }

    @EventHandler
    public void on(ShipCreatedEvent shipCreatedEvent) {

        Ship newShip = new Ship(shipCreatedEvent.getShipId(), shipCreatedEvent.getShipName());
        entityManager.persist(newShip);
        queryUpdateEmitter.emit(FindAllShipsQuery.class, x -> true, newShip);
    }

    @EventHandler
    public void on(ShipReadyForSailingEvent shipReadyEvent) {

        var ship = entityManager.find(Ship.class, shipReadyEvent.getShipId());
        ship.setReadyForSailing(true);
        queryUpdateEmitter.emit(FindAllShipsQuery.class, x -> true, ship);
        queryUpdateEmitter.emit(FindAllShipsReadyForSailingQuery.class, x -> true, ship);
    }

    @QueryHandler
    public List<Ship> handle(FindAllShipsQuery findAllShipsQuery) {

        return entityManager.createQuery("FROM com.appagility.shipping.query.Ship").getResultList();
    }

    @QueryHandler
    public List<Ship> handle(FindAllShipsReadyForSailingQuery findAllShipsReadyQuery) {

        return entityManager.createQuery("FROM com.appagility.shipping.query.Ship s WHERE s.isReadyForSailing = true").getResultList();
    }
}
