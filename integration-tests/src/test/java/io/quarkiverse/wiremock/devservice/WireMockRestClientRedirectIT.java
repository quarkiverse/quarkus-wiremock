package io.quarkiverse.wiremock.devservice;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;

import com.github.tomakehurst.wiremock.client.WireMock;

import io.quarkus.test.junit.QuarkusIntegrationTest;

/**
 * The application under test calls WireMock via a redirected REST client. This also works when the application runs
 * in a container ({@code -Dquarkus.container-image.build=true}), because the published URL then points to the host.
 */
@QuarkusIntegrationTest
@ConnectWireMock(restClient = "greeting-api")
class WireMockRestClientRedirectIT {

    private static final String GREETING_MSG = "Hello from WireMock!";

    WireMock wiremock; // will be injected automatically when the class has been annotated with @ConnectWireMock

    @Test
    void testApplicationReachesWireMock() {
        wiremock.register(get(urlEqualTo("/mock-me")).willReturn(aResponse().withStatus(200).withBody(GREETING_MSG)));
        given().when().get("/greeting").then().statusCode(200).body(is(GREETING_MSG));
    }
}
