package io.quarkiverse.wiremock.devservice;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.ServerSocket;

import org.eclipse.microprofile.config.ConfigProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.github.tomakehurst.wiremock.client.WireMock;

import io.quarkus.test.common.DevServicesContext;
import io.quarkus.test.junit.QuarkusIntegrationTest;

@QuarkusIntegrationTest
@ConnectWireMock
class WireMockDevServiceBasicIT {
    WireMock wiremock; // will be injected automatically when the class has been annotated with @ConnectWireMock

    // Quarkus can inject the Dev Service context even into integration tests.
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
    void testWireMockDevServiceConfigPropagation() {
        // Dev Service config gets propagated and is accessible via the config provider
        int port = ConfigProvider.getConfig().getValue(WireMockConfigKey.PORT, Integer.class);
        assertTrue(isInUse(port));
    }

    private static boolean isInUse(int port) {
        try (ServerSocket ignored = new ServerSocket(port)) {
            return false;
        } catch (IOException e) {
            return true;
        }
    }
}
