package pjatk.pro.event_organizer_app.optional_service.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum LanguagesEnum {

    SPANISH("SPANISH"),
    ENGLISH("ENGLISH"),
    POLISH("POLISH"),
    RUSSIAN("RUSSIAN"),
    GERMAN("GERMAN"),
    JAPANESE("JAPANESE"),
    ASL("ASL");

    private final String value;

    LanguagesEnum(String value) {
        this.value = value;
    }

    private static final Map<String, LanguagesEnum> VALUES = new HashMap<>();

    static {
        for (LanguagesEnum e : values()) {
            VALUES.put(e.value, e);
        }
    }

    public static LanguagesEnum valueOfLabel(String label) {
        return VALUES.get(label);
    }
}
