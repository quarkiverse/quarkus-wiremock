package io.quarkiverse.wiremock.devservice;

import static io.quarkiverse.wiremock.devservice.WireMockDevServiceConfig.PORT;
import static io.quarkiverse.wiremock.devservice.WireMockDevServiceConfig.PREFIX;
import static org.hamcrest.Matchers.is;
import static org.jboss.resteasy.reactive.RestResponse.StatusCode.OK;

import org.eclipse.microprofile.config.ConfigProvider;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import io.quarkus.test.QuarkusUnitTest;
import io.restassured.RestAssured;

class WireMockTemplatingTest {

    private static final String APP_PROPERTIES = "application-templating.properties";

    @RegisterExtension
    static final QuarkusUnitTest UNIT_TEST = new QuarkusUnitTest().withConfigurationResource(APP_PROPERTIES)
            .setArchiveProducer(() -> ShrinkWrap.create(JavaArchive.class));

    @Test
    void testTemplatingEnabled() {
        final int port = ConfigProvider.getConfig().getValue(PREFIX + "." + PORT, Integer.class);
        RestAssured.when().get(String.format("http://localhost:%d/template", port)).then().statusCode(OK)
                .body(is(String.format("Everything was just fine from %d!", port)));
    }
}
