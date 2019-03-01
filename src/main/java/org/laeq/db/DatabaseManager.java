package org.laeq.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
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

        Path path = Paths.get(fixtures);

        if( ! Files.exists(path)){
            logger.error(String.format("DatabaseManager: cannot find the file: %s", fixtures.toString()));
            return false;
        }

        BufferedReader reader = new BufferedReader(new FileReader(path.toFile()));
        StringBuilder builder = new StringBuilder();
        String line;

        try (Connection connection = getConnection();
             Statement stmt = connection.createStatement()){

            while((line = reader.readLine()) != null){
                builder.append(line);

                if(line.contains(";")){
                    stmt.addBatch(builder.toString());
                    builder.delete(0, builder.length());
                }
            }

            int[] result = stmt.executeBatch();

            return true;
        }
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
