package pjatk.pro.event_organizer_app.optional_service.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum KidPerformerTypeEnum {

    CLOWN("CLOWN"),
    MAGICIAN("MAGICIAN"),
    KID_HOST("KID HOST"),
    OTHER("OTHER");

    private final String value;

    KidPerformerTypeEnum(String value) {
        this.value = value;
    }

    private static final Map<String, KidPerformerTypeEnum> VALUES = new HashMap<>();

    static {
        for (KidPerformerTypeEnum e : values()) {
            VALUES.put(e.value, e);
        }
    }

    public static KidPerformerTypeEnum valueOfLabel(String label) {
        return VALUES.get(label);
    }
}
