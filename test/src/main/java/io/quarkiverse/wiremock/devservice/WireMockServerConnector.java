package io.quarkiverse.wiremock.devservice;

import static io.quarkiverse.wiremock.devservice.WireMockDevServiceConfig.PORT;
import static io.quarkiverse.wiremock.devservice.WireMockDevServiceConfig.PREFIX;

import java.util.Collections;
import java.util.Map;

import org.jboss.logging.Logger;

import com.github.tomakehurst.wiremock.client.WireMock;

import io.quarkus.test.common.DevServicesContext;
import io.quarkus.test.common.QuarkusTestResourceConfigurableLifecycleManager;

public class WireMockServerConnector
        implements QuarkusTestResourceConfigurableLifecycleManager<ConnectWireMock>, DevServicesContext.ContextAware {

    private static final Logger LOGGER = Logger.getLogger(WireMockServerConnector.class);
    private static final String CONFIG_TEMPLATE = "%%dev,test.%s.%s";
    WireMock wiremock;

    @Override
    public Map<String, String> start() {
        // nothing to do, since the Dev Service has already started the server
        return Collections.emptyMap();
    }

    @Override
    public void stop() {
        // nothing to do, since the Dev Service will shut down the server
    }

    @Override
    public void inject(TestInjector testInjector) {
        testInjector.injectIntoFields(wiremock, new TestInjector.MatchesType(WireMock.class));
    }

    @Override
    public void setIntegrationTestContext(DevServicesContext context) {
        final Map<String, String> devContext = context.devServicesProperties();
        int port = Integer.parseInt(devContext.get(getPropertyKey(PORT)));
        try {
            wiremock = new WireMock(port);
            wiremock.getGlobalSettings(); // establish a connection to WireMock server eagerly
        } catch (Exception ex) {
            LOGGER.error("WireMock server not found! It should run as Dev Service.", ex);
            throw ex;
        }
    }

    private static String getPropertyKey(String propertyName) {
        return String.format(CONFIG_TEMPLATE, PREFIX, propertyName);
    }

}
