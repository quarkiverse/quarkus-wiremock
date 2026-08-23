package io.quarkiverse.wiremock.devservice;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.github.tomakehurst.wiremock.client.WireMock;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@ConnectWireMock(restClient = "greeting-api")
@ConnectWireMock(restClient = "farewell-api")
class WireMockRestClientRedirectTest {

    private static final String GREETING_MSG = "Hello from WireMock!";
    private static final String FAREWELL_MSG = "Goodbye from WireMock!";

    WireMock wiremock; // will be injected automatically when the class has been annotated with @ConnectWireMock

    @RestClient
    GreetingApiClient greetingApiClient;

    @RestClient
    FarewellApiClient farewellApiClient;

    @Test
    void testRestClientsAreAutomaticallyRedirectedToWireMock() {
        Assertions.assertNotNull(wiremock);
        wiremock.register(get(urlEqualTo("/mock-me")).willReturn(aResponse().withStatus(200).withBody(GREETING_MSG)));
        wiremock.register(get(urlEqualTo("/mock-me-too")).willReturn(aResponse().withStatus(200).withBody(FAREWELL_MSG)));

        // no manual `quarkus.rest-client.*.url` properties needed: repeating @ConnectWireMock(restClient = "...")
        // redirects each client's base URL to the WireMock Dev Service for us.
        Assertions.assertEquals(GREETING_MSG, greetingApiClient.greet());
        Assertions.assertEquals(FAREWELL_MSG, farewellApiClient.farewell());
    }

}
