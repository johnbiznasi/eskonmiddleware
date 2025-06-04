package conf.middleware.endpoints;

import conf.middleware.models.dtos.GuruDTO;
import conf.middleware.services.GurusService;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.RestPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/register")
public class AccountSetup {
    private static final Logger log = LoggerFactory.getLogger(AccountSetup.class);
    @Inject
    private GurusService regservice;
    public static class DevoteeAccountSetup {
        @RestPath
        String type;

        @RestForm
        String username;
        @RestForm
        String password;
    }


    @Path("/teacher")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<Integer> createTeacherAccount(@BeanParam GuruDTO newguru) {
        newguru.setStatus("new_account");
        return this.regservice.insertMentor(newguru);

    }

    @Path("/teacher/{userid:\\d+}")
    @PUT
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<Boolean> updateTeacherAccount(@BeanParam GuruDTO newguru,int userid) {
        newguru.setId(userid);
        return this.regservice.updateMentor(newguru);

    }
    @Path("/teacher/{userid:\\d+}")
    @DELETE
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<Boolean> deleteTeacherAccount(int  userid) {
               return this.regservice.deleteMentor(userid);

    }


    @Path("devotee")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public String createAccount(@BeanParam DevoteeAccountSetup parameters) {
        return "Hello from Quarkus REST";
    }

}
