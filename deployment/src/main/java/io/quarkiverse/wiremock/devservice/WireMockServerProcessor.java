package io.quarkiverse.wiremock.devservice;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static io.quarkiverse.wiremock.devservice.WireMockConfigKey.PORT;
import static java.lang.String.format;

import java.io.IOException;
import java.io.UncheckedIOException;

import org.jboss.logging.Logger;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;

import io.quarkus.arc.deployment.ValidationPhaseBuildItem.ValidationErrorBuildItem;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.Consume;
import io.quarkus.deployment.builditem.CuratedApplicationShutdownBuildItem;
import io.quarkus.deployment.builditem.DevServicesResultBuildItem;
import io.quarkus.deployment.builditem.DevServicesResultBuildItem.RunningDevService;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.deployment.builditem.LaunchModeBuildItem;
import io.quarkus.deployment.builditem.LiveReloadBuildItem;
import io.quarkus.deployment.dev.devservices.DevServiceDescriptionBuildItem;
import io.quarkus.deployment.dev.devservices.GlobalDevServicesConfig;
import io.quarkus.runtime.configuration.ConfigurationException;

class WireMockServerProcessor {
    private static final Logger LOGGER = Logger.getLogger(WireMockServerProcessor.class);
    private static final String FEATURE_NAME = "wiremock";
    private static final String DEV_SERVICE_NAME = "WireMock";
    private static final String CONFIG_TEMPLATE = "%%dev,test.%s";
    private static final int MIN_PORT = 1025;
    private static final int MAX_PORT = 65535;
    static volatile RunningDevService devService;

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE_NAME);
    }

    @BuildStep(onlyIf = { WireMockServerEnabled.class, GlobalDevServicesConfig.Enabled.class })
    DevServicesResultBuildItem setup(LaunchModeBuildItem launchMode, LiveReloadBuildItem liveReload,
            CuratedApplicationShutdownBuildItem shutdown, WireMockServerBuildTimeConfig config,
            BuildProducer<ValidationErrorBuildItem> configErrors) {

        LOGGER.debugf("Quarkus launch mode [%s]", launchMode.getLaunchMode());

        if (isPortConfigInvalid(config)) {
            configErrors.produce(new ValidationErrorBuildItem(new ConfigurationException(
                    format("The specified port %d is not part of the permitted port range! Please specify a port between %d and %d.",
                            config.port().getAsInt(), MIN_PORT, MAX_PORT))));
            return null;
        }

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

    @BuildStep(onlyIf = { WireMockServerEnabled.class, GlobalDevServicesConfig.Enabled.class })
    @Consume(DevServicesResultBuildItem.class)
    DevServiceDescriptionBuildItem renderDevServiceDevUICard(WireMockServerBuildTimeConfig config) {
        return new DevServiceDescriptionBuildItem(DEV_SERVICE_NAME, null, devService.getConfig());
    }

    private static RunningDevService startWireMockDevService(WireMockServerBuildTimeConfig config) {

        final WireMockConfiguration configuration = options().usingFilesUnderDirectory(config.filesMapping())
                .globalTemplating(config.globalResponseTemplating());
        config.port().ifPresentOrElse(configuration::port, configuration::dynamicPort);

        final WireMockServer server = new WireMockServer(configuration);
        server.start();
        LOGGER.debugf("WireMock server listening on port [%s]", server.port());

        return new RunningDevService(DEV_SERVICE_NAME, null, server::shutdown, getPropertyKey(PORT),
                String.valueOf(server.port()));
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

    private static boolean isPortConfigInvalid(WireMockServerBuildTimeConfig config) {
        if (config.port().isEmpty()) {
            return false;
        }
        final int port = config.port().getAsInt();
        return port < MIN_PORT || port > MAX_PORT;
    }

    private static String getPropertyKey(String propertyName) {
        return format(CONFIG_TEMPLATE, propertyName);
    }
}
