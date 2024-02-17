package com.appagility.shipping.upgrading;

import com.appagility.shipping.CreateShipRequest;
import com.appagility.shipping.SailShipRequest;
import com.appagility.shipping.ShipReadierRequest;
import com.appagility.shipping.ShippingRestEndpoint;
import com.appagility.shipping.query.FindAllSailingsQuery;
import com.appagility.shipping.query.FindAllShipsQuery;
import com.appagility.shipping.query.Sailing;
import com.appagility.shipping.query.Ship;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.Disposable;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@ActiveProfiles("v1")
public class VersionOne {

    private static Duration TIME_TO_WAIT_FOR_PROJECTIONS = Duration.ofSeconds(3);

    @Autowired
    private ShippingRestEndpoint shippingRestEndpoint;

    @Autowired
    private QueryGateway queryGateway;

    @Test
    public void createShipWithTwoSailings() throws Exception {

        var shipId = shippingRestEndpoint.createShip(new CreateShipRequest("The Jolly Roger")).get();

        waitForQueryToReturn(new FindAllShipsQuery(),
                Ship.class,
                ship -> ship.getShipId().equals(shipId));

        shippingRestEndpoint.readyShip(new ShipReadierRequest(shipId));

        waitForQueryToReturn(new FindAllShipsQuery(),
                Ship.class,
                ship -> ship.getShipId().equals(shipId) && ship.isReadyForSailing());

        String intendedDestination = "Cardiff";

        shippingRestEndpoint.createSailing(new SailShipRequest(shipId, intendedDestination));

        waitForQueryToReturn(new FindAllSailingsQuery(false),
                Sailing.class,
                sailing -> sailing.getShipId().equals(shipId) && sailing.getStatedDestination().equals(intendedDestination));
    }

    private <TSingleResult> void waitForQueryToReturn(Object query,
                                                      Class<TSingleResult> singleResultClass,
                                                      Predicate<TSingleResult> condition) throws InterruptedException {

        try(var subscription = queryGateway.subscriptionQuery(query,
                ResponseTypes.multipleInstancesOf(singleResultClass),
                ResponseTypes.instanceOf(singleResultClass))) {

            CountDownLatch latch = new CountDownLatch(1);

            subscription.handle(ships -> {

                        System.out.println("Initial Results ---");
                        ships.forEach(System.out::println);
                        if (ships.stream().anyMatch(condition)) {
                            latch.countDown();
                        }
                    },
                    ship -> {

                        System.out.println("Update received ---");
                        System.out.println(ship);
                        if (condition.test(ship)) {

                            System.out.println("Condition matched");
                            latch.countDown();
                        }

                    });

            var result = latch.await(TIME_TO_WAIT_FOR_PROJECTIONS.toMillis(), TimeUnit.MILLISECONDS);

            assertThat(result).isTrue().as("Timedout waiting for projection to match");
        }
    }

}
