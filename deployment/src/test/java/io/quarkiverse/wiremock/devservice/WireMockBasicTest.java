package io.quarkiverse.wiremock.devservice;

import static io.quarkiverse.wiremock.devservice.WireMockConfigKey.HOST;
import static io.quarkiverse.wiremock.devservice.WireMockConfigKey.PORT;
import static io.quarkiverse.wiremock.devservice.WireMockConfigKey.URL;
import static org.hamcrest.Matchers.is;
import static org.jboss.resteasy.reactive.RestResponse.StatusCode.OK;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.eclipse.microprofile.config.ConfigProvider;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import io.quarkus.test.QuarkusUnitTest;
import io.restassured.RestAssured;

class WireMockBasicTest {

    private static final String APP_PROPERTIES = "application.properties";

    @RegisterExtension
    static final QuarkusUnitTest UNIT_TEST = new QuarkusUnitTest().withConfigurationResource(APP_PROPERTIES)
            .setArchiveProducer(() -> ShrinkWrap.create(JavaArchive.class));

    @Test
    void testWireMockMappingsFolder() {
        final int port = ConfigProvider.getConfig().getValue(PORT, Integer.class);
        RestAssured.when().get(String.format("http://localhost:%d/basic", port)).then().statusCode(OK)
                .body(is("Everything was just fine!"));
    }

    @Test
    void testHostAndUrlPropagation() {
        final int port = ConfigProvider.getConfig().getValue(PORT, Integer.class);
        assertEquals("localhost", ConfigProvider.getConfig().getValue(HOST, String.class));
        assertEquals("http://localhost:" + port, ConfigProvider.getConfig().getValue(URL, String.class));
        RestAssured.when().get(ConfigProvider.getConfig().getValue(URL, String.class) + "/basic").then().statusCode(OK)
                .body(is("Everything was just fine!"));
    }

    @Test
    void testTemplatingDisabled() {
        final int port = ConfigProvider.getConfig().getValue(PORT, Integer.class);
        RestAssured.when().get(String.format("http://localhost:%d/template", port)).then().statusCode(OK)
                .body(is("Everything was just fine from {{ request.port }}!"));
    }
}
