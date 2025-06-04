package conf.middleware.services;

import conf.middleware.models.*;
import io.agroal.api.AgroalDataSource;
import io.quarkus.logging.Log;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.Pool;
import io.vertx.mutiny.sqlclient.Tuple;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.sql.*;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class DevoteeService {
    @Inject
    AgroalDataSource db;

    @Inject
    Pool client;

    public Uni<Integer> createDevotee(Devotee student){
        String newdevotee="INSERT INTO public.devotee (status,created_on) VALUES ($1,CURRENT_TIMESTAMP) RETURN id";
        return client.preparedQuery(newdevotee)
                .execute(Tuple.tuple().addString("new_account"))
                .onItem()
                .transform(rows ->rows.iterator().next().getInteger("id") )
                .onFailure().recoverWithItem(-1);


    }

    public Uni<List<Devotee>> getDevotee(int id ){
        String newdevotee="select * from  public.devotee where id=$1";
        return client.preparedQuery(newdevotee)
                .execute(Tuple.tuple().addInteger(id))
                .onItem().transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem().transform(row -> new Devotee(row.getInteger("id")+"",new Date(), AccountStatus.valueOf(row.getString("status"))))
                .collect().asList();


    }

    public Uni<List<Devotee>> getDevoteeFromUsername(String username ){
        String newdevotee="SELECT b.status,pu.username,b.id\n" + //
                        "FROM public.devotee AS b\n" + //
                        "RIGHT JOIN public.platform_users pu \n" + //
                        "  ON b.id = pu.userid\n" + //
                        "WHERE pu.username =$1";
        return client.preparedQuery(newdevotee)
                .execute(Tuple.tuple().addString(username))
                .onItem().transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem().transform(row -> new Devotee(row.getInteger("id")+"",new Date(), AccountStatus.valueOf(row.getString("status"))))
                .collect().asList();


    }




    public Uni<Boolean> deleteDevotee(int  studentId){
        String newdevotee="delete from  public.devoteee  where id=$1";
        return client.preparedQuery(newdevotee)
                .execute(Tuple.tuple().addInteger(studentId))
                .onItem()
                .transform(rows ->rows.rowCount()>0)
                .onFailure().recoverWithItem(false);


    }

    public Uni<Integer> StartConveration(Devotee student){
        String newdevotee="INSERT INTO public.devotee (,status,created_on) VALUES ($1,CURRENT_TIMESTAMP) RETURN id";
        return client.preparedQuery(newdevotee)
                .execute(Tuple.tuple().addString("new_account"))
                .onItem()
                .transform(rows ->rows.iterator().next().getInteger("id") )
                .onFailure().recoverWithItem(-1);


    }







}
