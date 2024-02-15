package com.appagility.shipping;

import jakarta.persistence.EntityManager;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Service;

@Service
public class SailingProjection {

    private final EntityManager entityManager;

    public SailingProjection(EntityManager entityManager) {

        this.entityManager = entityManager;
    }

    @EventHandler
    public void on(ShipSailedEvent shipSailedEvent) {

        var sailing = new Sailing(shipSailedEvent.getShipId(), shipSailedEvent.getSource(), null, shipSailedEvent.getDestination());

        entityManager.persist(sailing);
    }

    @EventHandler
    public void on(ShipDockedEvent shipDockedEvent) {

        var query = entityManager.createQuery("SELECT s FROM Sailing s WHERE destination IS NULL AND shipId = ?");
        query.setParameter(1, shipDockedEvent.getShipId());


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
    }
}
