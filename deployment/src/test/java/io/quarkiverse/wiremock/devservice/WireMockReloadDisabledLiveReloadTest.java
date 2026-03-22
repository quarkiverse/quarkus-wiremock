package io.quarkiverse.wiremock.devservice;

import static io.quarkiverse.wiremock.devservice.ConfigProviderResource.BASE_URL;
import static io.quarkiverse.wiremock.devservice.WireMockConfigKey.FILES_MAPPING;
import static io.quarkiverse.wiremock.devservice.WireMockConfigKey.PORT;
import static io.quarkiverse.wiremock.devservice.WireMockConfigKey.RELOAD;
import static java.lang.String.format;
import static org.hamcrest.Matchers.is;
import static org.jboss.resteasy.reactive.RestResponse.StatusCode.OK;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
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

/**
 * Verifies the {@code reload=false} behaviour: WireMock must not restart on live reload
 * even when mapping files are modified, preserving the running instance and its in-memory stubs.
 */
@SuppressWarnings("java:S5786")
public class WireMockReloadDisabledLiveReloadTest {

    private static final String APP_PROPERTIES = "application.properties";

    @RegisterExtension
    static final QuarkusDevModeTest DEV_MODE_TEST = new QuarkusDevModeTest()
            .setArchiveProducer(() -> ShrinkWrap.create(JavaArchive.class)
                    .addClass(ConfigProviderResource.class)
                    .addAsResource(APP_PROPERTIES));

    @Test
    void testNoRestartWhenReloadDisabled() throws IOException {
        var tempDir = Files.createTempDirectory(Path.of("target"), "wiremock-");
        var mappingFile = createMappingFile(tempDir);

        // Disable reload and point files-mapping at the temp dir; wait for live reload to apply the config
        DEV_MODE_TEST.modifyResourceFile(APP_PROPERTIES, s -> s
                + System.lineSeparator() + RELOAD + "=false"
                + System.lineSeparator() + FILES_MAPPING + "=target/" + tempDir.getFileName());
        triggerHttpLiveReload(); // ensures file watching is active for the new location

        int portBeforeChange = getWireMockPort();
        RestAssured.when().get(format("http://localhost:%d/modify", portBeforeChange))
                .then().statusCode(OK).body(is("Modify me at runtime!"));

        // Modify the stub — triggers a live reload via file watching,
        // but with reload=false WireMock must not restart
        modifyMappingBody(mappingFile);

        // Same port proves the WireMock instance was not replaced; old stub is still served
        assertEquals(portBeforeChange, getWireMockPort());
        RestAssured.when().get(format("http://localhost:%d/modify", portBeforeChange))
                .then().statusCode(OK).body(is("Modify me at runtime!"));
    }

    private static Path createMappingFile(Path rootDir) throws IOException {
        Path mappings = Paths.get(rootDir.toAbsolutePath().toString(), "mappings");
        Files.createDirectories(mappings);
        Path mappingFile = mappings.resolve("modify.json");
        Files.write(mappingFile,
                "{\"request\":{\"method\":\"GET\",\"url\":\"/modify\"},\"response\":{\"status\":200,\"body\":\"Modify me at runtime!\"}}"
                        .getBytes(StandardCharsets.UTF_8));
        return mappingFile;
    }

    private static void triggerHttpLiveReload() {
        RestAssured.get(format("%s/reload", BASE_URL));
    }

    private static int getWireMockPort() {
        return Integer.parseInt(
                RestAssured.get(format("%s/config?propertyName=%s", BASE_URL, PORT)).then().extract().asString());
    }

    private static void modifyMappingBody(Path mappingFile) throws IOException {
        Files.writeString(mappingFile,
                Files.readString(mappingFile).replace("Modify me at runtime!", "Live reload rockz!"));
    }
}
