package pjatk.pro.event_organizer_app.exceptions;

public class BusinessVerificationException extends RuntimeException{

    public enum Enum {
        BUSINESS_NOT_VERIFIED, ALREADY_VERIFIED
    }

    public BusinessVerificationException(Enum message) {
        super(String.valueOf(message));
    }
}
