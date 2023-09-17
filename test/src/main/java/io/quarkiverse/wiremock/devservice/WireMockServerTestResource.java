package io.quarkiverse.wiremock.devservice;

import static io.quarkiverse.wiremock.devservice.WireMockConfig.PORT;

import java.util.Collections;
import java.util.Map;

import org.jboss.logging.Logger;

import com.github.tomakehurst.wiremock.client.WireMock;

import io.quarkus.test.common.DevServicesContext;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

public class WireMockServerTestResource
        implements QuarkusTestResourceLifecycleManager, DevServicesContext.ContextAware {

    private static final Logger LOGGER = Logger.getLogger(WireMockServerTestResource.class);
    WireMock wiremock;

    @Override
    public Map<String, String> start() {
        return Collections.emptyMap();
    }

    @Override
    public void stop() {
        LOGGER.debug("Stop callback called!");
    }

    @Override
    public void inject(TestInjector testInjector) {
        testInjector.injectIntoFields(wiremock,
                new TestInjector.AnnotatedAndMatchesType(InjectWireMock.class, WireMock.class));
    }

    @Override
    public void setIntegrationTestContext(DevServicesContext context) {
        final Map<String, String> devContext = context.devServicesProperties();
        int port = Integer.parseInt(devContext.get(PORT));
        try {
            wiremock = new WireMock(port);
        } catch (Exception ex) {
            LOGGER.error("WireMock server not found! It should run as Dev Service.", ex);
            throw ex;
        }
    }

}
