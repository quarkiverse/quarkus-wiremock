package io.quarkiverse.wiremock.deployment;

import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConfigRoot;

@ConfigRoot(name = "wiremock")
public class QuarkusWireMockConfig {

    /**
     * WireMock is enabled and will be started in DEVELOPMENT mode.
     */
    @ConfigItem(defaultValue = "true")
    public Boolean enabled;

    /**
     * If Wiremock needs to be reloaded after quarkus reload
     */
    @ConfigItem(defaultValue = "false")
    public Boolean reload;

    /**
     * Path of the wiremock configuration files
     */
    @ConfigItem(defaultValue = "src/main/resources")
    public String path;

    /**
     * Wiremock server port
     *
     * default=8081
     */
    @ConfigItem(defaultValue = "8082")
    public Integer port;

    /**
     * The host WireMock is running, if this is not localhost this tool
     * tries to connect towards it and never start own.
     *
     * default=localhost
     */
    @ConfigItem(defaultValue = "localhost")
    public String host;

}
