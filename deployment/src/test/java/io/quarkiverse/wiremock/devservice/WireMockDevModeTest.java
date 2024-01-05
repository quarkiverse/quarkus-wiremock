package io.quarkiverse.wiremock.devservice;

import static org.hamcrest.Matchers.is;
import static org.jboss.resteasy.reactive.RestResponse.StatusCode.OK;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import io.quarkus.test.QuarkusDevModeTest;
import io.restassured.RestAssured;

@SuppressWarnings("java:S5786")
public class WireMockDevModeTest {

    private static final String APP_PROPERTIES = "application-static-port.properties";

    @RegisterExtension
    static final QuarkusDevModeTest DEV_MODE_TEST = new QuarkusDevModeTest().setArchiveProducer(
            () -> ShrinkWrap.create(JavaArchive.class)
                    .addAsResource(APP_PROPERTIES, "application.properties"));

    @Test
    void testJsonMappingFilesRecognition() {
        RestAssured.when().get("http://localhost:50200/wiremock").then().statusCode(OK)
                .body(is("Everything was just fine!"));
    }
}
