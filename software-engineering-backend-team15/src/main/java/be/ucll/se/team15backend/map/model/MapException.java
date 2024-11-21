package be.ucll.se.team15backend.map.model;

public class MapException extends Exception{
    private String field;
    public MapException(String field, String message) {
        super(message);
        this.field = field;
    }

    public String getField() {
        return field;
    }

    public String toString() {
        return "MapException";
    }
}
