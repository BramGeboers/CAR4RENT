package be.ucll.se.team15backend.bot.model;

public class BotException extends Exception{
    private String field;
    public BotException(String field, String message) {
        super(message);
        this.field = field;
    }

    public String getField() {
        return field;
    }

    public String toString() {
        return "BotException";
    }
}
