package pjatk.pro.event_organizer_app.exceptions;

public class ForbiddenAccessException extends RuntimeException{
    public ForbiddenAccessException(String message) {
        super(message);
    }
}
