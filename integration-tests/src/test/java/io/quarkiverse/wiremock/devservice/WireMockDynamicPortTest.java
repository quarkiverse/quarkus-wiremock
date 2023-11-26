package io.quarkiverse.wiremock.devservice;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import java.net.URL;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.github.tomakehurst.wiremock.client.WireMock;

import io.quarkiverse.wiremock.devservice.Profile.DynamicPort;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusTest
@ConnectWireMock
@TestProfile(DynamicPort.class)
class WireMockDynamicPortTest {

    private static final String MOCK_MSG = "Hello from WireMock!";
    WireMock wiremock; // will be injected automatically when the class has been annotated with @ConnectWireMock

    @ConfigProperty(name = WireMockConfigKey.PORT)
    Integer port;

    @ConfigProperty(name = "custom.config.wiremock.url")
    URL url;

    @Test
    void testDynamicPortMapping() {
        Assertions.assertNotNull(wiremock);
        wiremock.register(get(urlEqualTo("/mock-me")).willReturn(aResponse().withStatus(200).withBody(MOCK_MSG)));
        given().when().get(String.format("http://localhost:%d/mock-me", port)).then().statusCode(200)
                .body(is(MOCK_MSG));
    }

    @Test
    void testDynamicPortMappingPropagatedViaCustomProperty() {
        Assertions.assertNotNull(wiremock);
        wiremock.register(get(urlEqualTo("/mock-me")).willReturn(aResponse().withStatus(200).withBody(MOCK_MSG)));
        given().when().get(url).then().statusCode(200).body(is(MOCK_MSG));
    }

}
