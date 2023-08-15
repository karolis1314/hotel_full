package org.registration.service;

import org.registration.domain.entity.Guest;
import org.registration.exception.ApplicationException;
import org.registration.mapper.GuestMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.registration.utils.ServiceUtils.getResultsFromQuery;

public class GuestService {

    public GuestService() {}

    public List<Guest> getAllGuest() throws ApplicationException {
        List<Guest> guests = new ArrayList<>();
        try (Connection connection = JdbcConnection.connect();
             ResultSet resultSet = getResultsFromQuery("SELECT * FROM Guest", connection)) {

            while (resultSet.next()) {
                guests.add(GuestMapper.fromResultSetToGuest(
                        resultSet.getLong("id"),
                        resultSet.getString("firstName"),
                        resultSet.getString("lastName")));
            }
        } catch (SQLException e) {
            throw new ApplicationException("Error retrieving guests");
        }

        return guests;
    }

    public void createGuest(String firstName, String lastName) throws ApplicationException {
        try (Connection connection = JdbcConnection.connect()) {
            if (connection != null) {
                String insertQuery = "INSERT INTO Guest (firstName, lastName) VALUES (?, ?)";
                try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                    insertStatement.setString(1, firstName);
                    insertStatement.setString(2, lastName);
                    insertStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            if (e.getMessage().contains("duplicate key value violates unique constraint")) {
                throw new ApplicationException("Error: Guest with the same first name and last name already exists.");
            } else {
                throw new ApplicationException("Error creating new Guest");
            }
        }
    }

    public void updateGuest(Guest guest, Guest toUpdate) throws ApplicationException {
        try (Connection connection = JdbcConnection.connect()) {
            if (connection != null) {
                if (hasGuestByLastName(guest.getLastName())) {
                    String updateQuery = "UPDATE Guest SET firstName = ?, lastName = ? WHERE lastName = ?";
                    try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                        updateStatement.setString(1, toUpdate.getFirstName());
                        updateStatement.setString(2, toUpdate.getLastName());
                        updateStatement.setString(3, guest.getLastName());
                        updateStatement.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            throw new ApplicationException("Error updating the guest");
        }
    }

    public void deleteGuest(String lastName) throws ApplicationException {
        if (hasGuestByLastName(lastName)) {
            try (Connection connection = JdbcConnection.connect()) {
                if (connection != null) {
                    String deleteQuery = "DELETE FROM Guest WHERE lastName = ?";
                    try (PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery)) {
                        deleteStatement.setString(1, lastName);
                        deleteStatement.executeUpdate();
                    }
                }
            } catch (SQLException e) {
                new ApplicationException("Error deleting guest");
            }
        }
    }

    public Guest getGuestByName(String lastName) throws ApplicationException {
        Guest guestDto = null;
        try (Connection connection = JdbcConnection.connect()) {
            String query = "SELECT * FROM Guest WHERE lastName = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, lastName);

                try (ResultSet resultSet = getResultsFromQuery(statement.toString(), connection)) {
                    if (resultSet.next()) {
                        guestDto = GuestMapper.fromResultSetToGuest(
                                resultSet.getLong("id"),
                                resultSet.getString("firstName"),
                                resultSet.getString("lastName")
                        );
                    }
                }
            }
        } catch (SQLException e) {
            throw new ApplicationException("Error trying to retrieve guest by last name");
        }

        return guestDto;
    }

    private boolean hasGuestByLastName (String lastName) throws ApplicationException {
        try (Connection connection = JdbcConnection.connect()) {
            if (connection != null) {
                String query = "SELECT id FROM Guest WHERE lastName = ?";
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.setString(1, lastName);
                    try (ResultSet resultSet = statement.executeQuery()) {
                        return resultSet.next();
                    }
                }
            }
        } catch (SQLException e) {
            throw new ApplicationException(String.format(Locale.getDefault(),"No such guest with lastName %s", lastName));
        }
        return false;
    }

}