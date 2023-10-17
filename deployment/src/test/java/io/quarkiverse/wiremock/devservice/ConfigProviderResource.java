package io.quarkiverse.wiremock.devservice;

import static io.quarkiverse.wiremock.devservice.WireMockDevServiceConfig.PREFIX;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import org.eclipse.microprofile.config.ConfigProvider;

@Path("/quarkus/wiremock")
@ApplicationScoped
class ConfigProviderResource {

    @Path("/devservices/{property}")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getValue(@PathParam("property") String property) {
        return ConfigProvider.getConfig().getValue(PREFIX + "." + property, String.class);
    }
}
