package io.quarkiverse.wiremock.test;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.config.ConfigProvider;

@Path("/wiremock")
@ApplicationScoped
class RetrieveConfigValueResource {

    @Path("config/{name}/value")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getValue(@PathParam("name") String name) {
        return ConfigProvider.getConfig().getOptionalValue("quarkus.wiremock.devservices." + name, String.class)
                .orElse("");
    }
}
