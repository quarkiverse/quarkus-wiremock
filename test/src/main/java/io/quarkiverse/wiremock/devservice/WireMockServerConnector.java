package io.quarkiverse.wiremock.devservice;

import static io.quarkiverse.wiremock.devservice.WireMockConfigKey.PORT;
import static java.lang.String.format;

import java.util.Collections;
import java.util.Map;

import com.github.tomakehurst.wiremock.client.WireMock;

import io.quarkus.logging.Log;
import io.quarkus.test.common.DevServicesContext;
import io.quarkus.test.common.QuarkusTestResourceConfigurableLifecycleManager;

public class WireMockServerConnector
        implements QuarkusTestResourceConfigurableLifecycleManager<ConnectWireMock>, DevServicesContext.ContextAware {

    private static final String REST_CLIENT_URL_PROPERTY = "quarkus.rest-client.%s.url";

    WireMock wiremock;

    private String restClient = "";
    private String baseUrl;

    @Override
    public void init(ConnectWireMock annotation) {
        restClient = annotation.restClient();
    }

    @Override
    public Map<String, String> start() {
        // the WireMock server itself is already started by the Dev Service, and its configuration is
        // propagated by Quarkus automatically; here we only need to redirect the requested REST client, if any.
        if (restClient.isBlank()) {
            return Collections.emptyMap();
        }
        String property = format(REST_CLIENT_URL_PROPERTY, restClient);
        Log.debugf("Redirecting REST client [%s] to WireMock via [%s=%s]", restClient, property, baseUrl);
        return Map.of(property, baseUrl);
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
            int port = Integer.parseInt(devContext.get(PORT));
            baseUrl = "http://localhost:" + port;
            wiremock = new WireMock(port);
            WireMock.configureFor(port);
            wiremock.getGlobalSettings(); // establish a connection to WireMock server eagerly
        } catch (Exception ex) {
            Log.error("Cannot connect to WireMock server!", ex);
            throw ex;
        }
    }
}
