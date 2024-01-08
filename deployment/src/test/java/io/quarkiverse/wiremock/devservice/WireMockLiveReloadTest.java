package io.quarkiverse.wiremock.devservice;

import static io.quarkiverse.wiremock.devservice.ConfigProviderResource.BASE_URL;
import static io.quarkiverse.wiremock.devservice.WireMockConfigKey.FILES_MAPPING;
import static io.quarkiverse.wiremock.devservice.WireMockConfigKey.PORT;
import static java.lang.String.format;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.jboss.resteasy.reactive.RestResponse.StatusCode.OK;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.ServerSocket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import io.quarkus.test.QuarkusDevModeTest;
import io.restassured.RestAssured;
import io.restassured.response.Response;

/**
 * Write your dev mode tests here - see the <a href="https://quarkus.io/guides/writing-extensions#testing-hot-reload">testing
 * extension guide</a> for more information.
 */
@SuppressWarnings("java:S5786")
public class WireMockLiveReloadTest {
    private static final int TARGET_STATIC_WIREMOCK_PORT = 50000;
    private static final String APP_PROPERTIES = "application.properties";

    @RegisterExtension
    static final QuarkusDevModeTest DEV_MODE_TEST = new QuarkusDevModeTest().setArchiveProducer(
            () -> ShrinkWrap.create(JavaArchive.class).addClass(ConfigProviderResource.class)
                    .addAsResource(APP_PROPERTIES));

    @Test
    void testPortModificationViaLiveReload() {

        Given.portIsNotInUse(TARGET_STATIC_WIREMOCK_PORT);

        When.addApplicationProperty(System.lineSeparator() + PORT + "=" +
                TARGET_STATIC_WIREMOCK_PORT); // add port configuration to the properties file
        When.callConfigProvider(PORT).then().body(equalTo(String.valueOf(TARGET_STATIC_WIREMOCK_PORT)));

        Then.portIsInUse(TARGET_STATIC_WIREMOCK_PORT);
    }

    @Test
    void testWireMockConfigChangeHotDeployment() throws IOException {

        var tempDir = Files.createTempDirectory(Path.of("target"), "wiremock-");
        var wireMockConfig = Given.wireMockConfigFile(tempDir); // create temporary WireMock config

        // specify temp dir as files-mapping location
        When.addApplicationProperty(System.lineSeparator() + FILES_MAPPING + "=target/" + tempDir.getFileName());
        When.triggerHttpLiveReload(); // --> At this point, Quarkus watches the new config file
        When.callWireMock().then().statusCode(OK).body(is("Modify me at runtime!"));

        When.modifyResponseBody(wireMockConfig);
        When.callWireMock().then().statusCode(OK).body(is("Live reload rockz!"));
    }

    private static class Given {

        private static Path wireMockConfigFile(Path rootLocation) throws IOException {
            var mapLocation = Paths.get(rootLocation.toFile().getAbsolutePath(), "mappings");
            Files.createDirectories(mapLocation);
            var configFile = Paths.get(mapLocation.toFile().getAbsolutePath(), "modify.json");
            Files.write(configFile,
                    "{\"request\":{\"method\":\"GET\",\"url\": \"/modify\"},\"response\":{\"status\": 200,\"body\":\"Modify me at runtime!\"}}"
                            .getBytes(
                                    StandardCharsets.UTF_8));
            return configFile;
        }

        private static void portIsNotInUse(int port) {
            assertFalse(isInUse(port), "Port " + port + " is already in use!");
        }
    }

    private static class When {
        private static void triggerHttpLiveReload() {
            RestAssured.get(format("%s/reload", BASE_URL));
        }

        private static Response callWireMock() {
            return RestAssured.when().get(format("http://localhost:%d/modify", getDynamicPort()));
        }

        private static Response callConfigProvider(String propertyName) {
            return RestAssured.get(format("%s/config?propertyName=%s", BASE_URL, propertyName));
        }

        private static void addApplicationProperty(String property) {
            DEV_MODE_TEST.modifyResourceFile(APP_PROPERTIES, s -> s.concat(property));
        }

        private static void modifyResponseBody(Path configFile) throws IOException {
            Files.writeString(configFile,
                    Files.readString(configFile).replace("Modify me at runtime!", "Live reload rockz!"));
        }
    }

    private static class Then {

        private static void portIsInUse(int port) {
            assertTrue(isInUse(port), "No Service is listening on port " + port);
        }
    }

    private static boolean isInUse(int port) {
        try (ServerSocket ignored = new ServerSocket(port)) {
            return false;
        } catch (IOException e) {
            return true;
        }
    }

    private static int getDynamicPort() {
        return Integer.parseInt(When.callConfigProvider(PORT).then().extract().asString());
    }
}
