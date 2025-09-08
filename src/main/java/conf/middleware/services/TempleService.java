package conf.middleware.services;

import conf.middleware.models.*;
import io.quarkus.logging.Log;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.Pool;
import io.vertx.mutiny.sqlclient.Tuple;
import io.vertx.sqlclient.PrepareOptions;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.sql.Connection;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class TempleService  {

    @Inject
    Pool client;

    public Uni<Temple> createTemple(Temple temple){

        try {

            String  insert="INSERT INTO public.temple(region, name,address) VALUES ($1,$2,$3) RETURNING id";
            return  client.preparedQuery(insert,new PrepareOptions())
                    .execute(Tuple.tuple()
                            .addString(temple.getLocation().getRegion()).addString(temple.getName()).addString(temple.getLocation().getRoad()))
                    .onItem().transform((row)->{
                        return new Temple(temple.getName(),row.iterator().next().getInteger("id")+"",
                                new LocationInfo(0.0,0.0,"","","",temple.getLocation().getRegion()));
                    });

        } catch (Exception e) {
            Log.error("Sql Error", e);
            return  Uni.createFrom().item(new Temple("error","-1",new LocationInfo()));
        }


    }
    public Optional<Temple> updateTemple(Temple temple){
        Connection conn = null;
        try {

            return Optional.of(temple);
        } catch (Exception e) {
            Log.error("Sql Error", e);
            return Optional.empty();
        }
    }

    public Optional<Temple> deleteTemple(Temple temple){

        try {

            return Optional.of(temple);
        } catch (Exception e) {
            Log.error("Sql Error", e);
            return Optional.empty();
        }


    }

    public Uni<List<Temple>> fetchAllTemples() {
        return client.query("SELECT * FROM  public.temple ")
                .execute()
                .onItem().transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem().transform(row -> new Temple(row.getString("name"),row.getInteger("id")+"",
                        new LocationInfo(0.0,0.0,"","",row.getString("road"),row.getString("region"))))
                .collect().asList(); // returns Uni<List<String>>
    }

    public Uni<List<Temple>> fetchRegionTemples(String  region) {
        return client.preparedQuery("SELECT * FROM  public.temple where  region=$1")
                .execute(Tuple.tuple()
                        .addString(region))
                .onItem().transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem().transform(row -> new Temple(row.getString("name"),row.getInteger("id")+"",new LocationInfo(0.0,0.0,"","","","")))
                .collect().asList(); // returns Uni<List<String>>
    }



}
