package io.quarkiverse.wiremock.devservice;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import io.quarkus.runtime.configuration.ConfigurationException;
import io.quarkus.test.QuarkusUnitTest;

class WireMockInvalidPortTest {

    private static final String APP_PROPERTIES = "application-invalid-port.properties";

    @RegisterExtension
    static final QuarkusUnitTest UNIT_TEST = new QuarkusUnitTest().setExpectedException(ConfigurationException.class)
            .withConfigurationResource(APP_PROPERTIES).setArchiveProducer(() -> ShrinkWrap.create(JavaArchive.class));

    @Test
    void testPersistenceAndConfigTest() {
        Assertions.fail(); // should not be called, deployment exception should happen first
    }
}
