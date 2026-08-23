package io.quarkiverse.wiremock.devservice;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "farewell-api", baseUri = "https://production.example.com")
public interface FarewellApiClient {

    @GET
    @Path("/mock-me-too")
    String farewell();
}
