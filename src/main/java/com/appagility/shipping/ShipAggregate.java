package com.appagility.shipping;

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

    private boolean readyForSailing;

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

    @EventSourcingHandler
    public void on(ShipCreatedEvent shipCreatedEvent) {

        this.shipId = shipCreatedEvent.getShipId();
        this.shipName = shipCreatedEvent.getShipName();
        this.readyForSailing = false;
    }

    protected ShipAggregate() {

    }
}
