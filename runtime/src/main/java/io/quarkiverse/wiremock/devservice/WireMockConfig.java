package io.quarkiverse.wiremock.devservice;

public class WireMockConfig {

    private WireMockConfig() {
        // do not instantiate
    }

    static final String APP_PROPERTIES = "application.properties";

    static final String CONFIG_ROOT_FQN = "quarkus.wiremock.devservices";

    public static final String PORT = CONFIG_ROOT_FQN + ".port";
}
