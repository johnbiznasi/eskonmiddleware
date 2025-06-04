package conf.middleware.services;

import conf.middleware.models.*;
import conf.middleware.models.dtos.GuruDTO;
import io.agroal.api.AgroalDataSource;
import io.smallrye.mutiny.Multi;
import io.vertx.mutiny.sqlclient.Pool;
import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;

import io.vertx.mutiny.sqlclient.Tuple;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class GurusService {
    private static final Logger log = LoggerFactory.getLogger(GurusService.class);
    @Inject
    AgroalDataSource db;
    @Inject
    Pool client;
    public  enum GuruFilters{country,region,temple,status};



    public Optional<Gurus> updateGuru(Gurus  guru){
        Connection conn = null;
        try {
            conn = db.getConnection();
            conn.createStatement().executeQuery("");
            conn.close();
            return Optional.of(guru);
        } catch (SQLException e) {
            Log.error("Sql Error", e);
            return Optional.empty();
        }
    }

    public Optional<Gurus> deleteGuru(Gurus  guru){
        Connection conn = null;
        try {
            conn = db.getConnection();
            conn.createStatement().executeQuery("");
            conn.close();
            return Optional.of(guru);
        } catch (SQLException e) {
            Log.error("Sql Error", e);
            return Optional.empty();
        }


    }


    public Uni<Integer> insertMentor(GuruDTO mentor) {

        String sql = "INSERT INTO public.mentor (first_name,last_name,status,region,temple,age, country) " +
                "VALUES ($1,$2,$3,$4,$5,$6,$7)  RETURNING id";
        Tuple tuple = Tuple.tuple()

                .addString(mentor.firstName)
                .addString(mentor.lastName)
                .addString(mentor.status)
                .addString(mentor.region)
                .addString(mentor.temple)
                .addInteger(mentor.age)
                .addString(mentor.country);

        return client
                .preparedQuery(sql)
                .execute(tuple)

                .onItem().transform(rows -> rows.iterator().next().getInteger("id"))
                .onFailure().recoverWithItem(e->{
                    log.error("teacher  inserting error", e);
                    return -1;}); // Return Uni<Void>
    }

    public Uni<Boolean> updateMentor(GuruDTO mentor) {
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

    public Uni<Boolean> deleteMentor(Integer userid) {
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


    /**
     * fetch  list  of  gurus
     * @param key
     * @param value
     * @return
     */
    public Uni<List<Gurus>> fetchTeacher(GuruFilters key, String value) {
        return client.query("SELECT * FROM  public.mentor  where "+key.name()+"='"+value+"' ")
                .execute()
                .onItem().transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem().transform(row -> new Gurus(row.getString("first_name"),
                        row.getString("last_name"),row.getInteger("id")+"",row.getString("region")
                        ,row.getString("country"),row.getInteger("age"),new Temple("name","id",new LocationInfo())))
                .collect().asList(); // returns Uni<List<String>>
    }

    public Uni<List<GuruDTO>> getGuru(int id ){
        String fetchguru="select * from  public.mentor where id=$1";
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
                .onItem().transform(row -> new GuruDTO(row.getInteger("id"), row.getString("first_name"),row.getString("last_name"),
                        row.getString("status"),row.getString("region"), row.getString("temple"),
                        row.getInteger("age"),row.getString("country")))
                .collect().asList();


    }

}
