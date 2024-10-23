package io.quarkiverse.wiremock.devservice;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

import jakarta.inject.Inject;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;

import com.github.tomakehurst.wiremock.client.WireMock;

import io.quarkus.test.junit.QuarkusTest;

@ConnectWireMock
@QuarkusTest
public class WiremockRestClientTest {

    @Inject
    @RestClient
    QuarkusClient quarkusClient;
    WireMock wiremock;

    @Test
    void testWireMockRestClientProxy() {
        wiremock.register(get(urlEqualTo("/mock-rest-client"))
                .willReturn(aResponse().withStatus(200).withHeader("Content-Type", "application/json")
                        .withBody("{\"name\": \"John Doe\", \"email\": \"john.doe@quarkus.io\"}")));
        QuarkusClient.User result = quarkusClient.test();
        assertEquals("John Doe", result.name());
    }
}
