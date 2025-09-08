package conf.middleware.endpoints;

import conf.middleware.models.forms.AppPublicKeys;
import conf.middleware.services.EncryptionService;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.reactive.RestPath;

import java.util.Optional;

@Path("/keys")
public class HandShake {
    @Inject
    EncryptionService encryservice;
    @Path("/add")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<Boolean> createAccountKey(@BeanParam AppPublicKeys key) {
        return Uni.createFrom().item(() -> this.encryservice.savePublicKey(key.getKey(), key.getAccount()))
                .runSubscriptionOn(Infrastructure.getDefaultExecutor());

    }


    @Path("/get/{account}")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Blocking
    public Optional<String> getAccoutKey(@RestPath int account) {
       return  this.encryservice.getPublicKey(account);

    }

    @Path("/delete/{account}")
    @DELETE
    @Produces(MediaType.TEXT_PLAIN)
    @Blocking
    public Boolean deleteAccoutKey(@RestPath int account) {
        return  this.encryservice.deletePublicKey(account);

    }



}
