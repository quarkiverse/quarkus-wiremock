package io.quarkiverse.wiremock.devservice;

import static io.quarkiverse.wiremock.devservice.WireMockConfigKey.PORT;
import static java.lang.String.format;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.ServerSocket;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import io.quarkus.test.QuarkusDevModeTest;
import io.restassured.RestAssured;

/**
 * Write your dev mode tests here - see the <a href="https://quarkus.io/guides/writing-extensions#testing-hot-reload">testing
 * extension guide</a> for more information.
 */
@SuppressWarnings("java:S5786")
public class WireMockLiveReloadTest {
    private static final String BASE_URL = "/quarkus/wiremock/devservices";
    private static final int TARGET_STATIC_WIREMOCK_PORT = 50000;
    private static final String APP_PROPERTIES = "application.properties";

    // Start hot reload (DevMode) test with your extension loaded
    @RegisterExtension
    static final QuarkusDevModeTest DEV_MODE_TEST = new QuarkusDevModeTest().setArchiveProducer(
            () -> ShrinkWrap.create(JavaArchive.class).addClass(ConfigProviderResource.class)
                    .addAsResource(APP_PROPERTIES));

    @Test
    void testPortModificationViaLiveReload() {

        assertFalse(isInUse(TARGET_STATIC_WIREMOCK_PORT),
                "Port" + TARGET_STATIC_WIREMOCK_PORT + " is already in use!");

        // add port configuration to the properties file
        DEV_MODE_TEST.modifyResourceFile(APP_PROPERTIES,
                s -> s.concat(System.lineSeparator() + PORT + "=" + TARGET_STATIC_WIREMOCK_PORT));

        // Currently, the live-reload can only be triggered via a http call.
        // See https://github.com/quarkiverse/quarkus-wiremock/issues/69 for more details.
        // This will be enhanced with the next development iteration.
        RestAssured.get(format("%s/config?name=%s", BASE_URL, PORT)).then()
                .body(equalTo(String.valueOf(TARGET_STATIC_WIREMOCK_PORT)));

        assertTrue(isInUse(TARGET_STATIC_WIREMOCK_PORT),
                "WireMock Dev Service doesn't listen on port " + TARGET_STATIC_WIREMOCK_PORT);
    }

    private static boolean isInUse(int port) {
        try (ServerSocket ignored = new ServerSocket(port)) {
            return false;
        } catch (IOException e) {
            return true;
        }
    }
}
