package conf.middleware.services;

import conf.middleware.models.*;
import io.agroal.api.AgroalDataSource;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.Pool;
import io.vertx.mutiny.sqlclient.Tuple;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

@ApplicationScoped
public class DevoteeService {
    private static final Logger log = LoggerFactory.getLogger(DevoteeService.class);
    @Inject
    AgroalDataSource db;

    @Inject
    Pool client;

    public Uni<Integer> createDevotee(Devotee student){
        String newdevotee="INSERT INTO public.devotee (status,created_on,public_key, device_id) VALUES ($1,CURRENT_TIMESTAMP,'"+student.getPublicKey()+"','"+student.getId()+"') RETURNING id";
        return client.preparedQuery(newdevotee)
                .execute(Tuple.tuple().addString("new_account"))
                .onItem()
                .transform(rows ->rows.iterator().next().getInteger("id") )
                .onFailure().recoverWithItem(-1);


    }

    public Uni<List<Devotee>> getDevotee(int id ){
        String newdevotee="select * from public.devotee where id=$1";
        return client.preparedQuery(newdevotee)
                .execute(Tuple.tuple().addInteger(id))
                .onItem().transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem().transform(row -> new Devotee(row.getInteger("id")+"",new Date(), AccountStatus.valueOf(row.getString("status")),row.getString("public_key")))
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
                .onItem().transform(row -> new Devotee(row.getInteger("id")+"",new Date(),
                        AccountStatus.ACTIVE,
                        row.getString("public_key")))
                .collect().asList();


    }

    public Uni<List<Devotee>> availableDevotees(){
        String newdevotee=" SELECT * FROM public.devotee where status=$1";
        return client.preparedQuery(newdevotee)
                .execute(Tuple.tuple().addString("new_account"))
                .onItem().transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem().transform(row -> new Devotee(row.getInteger("id")+"",new Date(),
                        AccountStatus.ACTIVE,
                        row.getString("public_key")))
                .collect().asList();
    }

    public Uni<List<Devotee>> guruDevotees(int  guru){
        String newdevotee="SELECT * FROM public.conversations_mappings as c right  join public.devotee  on devotee.id=c.devotee  where c.guru =$1 ORDER BY conversationid ASC ";
        return client.preparedQuery(newdevotee)
                .execute(Tuple.tuple().addInteger(guru))
                .onItem().transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem().transform(row -> new Devotee(row.getInteger("guru")+"",row.getInteger("id")+"",new Date(row.getLocalDateTime("created_on").getNano()),
                        AccountStatus.ACTIVE,
                        row.getString("public_key"),row.getString("shared"),row.getString("teacher_public")))
                .collect().asList();
    }

    public Uni<List<Devotee>> devotesGuru(int  devotee){
        String newdevotee="SELECT * FROM public.conversations_mappings as c right  join public.devotee  on devotee.id=c.devotee  where c.devotee =$1 ORDER BY conversationid ASC ";
        return client.preparedQuery(newdevotee)
                .execute(Tuple.tuple().addInteger(devotee))
                .onItem().transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem().transform(row -> new Devotee(row.getInteger("guru").toString(),row.getInteger("id")+"",new Date(row.getLocalDateTime("created_on").getNano()),
                        AccountStatus.ACTIVE,
                        row.getString("public_key"),row.getString("shared"),row.getString("teacher_public")))
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

    public Uni<Integer> startConversation(int devotee, int teacher){
        String update="update  public.devotee set   status = 'acquired', shared=$2, public=$3 where id= $1";
       client.preparedQuery(update)
                .execute(Tuple.tuple().addInteger(devotee)
                        )
               .subscribe().with(value->{
                   log.info("devottee $devotee  has  been  updated");
               },err->{
                   err.printStackTrace();
               });


        String newdevotee="INSERT INTO public.conversations_mappings(created_on, devotee, guru,status) " +
                "VALUES (CURRENT_TIMESTAMP, $1, $2,'active') RETURNING conversationid";
        return client.preparedQuery(newdevotee)
                .execute(Tuple.tuple().addInteger(devotee).addInteger(teacher))
                .onItem()
                .transform(rows ->rows.iterator().next().getInteger("conversationid") );




    }

    public Uni<List<Message>> converationMessages(int devotee, int teacher){
        String converstaions="SELECT * FROM public.conversations  where devotee= $1 and guru= $2 ";
        return client.preparedQuery(converstaions)
                .execute(Tuple.tuple().addInteger(devotee).addInteger(teacher))
                .onItem().transformToMulti(set -> Multi.createFrom().iterable(set))

                .onItem().transform(row -> new Message(row.getInteger("sender_id"), row.getString("message_id"),
                        row.getString("message"), row.getString("created_on")))
                .onFailure().recoverWithItem(new Message(-1,"-1","",""))
                .collect().asList();
    }


    public Uni<Integer> bookGuru(int guru,int devotee) {
        String converstaions = "insert into public.book_guru (created_on,guru,devotee)values(CURRENT_TIMESTAMP,$1,$2) RETURNING booking_id";
        return client.preparedQuery(converstaions)
                .execute(Tuple.tuple().addInteger(guru)
                        .addInteger(devotee))
                         .onItem()
                .transform(rows -> rows.iterator().next().getInteger("booking_id"));




    }

    public Uni<Integer> bookDevotee(int guru,int devotee) {
        String converstaions = "insert into public.book_devotee (created_on,guru,devotee)values(CURRENT_TIMESTAMP,$1,$2) RETURNING booking_id";
        return client.preparedQuery(converstaions)
                .execute(Tuple.tuple().addInteger(guru)
                        .addInteger(devotee))
                .onItem()
                .transform(rows -> rows.iterator().next().getInteger("booking_id"));




    }




    public Uni<Integer> saveConversation(boolean delivered,Message message,int guru,int devotee) {
        String converstaions = "insert into public.conversations (senderid,message,created_on,guru,devotee,delivered)values($1,$2,$3,$4,$5,$6,$7) RETURNING conversationid";
        return client.preparedQuery(converstaions)
                .execute(Tuple.tuple().addInteger(message.getSenderid())

                        .addString(message.getMessage())
                        .addString(new Date().getTime() + "").addInteger(guru).addInteger(devotee).addBoolean(delivered))
                .onItem()
                .transform(rows -> rows.iterator().next().getInteger("conversationid"))
                .onFailure().invoke(err -> {
                    // capture/log the error
                    log.info("Saving  Message error",err);  // or use a logger
                });

    }





        public Uni<List<ConversationsHeads>> devoteeConversations(int  devoteeid){
        String conversationsHeads="SELECT * FROM public.conversations_mappings  right   join mentor  on mentor.id=guru\n" +
                "where   conversations_mappings.devotee=$1";
               return client.preparedQuery(conversationsHeads)
                .execute(Tuple.tuple().addInteger(devoteeid))
                .onItem().transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem().transform(row -> new ConversationsHeads(row.getString("first_name"), row.getString("public_key"),
                               row.getValue("created_on").toString(), devoteeid+""))
                .collect().asList();



    }






}
