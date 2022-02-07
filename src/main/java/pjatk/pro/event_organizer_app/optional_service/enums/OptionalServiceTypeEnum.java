package pjatk.pro.event_organizer_app.optional_service.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum OptionalServiceTypeEnum {

    DJ("DJ"),
    HOST("HOST"),
    KIDS_PERFORMER("KIDS PERFORMER"),
    MUSICIAN("MUSICIAN"),
    MUSIC_BAND("MUSIC BAND"),
    OTHER("OTHER"),
    SINGER("SINGER"),
    INTERPRETER("INTERPRETER");

    private final String value;

    OptionalServiceTypeEnum(String value) {
        this.value = value;
    }

    private static final Map<String, OptionalServiceTypeEnum> VALUES = new HashMap<>();

    static {
        for (OptionalServiceTypeEnum e : values()) {
            VALUES.put(e.value, e);
        }
    }

    public static OptionalServiceTypeEnum valueOfLabel(String label) {
        return VALUES.get(label);
    }
}
