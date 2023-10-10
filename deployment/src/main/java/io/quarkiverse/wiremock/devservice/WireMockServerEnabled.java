package io.quarkiverse.wiremock.devservice;

import java.util.function.BooleanSupplier;

public class WireMockServerEnabled implements BooleanSupplier {
    private final WireMockServerConfig config;

    public WireMockServerEnabled(WireMockServerConfig config) {
        this.config = config;
    }

    @Override
    public boolean getAsBoolean() {
        return config.enabled();
    }
}
