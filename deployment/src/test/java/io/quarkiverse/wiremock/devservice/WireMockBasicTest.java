package io.quarkiverse.wiremock.devservice;

import static io.quarkiverse.wiremock.devservice.WireMockConfig.APP_PROPERTIES;
import static io.quarkiverse.wiremock.devservice.WireMockConfig.PORT;
import static org.hamcrest.Matchers.is;

import org.eclipse.jetty.server.Response;
import org.eclipse.microprofile.config.ConfigProvider;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import io.quarkus.test.QuarkusUnitTest;
import io.restassured.RestAssured;

class WireMockBasicTest {

    @RegisterExtension
    static final QuarkusUnitTest UNIT_TEST = new QuarkusUnitTest().withConfigurationResource(APP_PROPERTIES)
            .setArchiveProducer(() -> ShrinkWrap.create(JavaArchive.class));

    @Test
    void testWireMockMappingsFolder() {
        final String port = ConfigProvider.getConfig().getOptionalValue(PORT, String.class).orElseThrow();
        RestAssured.when().get(String.format("http://localhost:%s/wiremock", port)).then().statusCode(Response.SC_OK)
                .body(is("Everything was just fine!"));
    }

}
