package io.quarkiverse.wiremock.devservice;

import static io.quarkiverse.wiremock.devservice.ConfigProviderResource.BASE_URL;
import static io.quarkiverse.wiremock.devservice.WireMockConfigKey.PORT;
import static java.lang.String.format;
import static org.hamcrest.Matchers.is;
import static org.jboss.resteasy.reactive.RestResponse.StatusCode.OK;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import io.quarkus.test.QuarkusDevModeTest;
import io.restassured.RestAssured;

@SuppressWarnings("java:S5786")
public class WireMockBasicDevModeTest {

    private static final String APP_PROPERTIES = "application-static-port.properties";

    @RegisterExtension
    static final QuarkusDevModeTest DEV_MODE_TEST = new QuarkusDevModeTest().setArchiveProducer(
            () -> ShrinkWrap.create(JavaArchive.class).addClass(ConfigProviderResource.class)
                    .addAsResource(APP_PROPERTIES, "application.properties"));

    @Test
    void testJsonMappingFilesRecognition() {
        String port = RestAssured.get(format("%s/config?propertyName=%s", BASE_URL, PORT)).then().extract().asString();
        RestAssured.when().get(format("http://localhost:%s/basic", port)).then().statusCode(OK)
                .body(is("Everything was just fine!"));
    }
}
