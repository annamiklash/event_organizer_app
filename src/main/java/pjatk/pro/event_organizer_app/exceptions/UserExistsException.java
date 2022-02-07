package pjatk.pro.event_organizer_app.exceptions;

public class UserExistsException extends RuntimeException {

    public enum ENUM {
        USER_EXISTS
    }

    public UserExistsException(Enum message) {
        super(String.valueOf(message));
    }

}
