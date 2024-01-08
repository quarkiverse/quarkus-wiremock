package io.quarkiverse.wiremock.devservice;

import static io.quarkiverse.wiremock.devservice.ConfigProviderResource.BASE_URL;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.eclipse.microprofile.config.ConfigProvider;

@Path(BASE_URL)
@ApplicationScoped
class ConfigProviderResource {

    static final String BASE_URL = "/quarkus/wiremock/devservices";

    @GET
    @Path("/reload")
    public Response reload() {
        return Response.ok().build();
    }

    @GET
    @Path("/config")
    @Produces(MediaType.TEXT_PLAIN)
    public String getConfigValue(@QueryParam("propertyName") String propertyName) {
        return ConfigProvider.getConfig().getValue(propertyName, String.class);
    }
}
