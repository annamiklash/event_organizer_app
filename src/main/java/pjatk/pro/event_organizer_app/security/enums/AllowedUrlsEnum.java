package pjatk.pro.event_organizer_app.security.enums;

import java.util.HashMap;
import java.util.Map;

public enum AllowedUrlsEnum {

    EVENT_TYPES("/api/events/types/allowed/**"),

    CATERINGS("/api/caterings/allowed/**"),
    CATERING_ITEMS("/api/catering/items/allowed**"),
    CATERING_ITEM_TYPES("/api/catering/items/allowed/types"),
    CATERINGS_REVIEW("/api/reviews/catering/allowed/**"),
    CUISINES("/api/cuisines/allowed/**"),

    LOCATIONS("/api/locations/allowed/**"),
    LOCATIONS_REVIEW("/api/reviews/location/allowed/**"),
    LOCATION_AVAILABILITY_1("/api/availability/location/allowed**"),
    LOCATION_AVAILABILITY_2("/api/availability/location/allowed/**"),
    LOCATION_DESCRIPTIONS("/api/location_description/allowed/all"),

    LOGIN("/api/login*"),
    LOGOUT("/api/logout*"),

    REGISTER("/api/register/**"),
    RESET_PASSWORD("/api/reset_password"),
    RESET( "/api/reset"),

    SERVICES("/api/services/allowed/**"),
    SERVICE_REVIEW("/api/reviews/service/allowed/**"),
    SERVICES_AVAILABILITY("/api/availability/services/allowed**");

    public final String value;

    AllowedUrlsEnum(String value) {
        this.value = value;
    }

    private static final Map<String, AllowedUrlsEnum> VALUES = new HashMap<>();

    static {
        for (AllowedUrlsEnum e : values()) {
            VALUES.put(e.value, e);
        }
    }

    public static AllowedUrlsEnum valueOfLabel(String label) {
        return VALUES.get(label);
    }
    }