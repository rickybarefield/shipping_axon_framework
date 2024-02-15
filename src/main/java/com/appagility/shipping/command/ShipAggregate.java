package com.appagility.shipping.command;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
public class ShipAggregate {

    @AggregateIdentifier
    private String shipId;

    private String shipName;

    private String location;

    private String destination;

    private boolean readyForSailing;

    private boolean shipIsSailing = false;

    @CommandHandler
    public ShipAggregate(CreateShipCommand createShipCommand) {

        AggregateLifecycle.apply(new ShipCreatedEvent(createShipCommand.getShipId(), createShipCommand.getShipName()));
    }

    @CommandHandler
    public void handle(ReadyForSailingCommand readyForSailingCommand) {

        if(readyForSailing) {

            throw new AlreadyReadyForSailingException();
        }

        AggregateLifecycle.apply(new ShipReadyForSailingEvent(readyForSailingCommand.getShipId()));
    }

    @CommandHandler
    public void handle(SailShipCommand sailShipCommand) {

        if(!readyForSailing) {

            throw new NotReadyForSailingException();
        }

        AggregateLifecycle.apply(new ShipSailedEvent(sailShipCommand.getShipId(), location, sailShipCommand.getDestination()));
    }

    @CommandHandler
    public void handle(DockShipCommand dockShipCommand) {

        if(!shipIsSailing) {

            throw new CannotDockAShipThatIsNotSailingException();
        }

        AggregateLifecycle.apply(new ShipDockedEvent(dockShipCommand.getShipId(), dockShipCommand.getLocation()));
    }

    @EventSourcingHandler
    public void on(ShipCreatedEvent shipCreatedEvent) {

        this.shipId = shipCreatedEvent.getShipId();
        this.shipName = shipCreatedEvent.getShipName();
        this.readyForSailing = false;
    }

    @EventSourcingHandler
    public void on(ShipDockedEvent shipDockedEvent) {

        this.destination = null;
        this.location = shipDockedEvent.getLocation();
        this.shipIsSailing = false;
    }

    @EventSourcingHandler
    public void on(ShipReadyForSailingEvent shipReadyForSailingEvent) {

        this.readyForSailing = true;
    }

    @EventSourcingHandler
    public void on(ShipSailedEvent shipSailedEvent) {

        this.location = null;
        this.destination = shipSailedEvent.getDestination();
        this.shipIsSailing = true;
    }

    protected ShipAggregate() {

    }
}
