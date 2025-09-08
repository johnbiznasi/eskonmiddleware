package conf.middleware.endpoints;

import conf.middleware.models.Devotee;
import conf.middleware.models.Gurus;
import conf.middleware.models.forms.GuruDTO;
import conf.middleware.services.DevoteeService;
import conf.middleware.services.GurusService;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Path("/mentors")
public class Teachers {
    private static final Logger log = LoggerFactory.getLogger(Teachers.class);
    @Inject
    private DevoteeService devoteeService;
    @Inject
    private GurusService guruService;
    @Inject
    JsonWebToken jwt;

    @Path("regionteachers/{regionid}}")
    @GET
    @RolesAllowed({ "devotee","admin","mentors"})
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<List<Gurus>> getRegionMentors(String regionid) {

        return guruService.fetchTeacher(GurusService.GuruFilters.region,regionid);
    }


    @Path("allteachers")
    @GET
    //@RolesAllowed({ "devotee","admin","mentors"})
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)

    public Uni<List<Gurus>> getTeachers() {
        return this.guruService.fetchAllTeacher();
    }


    @Path("create")
    @POST
   // @RolesAllowed({ "admin"})
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Integer> createTeacher(@BeanParam GuruDTO  teacher) {
        return  this.guruService.createTeacher(teacher);
    }


    @Path("devotees")
    @GET
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<List<Devotee>> getAvailableDevotee() {
        return this.devoteeService.availableDevotees();
    }


    @Path("devotees/{teacher:\\d+}")
    @GET
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<List<Devotee>> getAvailableDevotee(int teacher) {
        log.info("Checking  devotess of  teacger>>"+teacher);
        return this.devoteeService.guruDevotees(teacher);
    }


    @Path("chat/{teacher}/{devotee}")
    @GET
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<List<Devotee>> getChats(String teacher, String devotee) {
        return this.devoteeService.availableDevotees();
    }




}
