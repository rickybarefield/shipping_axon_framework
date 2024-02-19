package com.appagility.shipping.upgrading;

import com.appagility.shipping.*;
import com.appagility.shipping.query.FindAllSailingsQuery;
import com.appagility.shipping.query.FindAllShipsQuery;
import com.appagility.shipping.query.Sailing;
import com.appagility.shipping.query.Ship;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("v1")
public class VersionOne {

    private static Duration TIME_TO_WAIT_FOR_PROJECTIONS = Duration.ofSeconds(20);

    @Autowired
    private ShippingRestEndpoint shippingRestEndpoint;

    @Autowired
    private QueryGateway queryGateway;

    @Autowired
    private EntityManager entityManager;

    private WebTestClient webTestClient;

    @BeforeEach
    public void setupTestClient() {


        webTestClient = WebTestClient.bindToServer().baseUrl("http://localhost:8080")
                .defaultHeader("Accept", MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Test
    public void createShipWithTwoSailings() throws Exception {

        var shipId = createShipAndAssertShipsProjection();

        readyShipAndAssertShipsProjection(shipId);
        sailShipToAnIntendedDestinationAssertingSailingsProjection(shipId);
        sailShipToAnUnintendedDestinationAssertingSailingsProjection(shipId);
    }


    private String createShipAndAssertShipsProjection() throws Exception {


        var createdShip = webTestClient.post().uri("/ships")
                .body(Mono.just(new CreateShipRequest("Jolly Stuff")), CreateShipRequest.class)
                .exchange()
                .expectStatus().isOk()
                .returnResult(Ship.class).getResponseBody().blockFirst();

        var shipId = createdShip.getShipId();

        //        var shipId = shippingRestEndpoint.createShip(new CreateShipRequest("The Jolly Roger")).get();

        waitForQueryToReturn(new FindAllShipsQuery(),
                Ship.class,
                ship -> ship.getShipId().equals(shipId));
        return shipId;
    }

    private void readyShipAndAssertShipsProjection(String shipId) throws Exception {

        webTestClient.post().uri("/shipReadiers")
                .body(Mono.just(new ShipReadierRequest(shipId)), ShipReadierRequest.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody().isEmpty();

        waitForQueryToReturn(new FindAllShipsQuery(),
                Ship.class,
                ship -> ship.getShipId().equals(shipId) && ship.isReadyForSailing());
    }

    private void sailShipToAnUnintendedDestinationAssertingSailingsProjection(String shipId) throws InterruptedException {

        sailShipAssertingSailingsProjection(shipId, "Newport", "Jersey");
    }

    private void sailShipToAnIntendedDestinationAssertingSailingsProjection(String shipId) throws InterruptedException {

        String destination = "Cardiff";
        sailShipAssertingSailingsProjection(shipId, destination, destination);

    }

    private void sailShipAssertingSailingsProjection(String shipId, String intendedDestination, String actualDestination) throws InterruptedException {


        webTestClient.post().uri("/sailings")
                .body(Mono.just(new SailShipRequest(shipId, intendedDestination)), SailShipRequest.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody().isEmpty();

        waitForQueryToReturn(new FindAllSailingsQuery(false),
                Sailing.class,
                sailing -> sailing.getShipId().equals(shipId)
                        && sailing.getStatedDestination().equals(intendedDestination));

        webTestClient.post().uri("/dockers")
                .body(Mono.just(new DockShipRequest(shipId, actualDestination)), DockShipRequest.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody().isEmpty();

        boolean limitToUnintendedDestinations = !intendedDestination.equals(actualDestination);
        waitForQueryToReturn(new FindAllSailingsQuery(false),
                Sailing.class,
                sailing -> sailing.getShipId().equals(shipId)
                        && intendedDestination.equals(sailing.getStatedDestination())
                        && actualDestination.equals(sailing.getDestination()));
    }

    private <TSingleResult> void waitForQueryToReturn(Object query,
                                                      Class<TSingleResult> singleResultClass,
                                                      Predicate<TSingleResult> condition) throws InterruptedException {

        CountDownLatch latch = new CountDownLatch(1);

        try (var subscription = queryGateway.subscriptionQuery(query,
                ResponseTypes.multipleInstancesOf(singleResultClass),
                ResponseTypes.instanceOf(singleResultClass))) {

            subscription.updates().subscribe(result -> {

                System.out.println("Update received ---");
                System.out.println(result);
                if (condition.test(result)) {

                    System.out.println("Condition matched");
                    latch.countDown();
                }
            });

            subscription.initialResult().subscribe(result -> {

                System.out.println("Initial Results ---");
                result.forEach(System.out::println);
                if (result.stream().anyMatch(condition)) {
                    latch.countDown();
                }
            });

            var result = latch.await(TIME_TO_WAIT_FOR_PROJECTIONS.toMillis(), TimeUnit.MILLISECONDS);

            assertThat(result).as("Timedout waiting for projection to match").isTrue();
        }

    }

    static String asJsonString(final Object obj) {
        try {
            String json = new ObjectMapper().writeValueAsString(obj);
            return json;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static <T> T jsonToObject(Class<T> objectClass, final String json) {
        try {
            return new ObjectMapper().readValue(json, objectClass);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}


