package io.quarkiverse.wiremock.test;

import java.util.Map;

import org.jboss.logging.Logger;

import com.github.tomakehurst.wiremock.client.WireMock;

import io.quarkiverse.wiremock.runtime.WireMockServerConfig;
import io.quarkus.test.common.DevServicesContext;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

public class WireMockServerTestResource implements QuarkusTestResourceLifecycleManager, DevServicesContext.ContextAware {

    private static final Logger LOGGER = Logger.getLogger(WireMockServerTestResource.class);
    WireMock WIREMOCK;

    @Override
    public Map<String, String> start() {
        LOGGER.debug("start");
        return null;
    }

    @Override
    public void stop() {
    }

    @Override
    public void inject(TestInjector testInjector) {
        LOGGER.debug("Injecting");
        testInjector.injectIntoFields(WIREMOCK,
                new TestInjector.AnnotatedAndMatchesType(InjectWireMock.class, WireMock.class));
    }

    @Override
    public void setIntegrationTestContext(DevServicesContext context) {
        LOGGER.debug("setIntegrationTest");
        Map<String, String> devContext = context.devServicesProperties();
        int port = Integer.parseInt(devContext.get(WireMockServerConfig.PORT));
        String host = "localhost";
        try {
            WIREMOCK = new WireMock(host, port);
        } catch (Exception ex) {
            LOGGER.error("WireMock not found, it should be run from devservices.");
        }
    }

}
