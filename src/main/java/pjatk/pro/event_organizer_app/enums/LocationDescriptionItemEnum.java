package pjatk.pro.event_organizer_app.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum LocationDescriptionItemEnum {

    OUTSIDE_CATERING_AVAILABLE("Outside Catering Available"),
    CAN_BRING_OWN_ALCOHOL("Can Bring Own Alcohol"),
    CAN_BRING_OWN_FOOD("Can Bring Own Food"),
    HAS_WIFI("Has WiFi"),
    HAS_PROJECTOR("Has Projector"),
    HAS_STAGE("Has Stage"),
    WHEELCHAIR_ACCESSIBLE("Wheelchair Accessible"),
    HAS_PATIO("Has Patio"),
    SERVES_FOOD("Serves Food");

    private final String value;

    LocationDescriptionItemEnum(String value) {
        this.value = value;
    }

    private static final Map<String, LocationDescriptionItemEnum> VALUES = new HashMap<>();

    static {
        for (LocationDescriptionItemEnum e : values()) {
            VALUES.put(e.value, e);
        }
    }

    public static LocationDescriptionItemEnum valueOfLabel(String label) {
        return VALUES.get(label);
    }

}
