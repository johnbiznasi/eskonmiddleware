package conf.middleware.endpoints;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import conf.middleware.services.DevoteeService;
import conf.middleware.services.GurusService;
import conf.middleware.services.JwtGenerator;
import conf.middleware.services.PreAuthServices;
import io.quarkus.logging.Log;
import io.vertx.mutiny.sqlclient.Pool;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.shiro.session.Session;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.RestPath;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

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
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    public Response portalLogin(@BeanParam LoginParameter login) {
        ObjectNode json = objectMapper.createObjectNode();
        Optional<Session>  logginAttemp = this.preauth.portalAuthenticate(login.username, login.password);
        if(logginAttemp.isEmpty()){
            json.put("message","login failed");
        }else{
            json.put("message","OK");
            json.put("session",logginAttemp.get().getId().toString());



            try {
             //   json.put("account", objectMapper.writeValueAsString(devoteeService.getDevoteeFromUsername(login.username).await().indefinitely().get(0)));
                HashSet<String>  roles=  new HashSet<>();
                roles.add("admin");
                String token = JwtGenerator.generate(login.username,roles,objectMapper.writeValueAsString(guruService.getGuruFromUsername(login.username).await().indefinitely().get(0)));
              //  String token = JwtGenerator.generate(login.username,roles);
                return Response.ok(Map.of("token", token)).build();
            } catch (Exception e) {
                Log.error(e);
                e.printStackTrace();
            }
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }
    @POST
    @Path("app")
    @PermitAll
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

