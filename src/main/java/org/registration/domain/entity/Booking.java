package org.registration.domain.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Booking {

    private Long id;
    private String guestName;
    private Long roomNumber;
    private boolean availability;
    private String checkedIn;
    private String checkedOut;

    public Booking(String guestName, Long roomNumber, boolean availability, String checkedIn, String checkedOut) {
        this.guestName = guestName;
        this.roomNumber = roomNumber;
        this.availability = availability;
        this.checkedIn = checkedIn;
        this.checkedOut = checkedOut;
    }

    public Booking() {}

    public Long getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(Long roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getGuestName() {
        return guestName;
    }

    public boolean isAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public String getCheckedIn() {
        return checkedIn;
    }

    public void setCheckedIn(String checkedIn) {
        this.checkedIn = checkedIn;
    }

    public String getCheckedOut() {
        return checkedOut;
    }

    public void setCheckedOut(String checkedOut) {
        this.checkedOut = checkedOut;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Booking booking = (Booking) o;

        return new EqualsBuilder().append(id, booking.id).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).toHashCode();
    }
}