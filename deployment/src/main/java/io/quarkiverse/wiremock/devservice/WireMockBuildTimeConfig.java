package io.quarkiverse.wiremock.devservice;

import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;

@ConfigRoot(name = "wiremock", phase = ConfigPhase.BUILD_TIME)
public class WireMockBuildTimeConfig {

    /**
     * Configuration for DevServices. DevServices allows Quarkus to automatically start WireMock in dev and test mode.
     */
    @ConfigItem
    public WireMockDevServicesBuildTimeConfig devservices;

}
