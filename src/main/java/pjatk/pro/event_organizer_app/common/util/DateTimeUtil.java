package pjatk.pro.event_organizer_app.common.util;

import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class DateTimeUtil {

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm";
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String TIME_FORMAT = "HH:mm";

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_FORMAT);

    public LocalDateTime fromStringToFormattedDateTime(String date) {
        if (date == null || date.equals("null")) {
            return null;
        }

        if (date.contains("T")) {
            date = date.replace("T", " ");
        }

        if (date.length() > DATE_TIME_FORMAT.length()) {
            date = date.substring(0, DATE_TIME_FORMAT.length());
        }
        return LocalDateTime.parse(date, DATE_TIME_FORMATTER);

    }

    public LocalDate fromStringToFormattedDate(String date) {
        if (date == null || date.equals("null")) {
            return null;
        }
        return LocalDate.parse(date, DATE_FORMATTER);
    }

    public String fromLocalDateTimetoString(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(DATE_TIME_FORMATTER);
    }

    public LocalDate fromStringToLocalDate(String date) {
        return LocalDate.parse(date, DATE_FORMATTER);
    }

    public String joinDateAndTime(String date, String time) {
        return date + " " + time;
    }

    public String fromLocalTimeToString(LocalTime time) {
        return time.format(TIME_FORMATTER);
    }


    public String fromLocalDateToDateString(LocalDate date) {
        return date.format(DATE_FORMATTER);
    }

    public String fromLocalDateTimeToTimeOnlyString(LocalDateTime dateTime) {
        final LocalTime localTime = dateTime.toLocalTime();

        return localTime.format(TIME_FORMATTER);
    }

    public String fromLocalTimeToTimeString(LocalTime localTime) {

        return localTime.format(TIME_FORMATTER);
    }

    public LocalTime fromTimeStringToLocalTime(String time) {
        if (time == null) {
            return null;
        }
        return LocalTime.parse(time, TIME_FORMATTER);
    }

}
