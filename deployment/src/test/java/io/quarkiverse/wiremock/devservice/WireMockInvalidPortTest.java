package io.quarkiverse.wiremock.devservice;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import io.quarkus.runtime.configuration.ConfigurationException;
import io.quarkus.test.QuarkusExtensionTest;

class WireMockInvalidPortTest {

    private static final String APP_PROPERTIES = "application-invalid-port.properties";

    @RegisterExtension
    static final QuarkusExtensionTest UNIT_TEST = new QuarkusExtensionTest().setExpectedException(ConfigurationException.class)
            .withConfigurationResource(APP_PROPERTIES).setArchiveProducer(() -> ShrinkWrap.create(JavaArchive.class));

    @Test
    void youShallNotPass() {
        Assertions.fail(); // should not be called, deployment exception should happen first
    }
}
