package pjatk.pro.event_organizer_app.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum CateringItemTypeEnum {

    ENTREE("Entree"),
    APPETIZER("Appetizer"),
    DESSERT("Dessert"),
    SOUP("Soup"),
    SALAD("Salad");

    private final String value;

    CateringItemTypeEnum(String value) {
        this.value = value;
    }

    private static final Map<String, CateringItemTypeEnum> VALUES = new HashMap<>();

    static {
        for (CateringItemTypeEnum e : values()) {
            VALUES.put(e.value, e);
        }
    }

    public static CateringItemTypeEnum valueOfLabel(String label) {
        return VALUES.get(label);
    }
}
