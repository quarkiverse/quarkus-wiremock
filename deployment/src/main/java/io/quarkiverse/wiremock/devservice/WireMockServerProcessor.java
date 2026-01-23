package io.quarkiverse.wiremock.devservice;

import static io.quarkiverse.wiremock.devservice.WireMockConfigKey.PORT;
import static java.lang.String.format;
import static java.lang.String.valueOf;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jboss.logging.Logger;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.common.ClasspathFileSource;
import com.github.tomakehurst.wiremock.common.Notifier;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;

import io.quarkus.arc.deployment.ValidationPhaseBuildItem.ValidationErrorBuildItem;
import io.quarkus.arc.processor.BuiltinScope;
import io.quarkus.deployment.IsDevelopment;
import io.quarkus.deployment.IsLocalDevelopment;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.Consume;
import io.quarkus.deployment.builditem.DevServicesResultBuildItem;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.deployment.builditem.HotDeploymentWatchedFileBuildItem;
import io.quarkus.deployment.builditem.Startable;
import io.quarkus.deployment.dev.devservices.DevServicesConfig;
import io.quarkus.devui.spi.JsonRPCProvidersBuildItem;
import io.quarkus.devui.spi.page.CardPageBuildItem;
import io.quarkus.devui.spi.page.Page;
import io.quarkus.logging.Log;
import io.quarkus.runtime.configuration.ConfigurationException;

class WireMockServerProcessor {

    private static final String FEATURE_NAME = "wiremock";
    private static final String DEV_SERVICE_NAME = "WireMock";
    private static final String MAPPINGS = "mappings";
    private static final String FILES = "__files";
    private static final int MIN_PORT = 1025;
    private static final int MAX_PORT = 65535;

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE_NAME);
    }

    @BuildStep(onlyIf = { WireMockServerEnabled.class, DevServicesConfig.Enabled.class })
    DevServicesResultBuildItem setup(WireMockServerBuildTimeConfig config,
            BuildProducer<ValidationErrorBuildItem> configErrors) {

        if (isPortConfigInvalid(config)) {
            configErrors.produce(new ValidationErrorBuildItem(new ConfigurationException(
                    format("The specified port %d is not part of the permitted port range! Please specify a port between %d and %d.",
                            config.port().getAsInt(), MIN_PORT, MAX_PORT))));
            return null;
        }

        return DevServicesResultBuildItem.owned()
                .name(FEATURE_NAME)
                .serviceName(DEV_SERVICE_NAME)
                .serviceConfig(config)
                .startable(() -> new WireMockStartable(config))
                .configProvider(Map.of(PORT, s -> valueOf(s.getExposedPort())))
                .build();
    }

    @BuildStep(onlyIf = { WireMockServerEnabled.class, DevServicesConfig.Enabled.class, IsDevelopment.class })
    void watchWireMockConfigFiles(WireMockServerBuildTimeConfig config,
            BuildProducer<HotDeploymentWatchedFileBuildItem> items) {

        if (!config.isClasspathFilesMapping()) {
            listFiles(Paths.get(config.effectiveFileMapping(), MAPPINGS),
                    Paths.get(config.effectiveFileMapping(), FILES)).forEach(file -> {
                        Log.debugf("Watching [%s] for hot deployment!", file);
                        items.produce(new HotDeploymentWatchedFileBuildItem(file));
                    });
        }
    }

    @BuildStep(onlyIf = { WireMockServerEnabled.class, DevServicesConfig.Enabled.class, IsLocalDevelopment.class })
    public JsonRPCProvidersBuildItem createJsonRPCService() {
        return new JsonRPCProvidersBuildItem(WireMockRCPService.class, BuiltinScope.SINGLETON.getName());
    }

    @BuildStep(onlyIf = { WireMockServerEnabled.class, DevServicesConfig.Enabled.class, IsLocalDevelopment.class })
    @Consume(DevServicesResultBuildItem.class)
    public CardPageBuildItem pages() {
        CardPageBuildItem cardPageBuildItem = new CardPageBuildItem();
        cardPageBuildItem.addPage(Page.externalPageBuilder("Mappings")
                .dynamicUrlJsonRPCMethodName("getMappingsUrl")
                .doNotEmbed()
                .icon("font-awesome-solid:file-code"));
        cardPageBuildItem.addLibraryVersion("org.wiremock", "wiremock-standalone", "WireMock", "https://wiremock.org/");
        return cardPageBuildItem;
    }

    public static class WireMockStartable implements Startable {

        private WireMockServer server;

        private final WireMockServerBuildTimeConfig config;

        public WireMockStartable(WireMockServerBuildTimeConfig config) {
            this.config = config;
        }

        @Override
        public void start() {
            final WireMockConfiguration configuration = WireMockConfiguration.options()
                    .globalTemplating(config.globalResponseTemplating())
                    .extensionScanningEnabled(config.extensionScanningEnabled())
                    .notifier(new JBossNotifier());
            config.port().ifPresentOrElse(configuration::port, configuration::dynamicPort);

            if (config.isClasspathFilesMapping()) {
                configuration.fileSource(new ClasspathFileSource(Thread.currentThread().getContextClassLoader(),
                        config.effectiveFileMapping()));
            } else {
                configuration.usingFilesUnderDirectory(config.effectiveFileMapping());
            }

            server = new WireMockServer(configuration);

            server.start();
            Log.debugf("WireMock server listening on port [%s]", server.port());
        }

        public int getExposedPort() {
            return server.port();
        }

        @Override
        public String getConnectionInfo() {
            return server.baseUrl();
        }

        @Override
        public String getContainerId() {
            return null;
        }

        @Override
        public void close() {
            if (server != null) {
                Log.debugf("Stopping WireMock server running on port [%s]", server.port());
                server.stop();
            }
        }
    }

    private static boolean isPortConfigInvalid(WireMockServerBuildTimeConfig config) {
        if (config.port().isEmpty()) {
            return false;
        }
        final int port = config.port().getAsInt();
        return port < MIN_PORT || port > MAX_PORT;
    }

    private static Set<String> listFiles(Path... dirs) {
        return Arrays.stream(dirs).filter(Files::isDirectory).map(WireMockServerProcessor::fileWalk)
                .flatMap(Set::stream).collect(Collectors.toSet());
    }

    private static Set<String> fileWalk(Path start) {
        try (Stream<Path> stream = Files.walk(start)) {
            return stream.filter(Files::isRegularFile).map(Path::toAbsolutePath).map(Path::toString)
                    .collect(Collectors.toSet());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static class JBossNotifier implements Notifier {
        private static final Logger LOGGER = Logger
                .getLogger(WireMockServerProcessor.class.getPackageName() + ".WireMockServer");

        @Override
        public void info(String message) {
            LOGGER.info(message);
        }

        @Override
        public void error(String message) {
            LOGGER.error(message);
        }

        @Override
        public void error(String message, Throwable t) {
            LOGGER.error(message, t);
        }
    }
}
