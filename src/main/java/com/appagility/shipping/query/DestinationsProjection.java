package com.appagility.shipping.query;

import com.appagility.shipping.command.ShipDockedEvent;
import com.appagility.shipping.command.ShipSailedEvent;
import jakarta.persistence.EntityManager;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Profile({"v2"})
public class DestinationsProjection {

    private final EntityManager entityManager;

    public DestinationsProjection(EntityManager entityManager) {

        this.entityManager = entityManager;
    }

    @EventHandler
    public void on(ShipSailedEvent shipSailedEvent) {

        findOrCreate(shipSailedEvent.getDestination());
    }

    @EventHandler
    public void on(ShipDockedEvent shipDockedEvent) {

        findOrCreate(shipDockedEvent.getLocation()).addVisit();
    }

    @QueryHandler
    public List<Destination> handle(FindAllDestinationsQuery findAllDestinationsQuery) {

        return entityManager.createQuery("FROM Destination").getResultList();
    }

    private Destination findOrCreate(String destinationName) {

        var destination = entityManager.find(Destination.class, destinationName);

        if(destination == null) {

            destination = new Destination(destinationName);
            entityManager.persist(destination);
        }
        return destination;
    }
}
