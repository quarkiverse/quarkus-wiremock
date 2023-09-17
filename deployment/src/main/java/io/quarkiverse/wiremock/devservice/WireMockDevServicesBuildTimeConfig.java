package io.quarkiverse.wiremock.devservice;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.quarkus.runtime.annotations.ConfigItem;

@ConfigGroup
public class WireMockDevServicesBuildTimeConfig {
    static final String DEFAULT_DEV_SERVICE_NAME = "wiremock-server";
    static final String DEFAULT_PORT = "8089";

    /**
     * Name of the WireMock Dev Service.
     */
    @ConfigItem(name = "service-name", defaultValue = DEFAULT_DEV_SERVICE_NAME)
    String serviceName;

    /**
     * If Dev Services for WireMock has been explicitly enabled or disabled.
     */
    @ConfigItem(defaultValue = "true")
    boolean enabled;

    /**
     * Indicates whether WireMock server needs to be restarted after Quarkus live reload (see development mode for more
     * information) or not.
     */
    @ConfigItem(defaultValue = "false")
    boolean reload;

    /**
     * Static fixed port of the WireMock server started via Dev Services.
     */
    @ConfigItem(defaultValue = DEFAULT_PORT)
    int port;

    /**
     * Path of the WireMock configuration files
     */
    @ConfigItem(name = "files-mapping", defaultValue = "src/test/resources")
    String filesMapping;
}
