package io.quarkiverse.wiremock.devservice;

/**
 * The purpose of this class is to access configuration identifiers more easily.
 */
public class WireMockConfigKey {
    static final String PREFIX = "quarkus.wiremock.devservices";
    public static final String PORT = PREFIX + ".port";

    private WireMockConfigKey() {
        // do not instantiate
    }

}
