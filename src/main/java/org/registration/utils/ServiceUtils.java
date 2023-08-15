package org.registration.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ServiceUtils {

    public static ResultSet getResultsFromQuery(String query, Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        return preparedStatement.executeQuery();
    }
}
