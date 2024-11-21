package be.ucll.se.team15backend.manage.model;

import lombok.Getter;


@Getter
public class ManageException extends Exception{

    private final String field;

    public ManageException(String field, String message) {
        super(message);
        this.field = field;
    }

    public String toString() {
        return "ManageException";
    }
}
