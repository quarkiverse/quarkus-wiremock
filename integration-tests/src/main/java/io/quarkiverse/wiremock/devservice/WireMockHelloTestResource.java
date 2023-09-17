package io.quarkiverse.wiremock.devservice;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import io.smallrye.mutiny.Uni;

@Path("/")
@ApplicationScoped
public class WireMockHelloTestResource {

    @RestClient
    RestClientService serviceToBeMocked;

    @GET
    @Path("/hello")
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<String> hello() {
        return serviceToBeMocked.get(); // this call needs to be mocked
    }
}
