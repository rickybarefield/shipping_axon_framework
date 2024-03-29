package com.appagility.shipping;

import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class CreateShipTest {

    private FixtureConfiguration<ShipAggregate> fixture;

    @BeforeEach
    public void setup() {

        fixture = new AggregateTestFixture<>(ShipAggregate.class);
    }

    @Test
    public void testCreateShip() {

        var shipId = UUID.randomUUID().toString();
        var shipName = "PirateParrot";

        fixture.givenNoPriorActivity()
                .when(new CreateShipCommand(shipId, shipName))
                .expectEvents(new ShipCreatedEvent(shipId, shipName));

    }

}
