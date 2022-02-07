package pjatk.pro.event_organizer_app.common.constants;

public class RegexConstants {

    public static final String PL_ZIP_REGEX = "^\\d{2}[-]{0,1}\\d{3}$";
    public static final String COUNTRY_REGEX = "^[A-Z][a-z]+( [A-Z][a-z]+)*$";
    public static final String CITY_REGEX = "^[a-zA-Z]+(?:[\\s-][a-zA-Z]+)*$";
    public static final String STREET_REGEX = "[\\w\\s]+";
    public static final String FIRST_NAME_REGEX = "^[A-Za-z]+((\\s)?((\\'|\\-|\\.)?([A-Za-z])+))*$";
    public static final String LAST_NAME_REGEX = "([A-Z][a-zA-Z]*)";
    public static final String PRICE_REGEX = "^\\d+(.\\d{1,2})?$";
    public static final String PHONE_NUMBER_REGEX = "^\\d{9}$";
    public static final String NAME_REGEX = "^(?=\\s*\\S).*$";
    /*
        ^                 # start-of-string
        (?=.*[0-9])       # a digit must occur at least once
        (?=.*[a-z])       # a lower case letter must occur at least once
        (?=.*[A-Z])       # an upper case letter must occur at least once
        (?=.*[@#$%!*^&+=])# a special character must occur at least once
        (?=\S+$)          # no whitespace allowed in the entire string
        .{8,}             # anything, at least eight places though
        $                 # end-of-string
     */
    public static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%!*^&+=])(?=\\S+$).{8,}$";
    public static final String DATE_REGEX = "^([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))$";
    public static final String TIME_REGEX = "^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$";
    public static final String TIME_REGEX_WITH_SECONDS = "^(((([0-1][0-9])|(2[0-3])):?[0-5][0-9]:?[0-5][0-9]+$))";
    public static final String TIMESTAMP_REGEX = "^([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))$";


}
