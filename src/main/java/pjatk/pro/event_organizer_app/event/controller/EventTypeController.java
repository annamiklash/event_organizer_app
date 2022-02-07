package pjatk.pro.event_organizer_app.event.controller;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pjatk.pro.event_organizer_app.event.mapper.EventTypeMapper;
import pjatk.pro.event_organizer_app.event.model.EventType;
import pjatk.pro.event_organizer_app.event.model.dto.EventTypeDto;
import pjatk.pro.event_organizer_app.event.service.EventTypeService;

@Slf4j
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("api/events/types")
public class EventTypeController {

    private final EventTypeService eventTypeService;

    @RequestMapping(
            method = RequestMethod.GET,
            path = "allowed/all",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<EventTypeDto>> findAll() {
        final ImmutableList<EventType> eventTypeList = eventTypeService.findAll();
        final ImmutableList<EventTypeDto> resultList = eventTypeList.stream()
                .map(EventTypeMapper::toDto)
                .collect(ImmutableList.toImmutableList());

        return ResponseEntity.ok(resultList);
    }
}
