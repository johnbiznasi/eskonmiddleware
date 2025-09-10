package conf.middleware.endpoints;

import conf.middleware.models.AccountStatus;
import conf.middleware.models.Devotee;
import conf.middleware.models.MobileAccount;
import conf.middleware.models.forms.GuruDTO;
import conf.middleware.services.DevoteeService;
import conf.middleware.services.GurusService;
import conf.middleware.services.JwtGenerator;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.reactive.RestForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashSet;

@Path("/register")
public class AccountSetup {
    private static final Logger log = LoggerFactory.getLogger(AccountSetup.class);
    @Inject
    private GurusService regservice;
    @Inject
    private DevoteeService devservice;
    public static class DevoteeAccountSetup {


        @RestForm
        String publickey;
        @RestForm
        String deviceid;
    }



    @Path("/teacher")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<Integer> createTeacherAccount(@BeanParam GuruDTO newguru) {
        newguru.setStatus("new_account");
        return this.regservice.createTeacher(newguru);

    }

    @Path("/teacher/{userid:\\d+}")
    @PUT
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<Boolean> updateTeacherAccount(@BeanParam GuruDTO newguru,int userid) {
        newguru.setId(userid);
        return this.regservice.updateTeacher(newguru);

    }
    @Path("/teacher/{userid:\\d+}")
    @DELETE
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<Boolean> deleteTeacherAccount(int  userid) {
               return this.regservice.deleteTeacher(userid);

    }


    @Path("devotee")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<MobileAccount> createAccount(@BeanParam DevoteeAccountSetup parameters) {


      return   this.devservice.createDevotee(new Devotee("",new Date().toString(), AccountStatus.ACTIVE,parameters.publickey)).
                onItem().transform((clientid)->{
                    HashSet<String> roles=  new HashSet<>();
                    roles.add("devotee");
                    String token = JwtGenerator.generate("devotee_"+clientid,roles,clientid+"");
                    return new MobileAccount(token,clientid+"");
                });



    }




}
