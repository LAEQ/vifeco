package org.laeq.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    private final String url;
    private final String user;
    private final String password;
    public DatabaseManager() throws ClassNotFoundException {
//        Class.forName("org.hsqldb.jdbc.JDBCDriver");
        this.url = "jdbc:hsqldb:hsql://localhost/testdb";
        this.user = "SA";
        this.password = "";
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(this.url, this.user, this.password);
    }
}
