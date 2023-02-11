package io.quarkiverse.wiremock.it;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.ext.web.client.HttpResponse;
import io.vertx.mutiny.ext.web.client.WebClient;
import io.vertx.mutiny.ext.web.codec.BodyCodec;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.*;

@Path("/wiremock-dev")
@ApplicationScoped
public class WiremockDevResource {
    @Context
    private UriInfo uriInfo;

    @Context
    private HttpHeaders headers;

    @Inject
    Vertx vertx;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<String> hello() {
        WebClient client = WebClient.create(vertx);

        return client.getAbs("http://localhost:8089/wiremock-dev").send().onItem()
                .transform(HttpResponse::bodyAsString);
    }
}
