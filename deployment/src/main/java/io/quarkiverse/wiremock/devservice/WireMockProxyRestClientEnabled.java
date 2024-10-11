package io.quarkiverse.wiremock.devservice;

import java.util.function.BooleanSupplier;

public class WireMockProxyRestClientEnabled implements BooleanSupplier {
    WireMockServerBuildTimeConfig config;

    @Override
    public boolean getAsBoolean() {
        return config.proxyRestClient();
    }
}
