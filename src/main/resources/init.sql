CREATE DATABASE hotel;
\c hotel;

CREATE TABLE Guest
(
    id        SERIAL PRIMARY KEY,
    firstName VARCHAR(255) NOT NULL,
    lastName  VARCHAR(255) NOT NULL
);

CREATE TABLE Room
(
    id        SERIAL PRIMARY KEY,
    available BOOLEAN NOT NULL DEFAULT TRUE,
    guest_id  INT REFERENCES Guest (id)
);

CREATE TABLE Booking
(
    id         SERIAL PRIMARY KEY        NOT NULL,
    guest_id   INT REFERENCES Guest (id) NOT NULL,
    room_id    INT REFERENCES Room (id)  NOT NULL,
    checkedIn  TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    checkedOut TIMESTAMP                 DEFAULT NULL
);

ALTER TABLE Booking
    ADD CONSTRAINT fk_guest FOREIGN KEY (guest_id) REFERENCES Guest (id);
ALTER TABLE Booking
    ADD CONSTRAINT fk_room FOREIGN KEY (room_id) REFERENCES Room (id);
ALTER TABLE Guest
    ADD CONSTRAINT fr_name_plus_sr_name_unique UNIQUE (firstName, lastName);

CREATE INDEX idx_booking_guest_id ON Booking (guest_id);
CREATE INDEX idx_booking_room_id ON Booking (room_id);
CREATE INDEX idx_room_guest_id ON Room (guest_id);

INSERT INTO Room(id, available, guest_id)
VALUES (1, true, null),
       (2, true, null),
       (3, true, null),
       (4, true, null),
       (5, true, null);