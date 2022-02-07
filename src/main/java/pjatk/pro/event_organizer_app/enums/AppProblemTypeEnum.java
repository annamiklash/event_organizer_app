package pjatk.pro.event_organizer_app.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum AppProblemTypeEnum {

    FUNCTIONALITY_ERROR("Functionality error"),

    INAPPROPRIATE_BEHAVIOR("Inappropriate behavior"),

    NOT_RESPONDING("User not responding"),

    ERROR_1("error_1"),

    ERROR_2("error_2"),

    ERROR_3("error_3");

    private final String value;

    AppProblemTypeEnum(String value) {
        this.value = value;
    }

    private static final Map<String, AppProblemTypeEnum> VALUES = new HashMap<>();

    static {
        for (AppProblemTypeEnum e : values()) {
            VALUES.put(e.value, e);
        }
    }

    public static AppProblemTypeEnum valueOfLabel(String label) {
        return VALUES.get(label);
    }
}
