
package io.quarkiverse.wiremock.it;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/wiremock-dev")
@ApplicationScoped
public class WiremockDevResource {
    // add some rest methods here

    @GET
    public String hello() {
        return "Hello wiremock-dev";
    }
}
