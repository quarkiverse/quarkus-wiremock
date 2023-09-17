package io.quarkiverse.wiremock.devservice;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static io.quarkiverse.wiremock.devservice.WireMockConfig.PORT;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Map;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

import org.jboss.logging.Logger;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;

import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.CuratedApplicationShutdownBuildItem;
import io.quarkus.deployment.builditem.DevServicesResultBuildItem;
import io.quarkus.deployment.builditem.DevServicesResultBuildItem.RunningDevService;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.deployment.builditem.LaunchModeBuildItem;
import io.quarkus.deployment.builditem.LiveReloadBuildItem;
import io.quarkus.deployment.dev.devservices.GlobalDevServicesConfig;

class WireMockDevProcessor {
    private static final Logger LOGGER = Logger.getLogger(WireMockDevProcessor.class);
    private static final String FEATURE_NAME = "wiremock";
    static volatile RunningDevService devService;

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE_NAME);
    }

    @BuildStep(onlyIf = { IsEnabled.class, GlobalDevServicesConfig.Enabled.class })
    DevServicesResultBuildItem setup(LaunchModeBuildItem launchMode, LiveReloadBuildItem liveReload,
            CuratedApplicationShutdownBuildItem shutdown, WireMockBuildTimeConfig wireMockBuildTimeConfig) {

        LOGGER.debugf("Quarkus launch mode [%s]", launchMode.getLaunchMode());
        final WireMockDevServicesBuildTimeConfig config = wireMockBuildTimeConfig.devservices;

        // register shutdown callback once
        shutdown.addCloseTask(WireMockDevProcessor::stopWireMockDevService, true);

        if (liveReload.isLiveReload() && config.reload) {
            LOGGER.debug("Live reload triggered!");
            stopWireMockDevService();
        }

        if (devService == null) {
            devService = startWireMockDevService(config);
        }
        return devService.toBuildItem();
    }

    private static RunningDevService startWireMockDevService(WireMockDevServicesBuildTimeConfig config) {

        LOGGER.debugf("Starting WireMock server with port [%s] and path [%s]", config.port, config.filesMapping);
        final WireMockConfiguration configuration = options().port(config.port).usingFilesUnderDirectory(config.filesMapping);

        final WireMockServer server = new WireMockServer(configuration);
        server.start();

        final Supplier<RunningDevService> supplier = () -> new RunningDevService(config.serviceName, null, server::shutdown,
                extractPort(config));

        return supplier.get();
    }

    private static Map<String, String> extractPort(WireMockDevServicesBuildTimeConfig config) {
        return Map.of(PORT, String.valueOf(config.port));
    }

    private static void stopWireMockDevService() {
        try {
            if (devService != null) {
                LOGGER.debugf("Stopping WireMock server on port %s", devService.getConfig().get(PORT));
                devService.close();
            }
        } catch (IOException e) {
            LOGGER.error("Failed to stop WireMock server", e);
            throw new UncheckedIOException(e);
        } finally {
            devService = null;
        }
    }

    public static class IsEnabled implements BooleanSupplier {
        WireMockBuildTimeConfig config;

        public boolean getAsBoolean() {
            return config.devservices.enabled;
        }
    }

}
