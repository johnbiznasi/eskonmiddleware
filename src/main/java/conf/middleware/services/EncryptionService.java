package conf.middleware.services;

import io.agroal.api.AgroalDataSource;
import io.vertx.mutiny.sqlclient.Pool;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class EncryptionService {
    private static final Logger log = LoggerFactory.getLogger(EncryptionService.class);
    @Inject
    AgroalDataSource db;
    @Inject
    Pool client;


    public  boolean  savePublicKey(String  key, int  account) {
        try {
           Connection con= db.getConnection();
            boolean execute = con.createStatement().execute("insert  into  public_keys(account,key)values(" + account + ",'" + key + "')");
            con.close();
            return  execute;

        } catch (SQLException e) {
          log.error("Public key saving", e);

        }
    return  false;}


    public Optional<String> getPublicKey(int  account){
        try {
            String   key="";
            Connection con= db.getConnection();
            ResultSet resultSet = con.createStatement().executeQuery("select key from public_keys  where account = " + account);
            if(resultSet.next()){
                key=resultSet.getString("key");
                con.close();
                return  Optional.of(key);
            }else{
                con.close();
                return  Optional.empty();
            }


        } catch (SQLException e) {
            log.error("Fetching  public  Key",e);

        }
        return  Optional.empty();}

    public boolean deletePublicKey(int  account){
        try {
            String   key="";
            Connection con= db.getConnection();
            boolean execute = con.createStatement().execute("delete   from public_keys  where account = " + account);
           con.close();
           return   execute;
        } catch (SQLException e) {
            log.error("Deleting  public  Key",e);
            return  false;
        }
       }

}
