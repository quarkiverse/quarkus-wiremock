package io.quarkiverse.wiremock.devservice;

import static io.quarkiverse.wiremock.devservice.WireMockDevServiceConfig.PREFIX;

import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;

@ConfigMapping(prefix = PREFIX)
@ConfigRoot(phase = ConfigPhase.BUILD_TIME)
public interface WireMockServerConfig {
    /**
     * Name of the WireMock Dev Service.
     */
    @WithDefault("wiremock-server")
    String serviceName();

    /**
     * If Dev Services for WireMock has been explicitly enabled or disabled.
     */
    @WithDefault("true")
    boolean enabled();

    /**
     * Indicates whether WireMock server needs to be restarted after Quarkus live reload (see development mode for more
     * information) or not.
     */
    @WithDefault("false")
    boolean reload();

    /**
     * Static fixed port of the WireMock server started via Dev Services.
     */
    @WithDefault("8089")
    int port();

    /**
     * Path of the WireMock configuration files
     */
    @WithDefault("src/test/resources")
    String filesMapping();
}
