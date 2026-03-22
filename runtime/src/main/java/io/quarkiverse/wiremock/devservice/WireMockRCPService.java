package io.quarkiverse.wiremock.devservice;

import static io.quarkiverse.wiremock.devservice.WireMockConfigKey.PORT;

import org.eclipse.microprofile.config.ConfigProvider;

public class WireMockRCPService {
    public String getMappingsUrl() {
        return "http://localhost:" + ConfigProvider.getConfig().getValue(PORT, Integer.class) + "/__admin/mappings";
    }
}
