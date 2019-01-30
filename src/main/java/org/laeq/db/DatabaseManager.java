package org.laeq.db;

import javax.annotation.Nonnull;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

public class DatabaseManager {
    @Nonnull
    private DatabaseConfigInterface config;

    public DatabaseManager(DatabaseConfigInterface config) {
        this.config = config;
    }


    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(this.config.getUrl(), this.config.getUser(), this.config.getPassword());
    }

    public boolean loadFixtures(String fixturesPath) throws IOException, SQLException {
        BufferedReader reader = new BufferedReader(new FileReader(fixturesPath));
        StringBuilder builder = new StringBuilder();
        String line;

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            while ((line = reader.readLine()) != null) {
                builder.append(line);

                if (line.contains(";")) {
                    statement.addBatch(builder.toString());
                    builder.delete(0, builder.length());
                }
            }

            statement.executeBatch();

            return true;
        }
    }
}
