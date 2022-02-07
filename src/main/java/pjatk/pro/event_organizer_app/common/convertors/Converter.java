package pjatk.pro.event_organizer_app.common.convertors;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Slf4j
@UtilityClass
public class Converter {

    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);

    public BigDecimal convertPriceString(String inputPrice) {
        String format = "";
        try {
            format = String.format("%.2f", Double.parseDouble(inputPrice));
        } catch (NullPointerException e) {
            return null;
        }
        return new BigDecimal(format);
    }

    public BigInteger convertPhoneNumberString(@NonNull String phoneNumber) {
        return new BigInteger(phoneNumber);
    }

    public String convertDescriptionsString(String description) {
        return Optional.ofNullable(description)
                .filter(s -> !s.isEmpty()).orElse(null);
    }

    public LocalDateTime fromStringToFormattedDateTime(String date) {
        if (date == null || date.equals("null")) {
            return null;
        }
        log.info(date);

        if (date.contains("T")) {
            date = date.replace("T", " ");
        }

        if (date.length() > DATE_TIME_FORMAT.length()) {
            date = date.substring(0, DATE_TIME_FORMAT.length());
        }
        return LocalDateTime.parse(date, DATE_TIME_FORMATTER);

    }

    public String capitalizeToEnum(String input) {
        return input.toUpperCase().replace(" ", "_");
    }
}
