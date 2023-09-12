package io.quarkiverse.wiremock.test;

import static org.hamcrest.Matchers.equalTo;
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
    private static final String DEFAULT_PORT = "8089";
    private static final String ANOTHER_PORT = "8090";

    private static final String APPLICATION_PROPERTIES = "application.properties";

    // Start hot reload (DevMode) test with your extension loaded
    @RegisterExtension
    static final QuarkusDevModeTest DEV_MODE_TEST = new QuarkusDevModeTest().setArchiveProducer(
            () -> ShrinkWrap.create(JavaArchive.class).addClass(RetrieveConfigValueResource.class)
                    .addAsResource(APPLICATION_PROPERTIES));

    @Test
    public void testPortMappingViaLiveReload() {
        RestAssured.get("/wiremock/config/reload/value").then().body(equalTo("true"));
        RestAssured.get("/wiremock/config/port/value").then().body(equalTo(DEFAULT_PORT));
        assertTrue(isPortInUse(DEFAULT_PORT), "WireMock DevService doesn't listen on port " + DEFAULT_PORT);

        DEV_MODE_TEST.modifyResourceFile(APPLICATION_PROPERTIES,
                s -> s.replace("port=" + DEFAULT_PORT, "port=" + ANOTHER_PORT));

        RestAssured.get("/wiremock/config/port/value").then()
                .body(equalTo(ANOTHER_PORT)); // REST request triggers live reload

        assertTrue(isPortInUse(ANOTHER_PORT), "WireMock DevService doesn't listen on port " + ANOTHER_PORT);
    }

    private static boolean isPortInUse(String port) {
        try (ServerSocket serverSocket = new ServerSocket(Integer.valueOf(port))) {
            return false;
        } catch (IOException e) {
            return true;
        }
    }
}
