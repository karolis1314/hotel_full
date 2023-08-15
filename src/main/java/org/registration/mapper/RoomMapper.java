package org.registration.mapper;

import org.registration.domain.entity.Room;

public class RoomMapper {

    public static Room fromResultSetToRoom(Long id, boolean available) {
        return new Room(id, available);
    }

    public static Room fromResultSetToRoomWithGuest(Long id, String guestName) {
        return new Room(id, guestName);
    }
}
