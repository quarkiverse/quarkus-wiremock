package io.quarkiverse.wiremock.devservice;

/**
 * The purpose of this class is to access configuration identifiers more easily.
 */
public class WireMockConfigKey {
    static final String PREFIX = "quarkus.wiremock.devservices"; // adheres to Quarkus Dev Service naming conventions

    public static final String PORT = PREFIX + ".port";
    public static final String RELOAD = PREFIX + ".reload";
    public static final String FILES_MAPPING = PREFIX + ".files-mapping";
    public static final String GLOBAL_RESPONSE_TEMPLATING = PREFIX + ".global-response-templating";
    public static final String PROXY_MODE = PREFIX + ".proxy-mode";

    private WireMockConfigKey() {
        // do not instantiate
    }

}
