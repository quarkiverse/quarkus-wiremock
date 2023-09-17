package io.quarkiverse.wiremock.devservices;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.github.tomakehurst.wiremock.client.WireMock;

import io.quarkiverse.wiremock.devservice.InjectWireMock;
import io.quarkiverse.wiremock.devservice.WireMockServerTestResource;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@QuarkusTestResource(WireMockServerTestResource.class)
class WireMockDevServiceResourceTest {

    private static final String MOCK_MSG = "Hello from WireMock!";

    @InjectWireMock
    WireMock wiremock;

    @Test
    void testHelloEndpoint() {
        Assertions.assertNotNull(wiremock);
        wiremock.register(get(urlEqualTo("/mock-me")).willReturn(aResponse().withStatus(200).withBody(MOCK_MSG)));

        given().when().get("/hello").then().statusCode(200).body(is(MOCK_MSG));
    }

}
