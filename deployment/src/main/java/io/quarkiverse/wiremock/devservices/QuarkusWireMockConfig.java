package io.quarkiverse.wiremock.devservices;

import java.util.Objects;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConfigRoot;

@ConfigRoot(name = "wiremock")
public class QuarkusWireMockConfig {

    /**
     * Default Dev services configuration.
     */
    @ConfigItem(name = ConfigItem.PARENT)
    public DevServiceConfiguration defaultDevService;

    @ConfigGroup
    public static class DevServiceConfiguration {
        /**
         * Configuration for DevServices
         * <p>
         * DevServices allows Quarkus to automatically start MockServer in dev and test mode.
         */
        @ConfigItem
        public DevServicesConfig devservices;

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            DevServiceConfiguration that = (DevServiceConfiguration) o;
            return Objects.equals(devservices, that.devservices);
        }

        @Override
        public int hashCode() {
            return Objects.hash(devservices);
        }
    }

}
