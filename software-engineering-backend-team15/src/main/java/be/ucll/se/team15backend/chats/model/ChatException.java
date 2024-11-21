package be.ucll.se.team15backend.chats.model;

public class ChatException extends Exception {

    private String field;
    public ChatException(String field, String message) {
        super(message);
        this.field = field;
    }

    public String getField() {
        return field;
    }

    public String toString() {
        return "ChatException";
    }
}
