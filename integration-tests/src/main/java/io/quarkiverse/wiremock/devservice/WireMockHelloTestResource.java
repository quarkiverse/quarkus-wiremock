package io.quarkiverse.wiremock.devservice;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

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
