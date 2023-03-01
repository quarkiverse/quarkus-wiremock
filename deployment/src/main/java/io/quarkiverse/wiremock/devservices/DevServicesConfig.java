package io.quarkiverse.wiremock.devservices;

import java.util.Objects;
import java.util.Optional;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.quarkus.runtime.annotations.ConfigItem;

@ConfigGroup
public class DevServicesConfig {

    /**
     * If DevServices has been explicitly enabled or disabled. DevServices is generally enabled
     * by default, unless there is an existing configuration present.
     * <p>
     * When DevServices is enabled Quarkus will attempt to automatically configure and start
     * a database when running in Dev or Test mode and when Docker is running.
     */
    @ConfigItem(name = "enabled", defaultValue = "true")
    public boolean enabled;

    /**
     * If Wiremock needs to be reloaded after quarkus reload in development mode
     */
    @ConfigItem(defaultValue = "false")
    public boolean reload;

    /**
     * Optional fixed port the dev service will listen to.
     * <p>
     * If not defined, the port will be 8089
     */
    @ConfigItem(name = "port")
    public Optional<Integer> port;

    /**
     * Devservice name. default to wiremock-server
     */
    @ConfigItem(name = "service-name", defaultValue = "wiremock-server")
    public String serviceName;

    /**
     * Path of the wiremock configuration files
     * default is src/main/resources
     */
    @ConfigItem(name = "files-mapping", defaultValue = "src/main/resources")
    public String filesMapping;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        DevServicesConfig that = (DevServicesConfig) o;
        return enabled == that.enabled &&
                Objects.equals(port, that.port) &&
                Objects.equals(serviceName, that.serviceName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(enabled, port, serviceName);
    }
}
