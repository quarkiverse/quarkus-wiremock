package io.quarkiverse.wiremock.devservice;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.github.tomakehurst.wiremock.client.WireMock;

import io.quarkiverse.wiremock.devservice.Profile.FixedPort;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusTest
@ConnectWireMock
@TestProfile(FixedPort.class)
class WireMockFixedPortTest {
    private static final String MOCK_MSG = "Hello from WireMock!";
    WireMock wiremock; // will be injected automatically when the class has been annotated with @ConnectWireMock

    @Test
    void testFixedPortMapping() {
        Assertions.assertNotNull(wiremock);
        wiremock.register(get(urlEqualTo("/mock-me")).willReturn(aResponse().withStatus(200).withBody(MOCK_MSG)));
        given().when().get(String.format("http://localhost:%s/mock-me", FixedPort.PORT)).then().statusCode(200)
                .body(is(MOCK_MSG));
    }

}
