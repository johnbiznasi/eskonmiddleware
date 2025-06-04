package conf.middleware.endpoints;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import conf.middleware.services.DevoteeService;
import conf.middleware.services.GurusService;
import conf.middleware.services.PreAuthServices;
import io.quarkus.logging.Log;
import io.vertx.mutiny.sqlclient.Pool;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.apache.shiro.session.Session;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.RestPath;
import java.util.Optional;

@Path("/authenticate")
public class Login {
    @Inject
    PreAuthServices preauth;
    @Inject
    DevoteeService devoteeService;
    @Inject
    GurusService  guruService;
    @Inject
    Pool client;
     @Inject
    ObjectMapper objectMapper;
    public static class LoginParameter {
        @RestPath
        String type;

        @RestForm
        String username;
        @RestForm
        String password;
    }

    @POST
    @Path("portal")
    @Produces(MediaType.APPLICATION_JSON)
    public String portalLogin(@BeanParam LoginParameter login) {
        ObjectNode json = objectMapper.createObjectNode();
        Optional<Session>  logginAttemp = this.preauth.portalAuthenticate(login.username, login.password);
        if(logginAttemp.isEmpty()){
            json.put("message","login failed");
        }else{
            json.put("message","OK");
            json.put("session",logginAttemp.get().getId().toString());
            try {
                json.put("account", objectMapper.writeValueAsString(guruService.getGuruFromUsername(login.username).await().indefinitely().get(0)));
            } catch (Exception e) {
                Log.error(e);
                e.printStackTrace();
            }
        }
        return json.toPrettyString();
    }
    @POST
    @Path("app")
    @Produces(MediaType.APPLICATION_JSON)
    public String appLogin(@BeanParam LoginParameter login) {
        ObjectNode json = objectMapper.createObjectNode();
        Optional<Session>  logginAttemp = this.preauth.portalAuthenticate(login.username, login.password);


        if(logginAttemp.isEmpty()){
            json.put("message","login failed");
        }else{
            json.put("message","OK");
                 
            json.put("session",logginAttemp.get().getId().toString());
            try {
                json.put("account", objectMapper.writeValueAsString(devoteeService.getDevoteeFromUsername(login.username).await().indefinitely().get(0)));
            } catch (Exception e) {
              Log.error(e);
                e.printStackTrace();
            }
        }
        return json.toPrettyString();
    }

}

