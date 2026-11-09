package io.quarkiverse.wiremock.devservice;

import static io.quarkiverse.wiremock.devservice.WireMockConfigKey.PREFIX;

import java.util.Optional;

import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;
import io.smallrye.config.ConfigMapping;

/**
 * Connection details published by the WireMock Dev Service. These are set automatically and are meant to be referenced
 * from other configuration properties, e.g. {@code quarkus.rest-client.my-client.url=${quarkus.wiremock.devservices.url}}.
 */
@ConfigMapping(prefix = PREFIX)
@ConfigRoot(phase = ConfigPhase.RUN_TIME)
public interface WireMockServerRuntimeConfig {

    /**
     * Host name under which the application can reach the WireMock server. This is {@code localhost}, unless the
     * application runs in a container during integration tests, in which case the host alias of the container runtime
     * is used ({@code host.docker.internal} or {@code host.containers.internal}).
     */
    Optional<String> host();

    /**
     * Base URL under which the application can reach the WireMock server, e.g. {@code http://localhost:8089}.
     */
    Optional<String> url();
}
