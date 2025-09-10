package conf.middleware.services;

import conf.middleware.models.*;
import conf.middleware.models.forms.GuruDTO;
import io.agroal.api.AgroalDataSource;
import io.smallrye.mutiny.Multi;
import io.vertx.mutiny.sqlclient.Pool;
import io.smallrye.mutiny.Uni;

import io.vertx.mutiny.sqlclient.Tuple;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import net.bytebuddy.utility.RandomString;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.interfaces.XECPrivateKey;
import java.security.interfaces.XECPublicKey;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@ApplicationScoped
public class GurusService {
    private static final Logger log = LoggerFactory.getLogger(GurusService.class);
    @Inject
    AgroalDataSource db;
    @Inject
    Pool client;
    public  enum GuruFilters{country,region,temple,status,id};
    @Inject
    @Channel("signup-emails")
    Emitter<EmailNotification> emailEmitter;







    public Uni<Integer> createTeacher(GuruDTO mentor) {
        KeyPair keyPair = null;
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("X25519");
             keyPair = kpg.generateKeyPair();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        if(keyPair!=null) {
            XECPublicKey publicKey = (XECPublicKey) keyPair.getPublic();
            XECPrivateKey privateKey = (XECPrivateKey) keyPair.getPrivate();

            byte[] rawPublic = publicKey.getU().toByteArray();

            byte[] rawPrivate =privateKey.getScalar().isPresent()?privateKey.getScalar().get():null;

            String sql = "INSERT INTO public.mentor (first_name,last_name,status,region,temple,age, " +
                    "country,email,cellphone,public_key,private_key) " +
                    "VALUES ($1,$2,$3,$4,$5,$6,$7,$8,$9,$10,$11)  RETURNING id";
            Tuple tuple = Tuple.tuple()

                    .addString(mentor.firstName)
                    .addString(mentor.lastName)
                    .addString(mentor.status)
                    .addString(mentor.region)
                    .addString(mentor.temple)
                    .addInteger(mentor.age)
                    .addString(mentor.country)
                    .addString(mentor.email)
                    .addString(mentor.cellphone)
                    .addString(Base64.getEncoder().encodeToString(rawPublic))
                    .addString(Base64.getEncoder().encodeToString(rawPrivate));


            return client
                    .preparedQuery(sql)
                    .execute(tuple)

                    .onItem()
                    // .transform(rows -> rows.iterator().next().getInteger("id"))
                    .transformToUni(rows -> {
                        Integer id = rows.iterator().next().getInteger("id");
                        String useraccount = "insert into public.platform_users (username,password,userid,role)VALUES ($1,$2,$3,$4)";
                        String password = RandomString.make(5);
                        String message = "Dear Mentor " + mentor.firstName + "Your  Username is" + mentor.email + " password is " + password;
                        Tuple app_user = Tuple.tuple()
                                .addString(mentor.email)
                                .addString(password)
                                .addInteger(id)
                                .addString("mentor");
                        emailEmitter.send(new EmailNotification(message, mentor.email, new Date().toString(), "New Mentor Account"));
                        return client
                                .preparedQuery(useraccount)
                                .execute(app_user)
                                .onItem()
                                .transform(rows2 -> id);
                    })
                    .onFailure().recoverWithItem(e -> {
                        log.error("teacher inserting error", e);
                        return -1;
                    }); // Return Uni<Void>
        }else{
            return  Uni.createFrom().item(1);
        }
    }

    public Uni<Boolean> updateTeacher(GuruDTO mentor) {
        String sql = "UPDATE public.mentor SET " +
                "first_name = $1, " +
                "last_name = $2, " +
                "status = $3, " +
                "region = $4, " +
                "temple = $5, " +
                "age = $6, " +
                "country = $7 " +
                "WHERE id = $8";
        Tuple tuple = Tuple.tuple()

                .addString(mentor.firstName)
                .addString(mentor.lastName)
                .addString(mentor.status)
                .addString(mentor.region)
                .addString(mentor.temple)
                .addInteger(mentor.age)
                .addString(mentor.country)
                .addInteger(mentor.id);;

        return client
                .preparedQuery(sql)
                .execute(tuple)
                .onItem().transform(rows -> rows.rowCount()>0)
                .onFailure().recoverWithItem(e->{
                    log.error("teacher  updating error", e);
                    return false;}); // Return Uni<Void>
    }

    public Uni<Boolean> deleteTeacher(Integer userid) {
        String sql = "delete  from public.mentor WHERE id = $1";

        Tuple tuple = Tuple.tuple()
             .addInteger(userid);
        return client
                .preparedQuery(sql)
                .execute(tuple)

                .onItem().transform(rows -> rows.rowCount()>0)
                .onFailure().recoverWithItem(e->{
                    log.error("teacher deleting error", e);
                    return false;}); // Return Uni<Void>
    }

    public Uni<List<Gurus>> fetchAllTeacher() {
        return client.query("SELECT * FROM public.mentor")
                .execute()
                .onItem().transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem().transform(row -> new Gurus(false,row.getString("first_name"),
                        row.getString("last_name"),row.getInteger("id")+"",row.getString("region")
                        ,row.getString("country"),-1,new Temple("name","id",new LocationInfo()),
                        row.getString("public_key"), row.getString("email"), row.getString("cellphone")))
                .collect().asList(); // returns Uni<List<String>>
    }
    /**
     * fetch  list  of  gurus
     * @param key
     * @param value
     * @return
     */
    public Uni<List<Gurus>> fetchTeacher(GuruFilters key, String value) {
log.info("Cheking key>>"+key.name()+"with  value"+value);
        return client.query("SELECT * FROM  public.mentor  where "+key.name()+"='"+value+"' ")
                .execute()
                .onItem().transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem().transform(row -> new Gurus(true,row.getString("first_name"),
                        row.getString("last_name"),row.getInteger("id")+"",row.getString("region")
                        ,row.getString("country"),-1,new Temple("name","id",new LocationInfo()),
                        row.getString("public_key"), row.getString("email"), row.getString("cellphone")))
                .collect().asList(); // returns Uni<List<String>>
    }

    public Uni<List<GuruDTO>> getGuru(int id ){
        String fetchguru="select * from public.mentor where id=$1";
        return client.preparedQuery(fetchguru)
                .execute(Tuple.tuple().addInteger(id))
                .onItem().transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem().transform(row -> new GuruDTO(id, row.getString("first_name"),row.getString("last_name"),
                        row.getString("status"),row.getString("region"), row.getString("temple"),
                        row.getInteger("age"),row.getString("country")) )
                .collect().asList();



    }

    public Uni<List<GuruDTO>> getGuruFromUsername(String username ){
        String newdevotee="SELECT * \n" + //
                "FROM public.mentor AS b\n" + //
                "RIGHT JOIN public.platform_users pu \n" + //
                "  ON b.id = pu.userid\n" + //
                "WHERE pu.username =$1";
        return client.preparedQuery(newdevotee)
                .execute(Tuple.tuple().addString(username))
                .onItem().transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem().transform(row -> new GuruDTO(row.getString("role"),row.getInteger("id"), row.getString("first_name"),row.getString("last_name"),
                        row.getString("status"),row.getString("region"), row.getString("temple"),
                        row.getInteger("age"),row.getString("country"),row.getString("private_key"),row.getString("public_key")))
                .collect().asList();


    }

}
