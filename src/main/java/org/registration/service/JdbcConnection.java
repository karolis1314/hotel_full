package org.registration.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcConnection {

    private static final String url = "jdbc:postgresql://localhost:5432/hotel";
    private static final String user = "postgres";
    private static final String password = "root";

    public static final Connection connect() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, user, password);

            if (connection != null) {
                System.out.println("Connected to the PostgreSQL server successfully.");
            } else {
                System.out.println("Failed to make connection!");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return connection;
    }
}
