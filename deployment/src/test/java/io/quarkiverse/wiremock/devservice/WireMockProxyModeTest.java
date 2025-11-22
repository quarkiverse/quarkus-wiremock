package io.quarkiverse.wiremock.devservice;

import static org.hamcrest.Matchers.is;
import static org.jboss.resteasy.reactive.RestResponse.StatusCode.OK;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import io.quarkus.test.QuarkusUnitTest;
import io.restassured.RestAssured;

class WireMockProxyModeTest {

    private static final String APP_PROPERTIES = "application-proxy-mode.properties";

    @RegisterExtension
    static final QuarkusUnitTest UNIT_TEST = new QuarkusUnitTest().withConfigurationResource(APP_PROPERTIES)
            .setArchiveProducer(() -> ShrinkWrap.create(JavaArchive.class));

    @Test
    void testWireMockMultiDomain() {
        RestAssured.when().get("http://my.first.domain/proxy-mode").then().statusCode(OK)
                .body(is("Domain 1"));
        RestAssured.when().get("http://my.second.domain/proxy-mode").then().statusCode(OK)
                .body(is("Domain 2"));
    }
}
