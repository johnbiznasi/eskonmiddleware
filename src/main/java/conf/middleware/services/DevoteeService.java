package conf.middleware.services;

import conf.middleware.models.Devotee;
import io.agroal.api.AgroalDataSource;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.sql.*;
import java.util.Optional;

@ApplicationScoped
public class DevoteeService {
    @Inject
    AgroalDataSource db;

    public Optional<Devotee> createDevotee(Devotee guru){
        Connection conn = null;
        try {
             conn = db.getConnection();
            PreparedStatement statement = conn.prepareStatement("INSERT INTO devotee (status,created_on) VALUES (?,CURRENT_TIMESTAMP)",
                    Statement.RETURN_GENERATED_KEYS
            );

            statement.setString(1, "active");  // or any value you want for status

            int rows_created = statement.executeUpdate();
            if (rows_created > 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);  // Column index 1
                    guru.setId(generatedId+"");
                }
            } else {
                System.out.println("Insert failed");
            }
            return Optional.of(guru);
        } catch (SQLException e) {
            Log.error("Sql Error", e);
            return Optional.empty();
        }


    }
    public Optional<Devotee> updateDevote(Devotee  guru){
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

    public Optional<Devotee> deleteDevotee(Devotee  guru){
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
}
