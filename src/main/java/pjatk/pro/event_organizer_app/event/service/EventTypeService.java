package pjatk.pro.event_organizer_app.event.service;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pjatk.pro.event_organizer_app.event.model.EventType;
import pjatk.pro.event_organizer_app.event.repository.EventRepository;
import pjatk.pro.event_organizer_app.exceptions.NotFoundException;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class EventTypeService {

    private final EventRepository eventRepository;

    public ImmutableList<EventType> findAll() {
        final List<EventType> all = eventRepository.findAll();
        return ImmutableList.copyOf(all);
    }


    public EventType getByType(String type) {
        return eventRepository.findByType(type)
                .orElseThrow(() -> new NotFoundException("Event type " + type + " does not exist"));

    }
}
