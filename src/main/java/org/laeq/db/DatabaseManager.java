package org.laeq.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;

public class DatabaseManager {
    private Logger logger;

    @Nonnull
    private DatabaseConfigInterface config;

    public DatabaseManager(DatabaseConfigInterface config) {
        logger = LoggerFactory.getLogger(getClass().getCanonicalName());

        this.config = config;
    }


    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(this.config.getUrl(), this.config.getUser(), this.config.getPassword());
    }

    public boolean loadFixtures(URI fixtures) throws IOException, SQLException {
        int result = 0;

        String content = new String(Files.readAllBytes(Paths.get(fixtures)));
        try (Connection connection = getConnection();
             Statement stmt = connection.createStatement()){

            result = stmt.executeUpdate(content);
        }

        return result == 1;
    }



    public boolean loadFixtures(URL fixtures) throws IOException, SQLException, URISyntaxException {
        return loadFixtures(fixtures.toURI());
    }

    public void getTableStatus() throws SQLException, DAOException {

        String query = "SELECT COUNT(*) FROM  INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'PUBLIC';";
        int result = 0;

        try(Connection connection = getConnection();
        Statement stmt = connection.createStatement())
        {
            ResultSet resultSet = stmt.executeQuery(query);

            if(resultSet.next()){
                result = resultSet.getInt(1);
            }
        }

        if(result != 6){
            String message = String.format("Table status: failed - %d tables instead of 6.", result);
            throw new DAOException(message);
        }
    }
}
