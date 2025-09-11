package conf.middleware.endpoints;

import conf.middleware.models.LocationInfo;
import conf.middleware.models.Temple;
import conf.middleware.models.forms.TempleForm;
import conf.middleware.services.TempleService;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/temple")
public class TempleRest {


    @Inject
    TempleService templeservice;


    @Path("regiontemples/{regionid}}")
    @GET
    @RolesAllowed({ "devotee","admin","mentors"})
    @Produces(MediaType.APPLICATION_JSON)
    public String getRegionTemple(String templeId) {
        return "Hello from Quarkus REST";
    }

    @Path("alltemples")
    @GET
   // @RolesAllowed({ "devotee","admin","mentors"})
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<List<Temple>> getTemples() {
        return this.templeservice.fetchAllTemples();
    }


    @Path("create")
    @POST
   // @RolesAllowed({ "admin"})
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Temple> createTempe(@BeanParam TempleForm form) {
     return  templeservice.createTemple(new Temple(form.getName(), "",
                new LocationInfo(0.0, 0.0, "", "", "", form.getRegion())));


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
