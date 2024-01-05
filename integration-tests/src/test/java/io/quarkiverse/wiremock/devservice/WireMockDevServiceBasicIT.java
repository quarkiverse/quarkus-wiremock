package io.quarkiverse.wiremock.devservice;

import static io.quarkiverse.wiremock.devservice.WireMockConfigKey.FILES_MAPPING;
import static io.quarkiverse.wiremock.devservice.WireMockConfigKey.PORT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.NoSuchElementException;

import org.eclipse.microprofile.config.ConfigProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.github.tomakehurst.wiremock.client.WireMock;

import io.quarkus.test.common.DevServicesContext;
import io.quarkus.test.junit.QuarkusIntegrationTest;

@ConnectWireMock
@QuarkusIntegrationTest
class WireMockDevServiceBasicIT {
    WireMock wiremock; // will be injected automatically when the class has been annotated with @ConnectWireMock

    // Quarkus can inject the Dev Services context into integration tests.
    // Please refer to https://quarkus.io/guides/getting-started-testing#testing-dev-services for more details.
    DevServicesContext devServicesContext;

    @Test
    void testWireMockInjection() {
        Assertions.assertNotNull(wiremock);
    }

    @Test
    void testDevServicesContextInjection() {
        Assertions.assertNotNull(devServicesContext);
    }

    @Test
    void testWireMockDevServiceContextPropagation() {
        // Dev Services properties are accessible via the Dev Services context
        assertTrue(isInUse(Integer.parseInt(devServicesContext.devServicesProperties().get(PORT))));
    }

    @Test
    void testWireMockDevServiceConfigPropagation() {
        // Dev Services properties are also accessible via the config provider
        assertTrue(isInUse(ConfigProvider.getConfig().getValue(PORT, Integer.class)));
    }

    @Test
    void testConfigValuePropagationIsConsistent() {
        assertEquals(ConfigProvider.getConfig().getValue(PORT, Integer.class),
                Integer.parseInt(devServicesContext.devServicesProperties().get(PORT)));
    }

    @Test
    void testNonPropagatedConfigsArentAccessibleInIT() {
        assertThrows(NoSuchElementException.class,
                () -> ConfigProvider.getConfig().getValue(FILES_MAPPING, String.class));
    }

    private static boolean isInUse(int port) {
        try (ServerSocket ignored = new ServerSocket(port)) {
            return false;
        } catch (IOException e) {
            return true;
        }
    }
}
