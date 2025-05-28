package conf.middleware.endpoints;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import conf.middleware.services.PreAuthServices;
import io.vertx.mutiny.sqlclient.Pool;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.apache.shiro.session.Session;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.RestPath;
import org.jboss.resteasy.reactive.RestQuery;

import java.util.Map;
import java.util.Optional;

@Path("/authenticate")
public class Login {
    @Inject
    PreAuthServices preauth;
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
    @Produces(MediaType.APPLICATION_JSON)
    public String portalLogin(@BeanParam LoginParameter login) {
        ObjectNode json = objectMapper.createObjectNode();
        Optional<Session>  logginAttemp = this.preauth.portalAuthenticate(login.username, login.password);
        if(logginAttemp.isEmpty()){
            json.put("message","login failed");
        }else{
            json.put("message","OK");
            json.put("session",logginAttemp.get().getId().toString());
        }
        return json.toPrettyString();
    }
}

