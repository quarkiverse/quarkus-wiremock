package io.quarkiverse.wiremock.devservice;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.github.tomakehurst.wiremock.client.WireMock;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@ConnectWireMock
class WireMockDevServiceResourceTest {

    private static final String MOCK_MSG = "Hello from WireMock!";
    WireMock wiremock; // will be injected automatically when the class has been annotated with @ConnectWireMock

    @Test
    void testHelloEndpoint() {
        Assertions.assertNotNull(wiremock);
        wiremock.register(get(urlEqualTo("/mock-me")).willReturn(aResponse().withStatus(200).withBody(MOCK_MSG)));

        given().when().get("/hello").then().statusCode(200).body(is(MOCK_MSG));
    }

}
