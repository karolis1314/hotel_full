package org.registration.service;

import org.registration.domain.entity.Guest;
import org.registration.domain.entity.Room;
import org.registration.exception.ApplicationException;
import org.registration.mapper.RoomMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static org.registration.utils.ServiceUtils.getResultsFromQuery;

public class RoomService {
    public RoomService() {
    }

    public List<Room> getAllRooms() throws ApplicationException {
        List<Room> rooms = new ArrayList<>();
        try (Connection connection = JdbcConnection.connect();
             ResultSet resultSet = getResultsFromQuery("SELECT * FROM Room", connection)) {

            while (resultSet.next()) {
                rooms.add(RoomMapper.fromResultSetToRoom(
                        resultSet.getLong("id"),
                        resultSet.getBoolean("available")));
            }
        } catch (SQLException e) {
            throw new ApplicationException("Error retrieving rooms");
        }

        return rooms;
    }

    public List<Room> getAllAvailableRooms() throws ApplicationException {
        List<Room> allRooms = getAllRooms();

        return allRooms.stream().filter(Room::isAvailable).collect(Collectors.toList());
    }

    public List<Room> getAllBookedRooms() throws ApplicationException {
        List<Room> rooms = new ArrayList<>();
        try (Connection connection = JdbcConnection.connect();
             ResultSet resultSet = getResultsFromQuery(
                     "SELECT r.id AS room_id, g.firstName, g.lastName FROM room r INNER JOIN guest g ON r.guest_id = g.id WHERE r.available = false AND r.guest_id IS NOT NULL;", connection)) {
            while (resultSet.next()) {
                rooms.add(RoomMapper.fromResultSetToRoomWithGuest(
                        resultSet.getLong("room_id"),
                        String.format(Locale.getDefault(), "%s %s"
                                , resultSet.getString("firstName")
                                , resultSet.getString("lastName"))));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new ApplicationException("Error retrieving rooms with guests");
        }
        return rooms;
    }

    public void setRoomsAvailableForGuest(String lastName) throws ApplicationException {
        try (Connection connection = JdbcConnection.connect()) {
            String query = "UPDATE room r SET available = true, guest_id = NULL WHERE available = false AND EXISTS (SELECT 1 FROM guest g WHERE r.guest_id = g.id AND g.lastName = ?)";

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, lastName);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new ApplicationException("Error updating room availability for guest");
        }
    }

    public void setRoomToBeTaken(Long roomNumber, Guest guest) throws ApplicationException {
        try (Connection connection = JdbcConnection.connect()) {
           String query =  "UPDATE room SET available = false, guest_id = ? WHERE id = ? AND available = true";

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setLong(1, guest.getId());
                statement.setLong(2, roomNumber);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new ApplicationException("Error updating room availability for guest");
        }
    }

}
