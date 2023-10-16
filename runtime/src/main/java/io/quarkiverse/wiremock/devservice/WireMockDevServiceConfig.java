package io.quarkiverse.wiremock.devservice;

/**
 * The purpose of this class is to share configuration identifiers between the deployment and test module.
 * Although the definitions are placed in the runtime module, it's not the intention to make these configurations
 * editable at runtime.
 */
public class WireMockDevServiceConfig {
    public static final String PREFIX = "quarkus.wiremock.devservices";
    public static final String PORT = "port";

    private WireMockDevServiceConfig() {
        // do not instantiate
    }

}
