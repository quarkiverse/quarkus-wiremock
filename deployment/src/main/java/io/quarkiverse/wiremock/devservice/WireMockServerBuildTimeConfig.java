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
     * Restart WireMock Dev Service whenever Quarkus is reloaded. Otherwise, whenever files are changed in the
     * {@code files-mapping} location you would need to restart the Dev Service (WireMock server) manually.
     */
    @WithDefault("true")
    boolean reload();

    /**
     * Optional fixed port the WireMock Dev Service will listen to. If not defined, the port will be chosen randomly.
     * <p>
     * <b>WARNING:</b> Only ports between 1025 and 65535 are permitted.
     */
    OptionalInt port();

    /**
     * Path to the WireMock configuration files (root dir which contains the {@code mappings} and {@code __files} folders).
     * If this starts with {@code classpath:} then files will be looked up on the classpath instead of the filesystem.
     */
    @WithDefault("src/test/resources")
    String filesMapping();

    /**
     * Response templating is enabled by default in WireMock 3, with this setting response templating can be enabled globally.
     * <p>
     * Please refer to <a href="https://wiremock.org/3.x/docs/response-templating/">Response Templating</a> for more details.
     */
    @WithDefault("false")
    boolean globalResponseTemplating();

    /**
     * Control whether WireMock Extension <a href=
     * "https://wiremock.org/docs/extending-wiremock/#extension-registration-via-service-loading">service
     * loading</a>,
     * is enabled.
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
