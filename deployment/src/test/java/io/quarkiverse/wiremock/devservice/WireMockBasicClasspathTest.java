package io.quarkiverse.wiremock.devservice;

import static io.quarkiverse.wiremock.devservice.WireMockConfigKey.PORT;
import static org.hamcrest.Matchers.is;
import static org.jboss.resteasy.reactive.RestResponse.StatusCode.OK;

import org.eclipse.microprofile.config.ConfigProvider;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import io.quarkus.test.QuarkusUnitTest;
import io.restassured.RestAssured;

/**
 * Verifies that classpath-based stub mappings are loaded correctly without response templating.
 * This isolates the classloader fix from templating concerns.
 */
class WireMockBasicClasspathTest {

    private static final String APP_PROPERTIES = "application-basic-classpath.properties";

    @RegisterExtension
    static final QuarkusUnitTest UNIT_TEST = new QuarkusUnitTest().withConfigurationResource(APP_PROPERTIES)
            .setArchiveProducer(() -> ShrinkWrap.create(JavaArchive.class)
                    .addAsResource("other/mappings/basic-cp.json"));

    @Test
    void testClasspathStubLoaded() {
        final int port = ConfigProvider.getConfig().getValue(PORT, Integer.class);
        RestAssured.when().get(String.format("http://localhost:%d/basic-cp", port))
                .then().statusCode(OK).body(is("Hello from classpath!"));
    }
}
