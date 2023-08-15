package org.registration.mapper;

import org.registration.domain.entity.Guest;

public class GuestMapper {

    public static Guest fromResultSetToGuest(Long id, String firstName, String lastName) {
        return new Guest(id, firstName, lastName);
    }

}
