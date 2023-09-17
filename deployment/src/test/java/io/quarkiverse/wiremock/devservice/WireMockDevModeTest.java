package io.quarkiverse.wiremock.devservice;

import static io.quarkiverse.wiremock.devservice.WireMockConfig.APP_PROPERTIES;
import static io.quarkiverse.wiremock.devservice.WireMockDevServicesBuildTimeConfig.DEFAULT_PORT;
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
    private static final String TEST_PORT = "8090";

    private static final String BASE_URL = "/quarkus/wiremock/devservices";

    // Start hot reload (DevMode) test with your extension loaded
    @RegisterExtension
    static final QuarkusDevModeTest DEV_MODE_TEST = new QuarkusDevModeTest().setArchiveProducer(
            () -> ShrinkWrap.create(JavaArchive.class).addClass(ConfigProviderResource.class)
                    .addAsResource(APP_PROPERTIES));

    @Test
    public void testPortMappingViaLiveReload() {
        assertTrue(isPortInUse(DEFAULT_PORT), "WireMock DevService doesn't listen on port " + DEFAULT_PORT);

        RestAssured.get(BASE_URL + "/reload").then().body(equalTo("true"));
        DEV_MODE_TEST.modifyResourceFile(APP_PROPERTIES,
                s -> s.replace("reload=true", "reload=true\nquarkus.wiremock.devservices.port=" + TEST_PORT));

        // REST request triggers live reload
        RestAssured.get(BASE_URL + "/port").then().body(equalTo(TEST_PORT));

        assertTrue(isPortInUse(TEST_PORT), "WireMock DevService doesn't listen on port " + TEST_PORT);
    }

    private static boolean isPortInUse(String port) {
        try (ServerSocket ignored = new ServerSocket(Integer.valueOf(port))) {
            return false;
        } catch (IOException e) {
            return true;
        }
    }
}
