package com.appagility.shipping.query;

import com.appagility.shipping.command.ShipDockedEvent;
import com.appagility.shipping.command.ShipSailedEvent;
import jakarta.persistence.EntityManager;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SailingProjection {

    private final EntityManager entityManager;

    private final QueryUpdateEmitter queryUpdateEmitter;

    public SailingProjection(EntityManager entityManager, QueryUpdateEmitter queryUpdateEmitter) {

        this.entityManager = entityManager;
        this.queryUpdateEmitter = queryUpdateEmitter;
    }

    @EventHandler(payloadType = ShipSailedEvent.class)
    public void on(ShipSailedEvent shipSailedEvent) {

        System.out.println("Handling ship sailed event");
        var sailing = new Sailing(shipSailedEvent.getShipId(), shipSailedEvent.getSource(), null, shipSailedEvent.getDestination());

        entityManager.persist(sailing);
        queryUpdateEmitter.emit(FindAllSailingsQuery.class, q -> true, sailing);
    }

    @EventHandler(payloadType = ShipDockedEvent.class)
    public void on(ShipDockedEvent shipDockedEvent) {

        System.out.println("Handling ship docked event");
        var query = entityManager.createQuery("SELECT s FROM Sailing s WHERE destination IS NULL AND shipId = :shipId");
        query.setParameter("shipId", shipDockedEvent.getShipId());

        var results = query.getResultList();

        if(results.isEmpty()) {

            
            System.out.println("No sailing exists!");
            return;
        }
        if(results.size() > 1) {

            System.out.println("Multiple matching sailings");
            return;
        }

        var sailing = (Sailing)results.get(0);

        sailing.setDestination(shipDockedEvent.getLocation());
        System.out.println("Emitting update for " + FindAllSailingsQuery.class.getName() + " of " + sailing);
        queryUpdateEmitter.emit(FindAllSailingsQuery.class, q -> !q.isLimitToUnintendedDestinations() || sailing.arrivedInUnintendedDestination(), sailing);
    }

    @QueryHandler
    public List<Sailing> handle(FindAllSailingsQuery findAllSailingsQuery) {

        final String query = findAllSailingsQuery.isLimitToUnintendedDestinations()
                ? "FROM Sailing s WHERE s.destination IS NOT NULL AND s.destination != s.statedDestination"
                : "From Sailing s";

        return entityManager.createQuery(query).getResultList();
    }
}
