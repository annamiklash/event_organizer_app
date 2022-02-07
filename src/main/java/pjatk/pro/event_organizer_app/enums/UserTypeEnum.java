package pjatk.pro.event_organizer_app.enums;

import lombok.Getter;

@Getter
public enum UserTypeEnum {

    CUSTOMER('C'),
    BUSINESS('B'),
    ADMIN('A');

    private final Character value;

    UserTypeEnum(Character value) {
        this.value = value;
    }
}
