package io.quarkiverse.wiremock.devservice;

import static io.quarkiverse.wiremock.devservice.WireMockConfigKey.HOST;
import static io.quarkiverse.wiremock.devservice.WireMockConfigKey.PORT;
import static io.quarkiverse.wiremock.devservice.WireMockConfigKey.URL;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.eclipse.microprofile.config.ConfigProvider;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import io.quarkus.test.QuarkusUnitTest;

/**
 * Forcing Dev Services onto a shared network must not change the published host, since the application itself still
 * runs on the host. Only a containerized application under test requires the host alias of the container runtime.
 */
class WireMockSharedNetworkTest {

    private static final String APP_PROPERTIES = "application.properties";

    @RegisterExtension
    static final QuarkusUnitTest UNIT_TEST = new QuarkusUnitTest().withConfigurationResource(APP_PROPERTIES)
            .overrideConfigKey("quarkus.devservices.launch-on-shared-network", "true")
            .setArchiveProducer(() -> ShrinkWrap.create(JavaArchive.class));

    @Test
    void testHostRemainsLocalhost() {
        final int port = ConfigProvider.getConfig().getValue(PORT, Integer.class);
        assertEquals("localhost", ConfigProvider.getConfig().getValue(HOST, String.class));
        assertEquals("http://localhost:" + port, ConfigProvider.getConfig().getValue(URL, String.class));
    }
}
