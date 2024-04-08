package io.quarkiverse.wiremock.devservice;

import static io.quarkiverse.wiremock.devservice.WireMockConfigKey.PREFIX;

import java.util.OptionalInt;

import io.quarkus.runtime.annotations.ConfigRoot;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;

@ConfigMapping(prefix = PREFIX)
@ConfigRoot
public interface WireMockServerBuildTimeConfig {

    /**
     * If Dev Services for WireMock has been explicitly enabled or disabled.
     */
    @WithDefault("true")
    boolean enabled();

    /**
     * Indicates whether WireMock server needs to be restarted after Quarkus live reload (see development mode for more
     * information) or not.
     */
    @WithDefault("true")
    boolean reload();

    /**
     * Optional fixed port the WireMock Dev Service will listen to.
     * <p>
     * If not defined, the port will be chosen randomly.
     */
    OptionalInt port();

    /**
     * Path to the WireMock configuration files.
     * If this starts with {@code classpath:} then files will be looked up on the classpath instead of the filesystem
     */
    @WithDefault("src/test/resources")
    String filesMapping();

    /**
     * If global response templating should be enabled for WireMock
     *
     * @see <a href="https://wiremock.org/3.x/docs/response-templating/">https://wiremock.org/3.x/docs/response-templating/</a>
     */
    @WithDefault("false")
    boolean globalResponseTemplating();

    /**
     * Control whether WireMock Extension <a href=
     * "https://wiremock.org/docs/extending-wiremock/#extension-registration-via-service-loading">service
     * loading</a>,
     * is enabled
     */
    @WithDefault("false")
    boolean extensionScanningEnabled();
  
    default boolean isClasspathFilesMapping() {
        return filesMapping().startsWith("classpath:");
    }

    default String effectiveFileMapping() {
        if (isClasspathFilesMapping()) {
            int index = filesMapping().indexOf(':');
            return filesMapping().substring(index + 1);
        }
        return filesMapping();
    }

}
