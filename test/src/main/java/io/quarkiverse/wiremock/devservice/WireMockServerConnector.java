package io.quarkiverse.wiremock.devservice;

import static io.quarkiverse.wiremock.devservice.WireMockConfigKey.PORT;

import java.util.Collections;
import java.util.Map;

import org.jboss.logging.Logger;

import com.github.tomakehurst.wiremock.client.WireMock;

import io.quarkus.test.common.DevServicesContext;
import io.quarkus.test.common.QuarkusTestResourceConfigurableLifecycleManager;

public class WireMockServerConnector
        implements QuarkusTestResourceConfigurableLifecycleManager<ConnectWireMock>, DevServicesContext.ContextAware {

    private static final Logger LOGGER = Logger.getLogger(WireMockServerConnector.class);
    private static final String CONFIG_TEMPLATE = "%%dev,test.%s";
    WireMock wiremock;

    @Override
    public Map<String, String> start() {
        // nothing to do, since the Dev Service has already started the server
        // and the Dev Service configuration will be propagated by Quarkus automatically.
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
        try {
            int port = Integer.parseInt(devContext.get(getPropertyKey(PORT)));
            wiremock = new WireMock(port);
            wiremock.getGlobalSettings(); // establish a connection to WireMock server eagerly
        } catch (Exception ex) {
            LOGGER.error("Cannot connect to WireMock server!", ex);
            throw ex;
        }
    }

    private static String getPropertyKey(String propertyName) {
        return String.format(CONFIG_TEMPLATE, propertyName);
    }

}
