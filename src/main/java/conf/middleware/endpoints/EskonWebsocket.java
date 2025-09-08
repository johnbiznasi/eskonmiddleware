package conf.middleware.endpoints;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import conf.middleware.models.Message;
import conf.middleware.services.DevoteeService;
import conf.middleware.services.RedisService;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import jakarta.websocket.Session;
import org.slf4j.LoggerFactory;

@ServerEndpoint("/chat/{usertype}/{userid}")
@ApplicationScoped
public class EskonWebsocket {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(EskonWebsocket.class);
    @Inject
    DevoteeService  devoteeService;
    @Inject
    RedisService redisService;
    @Inject
    ObjectMapper objectMapper;

    Map<String, Session> sessions = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("userid") String userid,@PathParam("usertype") String usertype) {
      //  userid="20";
        log.info("incoming userid$userid");
        String userkey = getReceiverUserKey(userid, usertype);
        sessions.put(userkey, session);
       // this.redisService.saveDevoteeChatSessions(userkey,session);
        log.info("Connection established> fro user key>>%s".formatted(userkey));
    }

    @OnClose
    public void onClose(Session session, @PathParam("userid") String userid,@PathParam("usertype") String usertype) {
        sessions.remove(userid+usertype);
     //   broadcast("User " + userid+usertype + " left");
    }

    @OnError
    public void onError(Session session, @PathParam("userid") String userid,@PathParam("usertype") String usertype, Throwable throwable) {
        sessions.remove(userid+usertype);
        throwable.printStackTrace();
        broadcast("User " + userid+usertype + " left on error: " + throwable);
    }

    @OnMessage
    public void onMessage(String message, @PathParam("userid") String userid,@PathParam("usertype") String usertype) {
        log.info("Message  came..."+message);
        try {
            JsonNode jsonNode = objectMapper.readTree(message);
            switch(jsonNode.get("operation").asText()) {
                case "set_teacher_key": {
                    devoteeService.bookGuru(jsonNode.get("teacher").asInt(), jsonNode.get("devotee").asInt());
                    //mark devoteee  with  preference
                    break;
                }

                case "set_devotee_key": {
                    devoteeService.bookDevotee(jsonNode.get("teacher").asInt(), jsonNode.get("devotee").asInt());
                    break;
                }
                case "send_message":{
                    boolean delivered=false;
                    String rec=jsonNode.get("receiver").asText().equals("20")?"21":jsonNode.get("receiver").asText();
                    String receiver = getReplyToUserKey(rec, usertype);
                    // receiver="21";
                    Session session = sessions.get(receiver);
                    if(session!=null&&session.isOpen()){
                        log.info("receiver  is  available"+receiver);
                        session.getAsyncRemote().sendObject(message);
delivered=true;

                    }else{
                        if(session!=null) {
                            sessions.remove(session);
                        }
                        log.info("receiver  is  not  online"+receiver);

                    }
                    //update  to  only save   secured   chats
                    devoteeService.saveConversation(delivered,new Message(jsonNode.get("sender").asInt(),
                                    jsonNode.get("cptext").asText()
                                    , jsonNode.get("cptext").asText(), new Date().getTime() + ""), 1, 2)
                            .subscribe().with(value->{
                                log.info("after insert");
                            },err->{
                                log.info("after insert ERROR");
                                err.printStackTrace();
                            });
                    break;
                }
            }





        } catch (Exception e) {
       e.printStackTrace();
        }


    }

    private void broadcast(String message) {
        sessions.values().forEach(s -> {
            s.getAsyncRemote().sendObject(message, result ->  {
                if (result.getException() != null) {
                    System.out.println("Unable to send message: " + result.getException());
                }
            });
        });
    }

    public String getReceiverUserKey(String userid,String userType){
        String retKey="";
        if (userType.equals("teacher")) {
            retKey="teacher" + userid;
        }else {
            retKey = "devotee" + userid;
        }
        log.info("The restuned  key  is"+retKey);
        return  retKey;
    }

    public String getReplyToUserKey(String userid,String userType){
        String retKey="";
        if (userType.equals("devotee")) {
            retKey="teacher" + userid;
        }else {
            retKey = "devotee" + userid;
        }
        log.info("The restuned  reply key  is"+retKey);
        return  retKey;
    }

}