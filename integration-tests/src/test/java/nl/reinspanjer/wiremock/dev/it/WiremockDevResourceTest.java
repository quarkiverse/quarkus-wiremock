package io.quarkiverse.wiremock.dev.it;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class WiremockDevResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
                .when().get("/wiremock-dev")
                .then()
                .statusCode(200)
                .body(is("Hello wiremock-dev"));
    }
}
