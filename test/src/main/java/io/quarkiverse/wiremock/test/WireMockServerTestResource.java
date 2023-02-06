package io.quarkiverse.wiremock.test;

import java.util.Map;

import javax.inject.Inject;

import org.jboss.logging.Logger;

import com.github.tomakehurst.wiremock.client.WireMock;

import io.quarkiverse.wiremock.deployment.QuarkusWireMockConfig;
import io.quarkus.test.common.DevServicesContext;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

public class WireMockServerTestResource implements QuarkusTestResourceLifecycleManager, DevServicesContext.ContextAware {

    @Inject
    QuarkusWireMockConfig config;

    WireMock WIREMOCK;

    private static final Logger LOGGER = Logger.getLogger(WireMockServerTestResource.class);

    @Override
    public Map<String, String> start() {
        LOGGER.debug("start");
        WIREMOCK = new WireMock(config.host, config.port);
        LOGGER.debug(WIREMOCK);
        return null;
    }

    @Override
    public void stop() {

    }

    @Override
    public void inject(TestInjector testInjector) {
        LOGGER.error("Injecting");
        testInjector.injectIntoFields(WIREMOCK,
                new TestInjector.AnnotatedAndMatchesType(InjectWireMock.class, WireMock.class));
    }

    @Override
    public void setIntegrationTestContext(DevServicesContext context) {

    }
}
