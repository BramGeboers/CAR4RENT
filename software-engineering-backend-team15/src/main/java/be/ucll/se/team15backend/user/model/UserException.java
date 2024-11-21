package be.ucll.se.team15backend.user.model;

import lombok.Getter;

@Getter
public class UserException extends Exception{
    private final String field;

    public UserException(String field, String message) {
        super(message);
        this.field = field;
    }

    public String toString() {
        return "UserException";
    }
}
