package org.registration.exception;

import java.sql.SQLException;

public class ApplicationException extends SQLException {

    public ApplicationException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}