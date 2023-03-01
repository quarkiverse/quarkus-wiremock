package io.quarkiverse.wiremock.it;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.github.tomakehurst.wiremock.client.WireMock;

import io.quarkiverse.wiremock.test.InjectWireMock;
import io.quarkiverse.wiremock.test.WithWireMockServer;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@WithWireMockServer
public class WiremockDevResourceTest {

    @InjectWireMock
    WireMock wiremock;

    @Test
    public void testHelloEndpoint() {
        Assertions.assertNotNull(wiremock);
        wiremock.register(
                get(urlPathEqualTo("/wiremock-dev")).willReturn(aResponse().withStatus(200)
                        .withBody("Hello wiremock-dev")));

        given()
                .when().get("/wiremock-dev")
                .then()
                .statusCode(200)
                .body(is("Hello wiremock-dev"));
    }

}
