package pjatk.pro.event_organizer_app.event.mapper;

import lombok.experimental.UtilityClass;
import pjatk.pro.event_organizer_app.event.model.EventType;
import pjatk.pro.event_organizer_app.event.model.dto.EventTypeDto;

@UtilityClass
public class EventTypeMapper {

    public EventTypeDto toDto(EventType eventType) {
        return EventTypeDto.builder()
                .id(eventType.getId())
                .type(eventType.getType())
                .build();
    }

    public EventType fromDto(EventTypeDto dto) {
        return EventType.builder()
                .type(dto.getType())
                .build();
    }
}
