package io.quarkiverse.wiremock.devservice;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import io.smallrye.mutiny.Uni;

@RegisterRestClient(configKey = "wiremock-server")
public interface RestClientService {

    @GET
    @Path("/mock-me")
    Uni<String> get();

}
