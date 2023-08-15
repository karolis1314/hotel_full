package org.registration.mapper;

import org.registration.domain.entity.Booking;

public class BookingMapper {


    public static Booking fromResultSetToBooking(String guestFullName, Long roomNumber, boolean availability, String checkin, String checkout) {
        return new Booking(guestFullName, roomNumber, availability, checkin, checkout);
    }
}
