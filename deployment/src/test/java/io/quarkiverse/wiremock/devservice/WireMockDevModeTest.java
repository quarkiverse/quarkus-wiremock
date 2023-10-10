package io.quarkiverse.wiremock.devservice;

import static io.quarkiverse.wiremock.devservice.TestUtil.APP_PROPERTIES;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.ServerSocket;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import io.quarkus.test.QuarkusDevModeTest;
import io.restassured.RestAssured;

// Write your dev mode tests here - see the testing extension guide https://quarkus.io/guides/writing-extensions#testing-hot-reload for more information
public class WireMockDevModeTest {
    private static final int DEV_MODE_PORT = 9099;
    private static final int DEV_MODE_PORT_MODIFIED = 9100;
    private static final String BASE_URL = "/quarkus/wiremock/devservices";

    // Start hot reload (DevMode) test with your extension loaded
    @RegisterExtension
    static final QuarkusDevModeTest DEV_MODE_TEST = new QuarkusDevModeTest().setArchiveProducer(
            () -> ShrinkWrap.create(JavaArchive.class).addClass(ConfigProviderResource.class)
                    .addAsResource(APP_PROPERTIES));

    @Test
    public void testPortMappingViaLiveReload() {
        assertTrue(isPortInUse(DEV_MODE_PORT), "WireMock DevService doesn't listen on port " + DEV_MODE_PORT);

        RestAssured.get(BASE_URL + "/reload").then().body(equalTo("true"));
        DEV_MODE_TEST.modifyResourceFile(APP_PROPERTIES,
                s -> s.replace("devservices.port=" + DEV_MODE_PORT, "devservices.port=" + DEV_MODE_PORT_MODIFIED));

        // REST request triggers live reload
        RestAssured.get(BASE_URL + "/port").then().body(equalTo(String.valueOf(DEV_MODE_PORT_MODIFIED)));

        assertTrue(isPortInUse(DEV_MODE_PORT_MODIFIED),
                "WireMock DevService doesn't listen on port " + DEV_MODE_PORT_MODIFIED);
    }

    private static boolean isPortInUse(int port) {
        try (ServerSocket ignored = new ServerSocket(port)) {
            return false;
        } catch (IOException e) {
            return true;
        }
    }
}
