package io.quarkiverse.wiremock.dev.test;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import io.quarkus.runtime.StartupEvent;
import io.quarkus.test.QuarkusUnitTest;

public class WiremockDevTest {

    @RegisterExtension
    static final QuarkusUnitTest unitTest = new QuarkusUnitTest().withConfigurationResource("application.properties")
            .setArchiveProducer(() -> ShrinkWrap.create(JavaArchive.class).addClasses(DevBean.class));

    @Test
    void buildTimeRunTimeConfig() {
        // A combination of QuarkusUnitTest and QuarkusProdModeTest tests ordering may mess with the port leaving it in
        // 8081 and QuarkusDevModeTest does not change to the right port.
        //        RestAssured.port = -1;
        //
        //        RestAssured.when().get("http://localhost:8081/application").then()
        //                .statusCode(200)
        //                .body(is("my-app"));
    }

    @ApplicationScoped
    public static class DevBean {

        public void register(@Observes StartupEvent ev) {

        }
    }
}
