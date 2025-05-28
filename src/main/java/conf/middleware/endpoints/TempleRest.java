package conf.middleware.endpoints;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("/temple")
public class TempleRest {
    /**
     *
     * @param templeId
     * @return Temple Json   object
     */

    @Path("get/{templeId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getTemple(String templeId) {
        return "Hello from Quarkus REST";
    }

    /**
     *
     * @param page
     * @param pageSize
     * @return
     */
    @Path("list/{page:\\d+}/{pageSize:\\d+}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String listTemple( int page, int pageSize) {
        return "Hello from Quarkus REST";
    }

    @Path("get/{templeId}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public String gTemple(String templeId) {
        return "Hello from Quarkus REST";
    }

}
