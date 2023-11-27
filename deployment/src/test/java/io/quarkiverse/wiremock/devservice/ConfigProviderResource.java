package io.quarkiverse.wiremock.devservice;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

import org.eclipse.microprofile.config.ConfigProvider;

@Path("/quarkus/wiremock")
@ApplicationScoped
class ConfigProviderResource {

    @Path("/devservices/config")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getConfigValue(@QueryParam("name") String propertyName) {
        return ConfigProvider.getConfig().getValue(propertyName, String.class);
    }
}
