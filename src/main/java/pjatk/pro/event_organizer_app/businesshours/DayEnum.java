package pjatk.pro.event_organizer_app.businesshours;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public enum DayEnum implements Serializable {

    MONDAY("MONDAY"),
    TUESDAY("TUESDAY"),
    WEDNESDAY("WEDNESDAY"),
    THURSDAY("THURSDAY"),
    FRIDAY("FRIDAY"),
    SATURDAY("SATURDAY"),
    SUNDAY("SUNDAY");

    private final String value;

    DayEnum(String value) {
        this.value = value;
    }

    private static final Map<String, DayEnum> VALUES = new HashMap<>();

    static {
        for (DayEnum e : values()) {
            VALUES.put(e.value, e);
        }
    }

    public static DayEnum valueOfLabel(String label) {
        return VALUES.get(label);
    }

}
