package conf.middleware.services;

import com.fasterxml.jackson.core.type.TypeReference;
import io.quarkus.redis.datasource.hash.HashCommands;
import io.quarkus.redis.datasource.value.ValueCommands;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import io.quarkus.redis.datasource.ReactiveRedisDataSource;
import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.keys.ReactiveKeyCommands;
import io.smallrye.mutiny.Uni;
import jakarta.websocket.Session;

import java.util.List;

@ApplicationScoped
public class RedisService {

    private ReactiveKeyCommands<String> keyCommands;
    private ValueCommands<String, Long> countCommands;
    private HashCommands<String, String, Session> devoteesCommands;


    public RedisService(RedisDataSource ds, ReactiveRedisDataSource reactive) {
        countCommands = ds.value(Long.class);
        keyCommands = reactive.key();
        devoteesCommands = ds.hash(new TypeReference<Session>(){});


    }


    long get(String key) {
        Long value = countCommands.get(key);
        if (value == null) {
            return 0L;
        }
        return value;
    }

    void set(String key, Long value) {
        countCommands.set(key, value);
    }

    void increment(String key, Long incrementBy) {
        countCommands.incrby(key, incrementBy);
    }

    Uni<Void> del(String key) {
        return keyCommands.del(key)
                .replaceWithVoid();
    }

    Uni<List<String>> keys() {
        return keyCommands.keys("*");
    }
    public void  saveDevoteeChatSessions(String userid, Session session){
        devoteesCommands.hset("conversation_chats", userid,session);
    }
    public Session  getDevoteeChatSessions(String userid){
        return devoteesCommands.hget("conversation_chats",userid);

    }

    public  Session removeChatSessions(String userid){
        return devoteesCommands.hget("conversation_chats",userid);

    }

}