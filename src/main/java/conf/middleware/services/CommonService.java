package conf.middleware.services;

import conf.middleware.endpoints.CommonEndpoint;
import conf.middleware.models.LocationInfo;
import conf.middleware.models.Temple;
import conf.middleware.models.forms.RegionDTO;
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
public class CommonService {

    @Inject
    Pool client;

    public Uni<RegionDTO> createRegion(CommonEndpoint.RegionParameter region){
            String  insert="INSERT INTO public.regions(name,country) VALUES ($1,$2) RETURNING id";
            return  client.preparedQuery(insert,new PrepareOptions())
                    .execute(Tuple.tuple()
                            .addString(region.getName()).addString(region.getCountry()))
                    .onItem().transform((row)->{
                        return new RegionDTO(row.iterator().next().getInteger("id"),
                                region.getCountry(),region.getName());
                    }).onFailure().
                    recoverWithItem(new RegionDTO(-1,"country","not  set"));
    }


    public Uni<Boolean> deleteRegion(RegionDTO  region){
        return  client.preparedQuery("delete  from  public.region  where id=$1",new PrepareOptions())
                    .execute(Tuple.tuple()
                            .addInteger(region.getId()))
                    .onItem().transform((row)->{
                   return  true;
                    }).onFailure().recoverWithItem(false);


    }

    public Uni<List<RegionDTO>> fetchAllRegions() {
        return client.query("SELECT * FROM  public.regions ")
                .execute()
                .onItem().transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem().transform(row -> new RegionDTO(row.getInteger("id"),row.getString("country"),row.getString("name")))
                .collect().asList();
    }




}
