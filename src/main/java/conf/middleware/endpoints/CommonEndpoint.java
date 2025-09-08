package conf.middleware.endpoints;

import conf.middleware.models.forms.RegionDTO;
import conf.middleware.services.CommonService;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.RestPath;

import java.util.List;

@Path("/commonroute")
public class CommonEndpoint {
    public static class RegionParameter {
        @RestPath
        String type;

        @RestForm
        String name;
        @RestForm
        String country;

        public String getType() {
            return type;
        }

        public String getName() {
            return name;
        }

        public String getCountry() {
            return country;
        }
    }

    @Inject
    private CommonService common;

    @Path("/regions")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<List<RegionDTO>>  getRegions() {
return common.fetchAllRegions();
    }


    @Path("/create_region")

    @Produces(MediaType.APPLICATION_JSON)
    @POST
    public Uni<RegionDTO> createRegions(RegionParameter regions) {
       return common.createRegion(regions);

    }

}
