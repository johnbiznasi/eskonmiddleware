package conf.middleware.services;

import conf.middleware.models.Temple;
import io.agroal.api.AgroalDataSource;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

@ApplicationScoped
public class TempleService  {
    @Inject
    AgroalDataSource db;

    public Optional<Temple> createTemple(Temple temple){
        Connection conn = null;
        try {
             conn = db.getConnection();
            conn.createStatement().executeQuery("");
            conn.close();
            return Optional.of(temple);
        } catch (SQLException e) {
            Log.error("Sql Error", e);
            return Optional.empty();
        }


    }
    public Optional<Temple> updateTemple(Temple temple){
        Connection conn = null;
        try {
            conn = db.getConnection();
            conn.createStatement().executeQuery("");
            conn.close();
            return Optional.of(temple);
        } catch (SQLException e) {
            Log.error("Sql Error", e);
            return Optional.empty();
        }
    }

    public Optional<Temple> deleteTemple(Temple temple){
        Connection conn = null;
        try {
            conn = db.getConnection();
            conn.createStatement().executeQuery("");
            conn.close();
            return Optional.of(temple);
        } catch (SQLException e) {
            Log.error("Sql Error", e);
            return Optional.empty();
        }


    }
}
