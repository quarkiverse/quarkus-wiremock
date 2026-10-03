package io.quarkiverse.wiremock.devservice;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

import org.eclipse.microprofile.rest.client.inject.RestClient;

@Path("/greeting")
public class GreetingResource {

    @RestClient
    GreetingApiClient greetingApiClient;

    @GET
    public String greet() {
        // calls WireMock from within the application under test, which may run in a container
        return greetingApiClient.greet();
    }
}
