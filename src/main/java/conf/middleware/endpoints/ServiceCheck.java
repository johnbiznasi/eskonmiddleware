package conf.middleware.endpoints;

import io.vertx.mutiny.sqlclient.Pool;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/hello")
public class ServiceCheck {

    @Inject
    private  Pool client;
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {




        return "Hello from Quarkus RESTmtttukii";
    }
}
