package io.quarkiverse.wiremock.it;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.ext.web.client.HttpResponse;
import io.vertx.mutiny.ext.web.client.WebClient;

@Path("/wiremock-dev")
@ApplicationScoped
public class WiremockDevResource {
    @Inject
    Vertx vertx;

    @Context
    private UriInfo uriInfo;

    @Context
    private HttpHeaders headers;

    @ConfigProperty(name = "quarkus.wiremock.devservices.port")
    String wireMockPort;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<String> hello() {
        WebClient client = WebClient.create(vertx);

        return client.getAbs(String.format("http://localhost:%s/wiremock-dev", wireMockPort)).send().onItem()
                .transform(HttpResponse::bodyAsString);
    }
}
