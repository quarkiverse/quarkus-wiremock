package io.quarkiverse.wiremock.devservice;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "greeting-api", baseUri = "https://production.example.com")
public interface GreetingApiClient {

    @GET
    @Path("/mock-me")
    String greet();
}
