package pjatk.pro.event_organizer_app.optional_service.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum MusicStyleEnum {

    A_CAPELLA("A CAPELLA"),
    COVER("COVER"),
    ELECTRONIC("ELECTRONIC"),
    JAZZ("JAZZ"),
    OTHER("OTHER"),
    POP("POP"),
    PUNK("PUNK"),
    RNB("R'N'B"),
    ROCK("ROCK"),
    SOUL("SOUL"),
    TECHNO("TECHNO");

    private final String value;

    MusicStyleEnum(String value) {
        this.value = value;
    }

    private static final Map<String, MusicStyleEnum> VALUES = new HashMap<>();

    static {
        for (MusicStyleEnum e : values()) {
            VALUES.put(e.value, e);
        }
    }

    public static MusicStyleEnum valueOfLabel(String label) {
        return VALUES.get(label);
    }
}