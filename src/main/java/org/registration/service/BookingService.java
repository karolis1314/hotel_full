package org.registration.service;

import org.registration.domain.entity.Booking;
import org.registration.exception.ApplicationException;
import org.registration.mapper.BookingMapper;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BookingService {

    private final RoomService roomService;
    private final GuestService guestService;

    public BookingService() {
        this.roomService = new RoomService();
        this.guestService = new GuestService();
    }


    public void checkin(String lastName) throws ApplicationException {
        var availableRooms = roomService.getAllAvailableRooms().stream().findAny().orElse(null);

        if (availableRooms != null) {
            var guest = guestService.getGuestByName(lastName);
            try (Connection connection = JdbcConnection.connect()) {
                if (connection != null) {
                    String checkExistingBookingQuery = "SELECT COUNT(*) FROM Booking WHERE guest_id = ? AND checkedOut IS NULL";
                    try (PreparedStatement checkStatement = connection.prepareStatement(checkExistingBookingQuery)) {
                        checkStatement.setLong(1, guest.getId());
                        try (ResultSet resultSet = checkStatement.executeQuery()) {
                            if (resultSet.next() && resultSet.getInt(1) == 0) {
                                String insertQuery = "INSERT INTO Booking (guest_id, room_id) VALUES (?, ?)";
                                try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                                    insertStatement.setLong(1, guest.getId());
                                    insertStatement.setLong(2, availableRooms.getId());
                                    insertStatement.executeUpdate();
                                }
                            } else {
                                throw new ApplicationException("Guest already has an active booking.");
                            }
                        }
                    }
                }
            } catch (SQLException e) {
                throw new ApplicationException("Error registering guest into a hotel");
            }
            roomService.setRoomToBeTaken(availableRooms.getId(), guest);
        }
    }


    public void checkout(String fullName) throws ApplicationException {
        String lastName = fullName.split("\\s+")[1];

        var guest = guestService.getGuestByName(lastName);

        try (Connection connection = JdbcConnection.connect()) {
            String updateQuery = "UPDATE booking b SET checkedOut = ? WHERE b.guest_id IN (SELECT g.id FROM guest g WHERE g.lastName = ?)";

            LocalDateTime currentTime = LocalDateTime.now();

            try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                updateStatement.setObject(1, currentTime);
                updateStatement.setString(2, guest.getLastName());
                updateStatement.executeUpdate();
            }
            roomService.setRoomsAvailableForGuest(guest.getLastName());
        } catch (SQLException e) {
            throw new ApplicationException("Error checking out guest and updating room availability");
        }
    }

    public List<Booking> getHistory() throws ApplicationException {
        List<Booking> bookings = new ArrayList<>();

        try (Connection connection = JdbcConnection.connect();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT b.checkedIn, b.checkedOut, g.firstName AS guest_firstName, g.lastName AS guest_lastName, r.id AS room_id, r.available AS room_available FROM booking b INNER JOIN guest g ON b.guest_id = g.id INNER JOIN room r ON b.room_id = r.id")) {

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                bookings.add(BookingMapper.fromResultSetToBooking(
                        String.format(Locale.getDefault(), "%s %s", resultSet.getString("guest_firstName"), resultSet.getString("guest_lastName")),
                        resultSet.getLong("room_id"),
                        resultSet.getBoolean("room_available"),
                        resultSet.getTimestamp("checkedIn").toLocalDateTime().format(DateTimeFormatter.ISO_DATE_TIME),
                        resultSet.getTimestamp("checkedOut") == null ? "" : resultSet.getTimestamp("checkedOut").toLocalDateTime().format(DateTimeFormatter.ISO_DATE_TIME)));
            }
        } catch (SQLException e) {
            throw new ApplicationException("Error retrieving booking history");
        }

        return bookings;
    }

}
