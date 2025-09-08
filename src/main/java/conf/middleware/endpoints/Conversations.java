package conf.middleware.endpoints;

import conf.middleware.models.ConversationsHeads;
import conf.middleware.models.Gurus;
import conf.middleware.models.Message;
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

@Path("/devotee")
public class Conversations {
    private static final Logger log = LoggerFactory.getLogger(Conversations.class);
    @Inject
    private DevoteeService devoteeService;
    @Inject
    private GurusService guruService;
    @Inject
    JsonWebToken jwt;


    @Path("/conversations/{userid}")
    @GET
    @RolesAllowed({ "devotee","admin" })
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<List<ConversationsHeads>> GETDevoteeConverations(int userid) {
       return this.devoteeService.devoteeConversations(userid);

    }

    @Path("conversations/start/{userid}/{teacherid}")
    @GET

    @Produces(MediaType.TEXT_PLAIN)
    public Uni<Integer> startConveration(int  userid, int teacherid) {
        return this.devoteeService.startConversation(userid,teacherid);

    }

    @Path("/conversations/get/{userid}/{teacherid}")
    @GET

    @Produces(MediaType.TEXT_PLAIN)
    public Uni<List<Message>> getConverations(int userid, int teacherid) {
        return this.devoteeService.converationMessages(userid,teacherid);

    }




    @Path("teachers/{county}")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<List<Gurus>> getTeachers(String country) {
return this.guruService.fetchTeacher(GurusService.GuruFilters.country,country);

    }

    @Path("devotee_teachers/{profile_id}")
    @POST

    //@RolesAllowed({ "devotee","admin","mentors"})
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)

    public Uni<List<Gurus>> devoteeTeachers(int profile_id) {
        log.info("the profile id is"+profile_id);
        return this.devoteeService.devotesGuru(profile_id).onItem().transformToUni(row->{

            if(row.size()==0){
                log.info(row.size()+"no Teachers  found...fetcj-hing allgurus");
                //return all   teachers
                 return  guruService.fetchAllTeacher();
            }else {
                log.info(row.size()+"Teachers  found...");
                return guruService.fetchTeacher(GurusService.GuruFilters.id,row.getFirst().getMentorId());
            }
        });
    }




    @Path("allteachers")
    @POST

    //@RolesAllowed({ "devotee","admin","mentors"})
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)

    public Uni<List<Gurus>> getTeachers() {
        return this.guruService.fetchAllTeacher();
    }




}
