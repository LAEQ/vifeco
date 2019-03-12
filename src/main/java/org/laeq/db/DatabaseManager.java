package org.laeq.db;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.List;

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

    public boolean loadFixtures(String fixtures){
        try(Connection connection = getConnection();
            Statement stmt = connection.createStatement())
        {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fixtures);
            List<String> fixturesQuery = IOUtils.readLines(inputStream, "UTF-8");

            for (String query: fixturesQuery) {
                stmt.addBatch(query);
            }

            stmt.executeBatch();

            return true;
        } catch (SQLException | IOException e) {
            logger.error("Fail to create tables" + e.getMessage());
            return false;
        }
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
