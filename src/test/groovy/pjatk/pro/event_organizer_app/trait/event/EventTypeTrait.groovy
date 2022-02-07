package pjatk.pro.event_organizer_app.trait.event

import pjatk.pro.event_organizer_app.event.model.EventType
import pjatk.pro.event_organizer_app.event.model.dto.EventTypeDto

trait EventTypeTrait {

    EventType fakeEventType = EventType.builder()
            .id(1)
            .type('Party')
            .build();

    EventTypeDto fakeEventTypeDto = EventTypeDto.builder()
            .type('Party')
            .build();

}