package io.quarkiverse.wiremock.it;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import io.quarkiverse.wiremock.test.WireMockServerTestResource;
import io.quarkus.test.common.QuarkusTestResource;
import org.junit.jupiter.api.Test;

import com.github.tomakehurst.wiremock.client.WireMock;

import io.quarkiverse.wiremock.test.InjectWireMock;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;

@QuarkusTest
@QuarkusTestResource(WireMockServerTestResource.class)
public class WiremockDevResourceTest {

    @InjectWireMock
    WireMock wiremock;

    @Test
    public void testHelloEndpoint() {
        Assertions.assertNotNull(wiremock);
        given()
                .when().get("/wiremock-dev")
                .then()
                .statusCode(200)
                .body(is("Hello wiremock-dev"));
    }

}
