package io.quarkiverse.wiremock.devservice;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static io.quarkiverse.wiremock.devservice.WireMockDevServiceConfig.PORT;
import static io.quarkiverse.wiremock.devservice.WireMockDevServiceConfig.PREFIX;

import java.io.IOException;
import java.io.UncheckedIOException;

import org.jboss.logging.Logger;

import com.github.tomakehurst.wiremock.WireMockServer;

import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.CuratedApplicationShutdownBuildItem;
import io.quarkus.deployment.builditem.DevServicesResultBuildItem;
import io.quarkus.deployment.builditem.DevServicesResultBuildItem.RunningDevService;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.deployment.builditem.LaunchModeBuildItem;
import io.quarkus.deployment.builditem.LiveReloadBuildItem;
import io.quarkus.deployment.dev.devservices.GlobalDevServicesConfig;

class WireMockServerProcessor {
    private static final Logger LOGGER = Logger.getLogger(WireMockServerProcessor.class);
    private static final String FEATURE_NAME = "wiremock";
    private static final String CONFIG_TEMPLATE = "%%dev,test.%s.%s";
    static volatile RunningDevService devService;

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE_NAME);
    }

    @BuildStep(onlyIf = { WireMockServerEnabled.class, GlobalDevServicesConfig.Enabled.class })
    DevServicesResultBuildItem setup(LaunchModeBuildItem launchMode, LiveReloadBuildItem liveReload,
            CuratedApplicationShutdownBuildItem shutdown, WireMockServerConfig config) {

        LOGGER.debugf("Quarkus launch mode [%s]", launchMode.getLaunchMode());

        // register shutdown callback once
        shutdown.addCloseTask(WireMockServerProcessor::stopWireMockDevService, true);

        if (liveReload.isLiveReload() && config.reload()) {
            LOGGER.debug("Live reload triggered!");
            stopWireMockDevService();
        }

        if (devService == null) {
            devService = startWireMockDevService(config);
        }
        return devService.toBuildItem();
    }

    private static RunningDevService startWireMockDevService(WireMockServerConfig config) {

        LOGGER.debugf("Starting WireMock server with port [%s] and path [%s]", config.port(), config.filesMapping());
        final WireMockServer server = new WireMockServer(
                options().port(config.port()).usingFilesUnderDirectory(config.filesMapping()));
        server.start();

        return new RunningDevService(config.serviceName(), null, server::shutdown, getPropertyKey(PORT),
                String.valueOf(config.port()));
    }

    private static synchronized void stopWireMockDevService() {
        try {
            if (devService != null) {
                LOGGER.debugf("Stopping WireMock server running on port %s",
                        devService.getConfig().get(getPropertyKey(PORT)));
                devService.close();
            }
        } catch (IOException e) {
            LOGGER.error("Failed to stop WireMock server", e);
            throw new UncheckedIOException(e);
        } finally {
            devService = null;
        }
    }

    private static String getPropertyKey(String propertyName) {
        return String.format(CONFIG_TEMPLATE, PREFIX, propertyName);
    }

}
