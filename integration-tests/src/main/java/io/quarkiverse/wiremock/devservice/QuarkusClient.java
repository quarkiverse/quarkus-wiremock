package io.quarkiverse.wiremock.devservice;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/mock-rest-client")
@RegisterRestClient(baseUri = "https://imaginary-host.io/", configKey = "quarkus-client")
public interface QuarkusClient {

    record User(String name, String email) {
    }

    @GET
    @Consumes("application/json")
    User test();
}
