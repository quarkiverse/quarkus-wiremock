package io.quarkiverse.wiremock.devservice;

import static io.quarkiverse.wiremock.devservice.WireMockConfig.CONFIG_ROOT_FQN;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.config.ConfigProvider;

@Path("/quarkus/wiremock")
@ApplicationScoped
class ConfigProviderResource {

    @Path("/devservices/{property}")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getValue(@PathParam("property") String property) {
        return ConfigProvider.getConfig().getOptionalValue(CONFIG_ROOT_FQN + "." + property, String.class)
                .orElseThrow(NotFoundException::new);
    }
}
