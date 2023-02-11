package io.quarkiverse.wiremock.devservices;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

import java.io.IOException;
import java.util.Map;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

import org.jboss.logging.Logger;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.global.GlobalSettings;

import io.quarkiverse.wiremock.runtime.WireMockServerConfig;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.CuratedApplicationShutdownBuildItem;
import io.quarkus.deployment.builditem.DevServicesResultBuildItem;
import io.quarkus.deployment.builditem.DevServicesResultBuildItem.RunningDevService;
import io.quarkus.deployment.builditem.LaunchModeBuildItem;
import io.quarkus.deployment.builditem.LiveReloadBuildItem;
import io.quarkus.deployment.dev.devservices.GlobalDevServicesConfig;

class QuarkusWireMockDevProcessor {
    private static final String WIREMOCK_HOST = "localhost";
    private static final String DEV_SERVICE_NAME = "wiremock-service";
    private static final Logger LOGGER = Logger.getLogger(QuarkusWireMockDevProcessor.class);
    static volatile RunningDevService devService;
    static volatile WireMockServer server;

    static volatile WireMock client;

    @BuildStep(onlyIf = { IsEnabled.class, GlobalDevServicesConfig.Enabled.class })
    DevServicesResultBuildItem setup(LaunchModeBuildItem launchMode,
            LiveReloadBuildItem liveReload,
            CuratedApplicationShutdownBuildItem shutdown,
            QuarkusWireMockConfig config, GlobalDevServicesConfig devServicesConfig) {

        LOGGER.debug("Launchmode [" + launchMode + "]");
        QuarkusWireMockConfig.DevServiceConfiguration currentDevServicesConfiguration = config.defaultDevService;

        if (!liveReload.isLiveReload()) {
            Runnable closeTask = () -> {
                if (devService != null) {
                    shutdownDevService(config.defaultDevService);
                }
                devService = null;
                server = null;
            };
            shutdown.addCloseTask(closeTask, true);
        } else if (currentDevServicesConfiguration.devservices.reload) {
            LOGGER.debug("Destroying and starting Wiremock");
            shutdownDevService(config.defaultDevService);
        }

        if (devService != null) {
            return devService.toBuildItem();
        }

        devService = startWireMock(config.defaultDevService);
        return devService.toBuildItem();

    }

    private RunningDevService startWireMock(QuarkusWireMockConfig.DevServiceConfiguration config) {

        try {
            LOGGER.debug("Check if wiremock instance is running on port");
            client = new WireMock(WIREMOCK_HOST, config.devservices.port);
            GlobalSettings globalSettings = client.getGlobalSettings();

        } catch (Exception ex) {
            LOGGER.debug("No WireMock instance found");
            client = null;
        }

        if (client == null) {
            LOGGER.debug("Starting WireMockServer with port [" + config.devservices.port + "] " + "and path ["
                    + config.devservices.filesMapping + "]");
            WireMockConfiguration configuration = options()
                    .port(config.devservices.port)
                    .usingFilesUnderDirectory(config.devservices.filesMapping);

            server = new WireMockServer(configuration);
            server.start();

            client = new WireMock(WIREMOCK_HOST, config.devservices.port);
        }

        final Supplier<RunningDevService> supplier = () -> {
            try {
                return new RunningDevService(DEV_SERVICE_NAME,
                        null,
                        client::shutdown,
                        prepareConfiguration(config));
            } catch (Throwable ex) {
                throw new RuntimeException(ex);
            }
        };

        return supplier.get();
    }

    private Map<String, String> prepareConfiguration(QuarkusWireMockConfig.DevServiceConfiguration config) {
        return Map.of(
                WireMockServerConfig.PORT, "" + config.devservices.port);
    }

    private void shutdownDevService(QuarkusWireMockConfig.DevServiceConfiguration config) {
        try {
            devService.close();
        } catch (IOException e) {
            LOGGER.error("Failed to stop WireMock server", e);
        }

        // Wait till the wiremock instance is shutdown.
        try {

            client = new WireMock(WIREMOCK_HOST, config.devservices.port);
            client.resetToDefaultMappings();
            int retries = 20;
            while (retries > 0) {
                retries--;
                client.getGlobalSettings();
                Thread.sleep(100);
            }
        } catch (Exception ignored) {
        }

        client = null;
        devService = null;
        server = null;
    }

    public static class IsEnabled implements BooleanSupplier {
        QuarkusWireMockConfig config;

        public boolean getAsBoolean() {
            if (config.defaultDevService.devservices.enabled) {
                return true;
            } else {
                return false;
            }
        }
    }

}
