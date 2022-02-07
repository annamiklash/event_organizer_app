package pjatk.pro.event_organizer_app.event.service

import com.google.common.collect.ImmutableList
import pjatk.pro.event_organizer_app.event.repository.EventRepository
import pjatk.pro.event_organizer_app.exceptions.NotFoundException
import pjatk.pro.event_organizer_app.trait.event.EventTypeTrait
import spock.lang.Specification
import spock.lang.Subject


class EventTypeServiceTest extends Specification implements EventTypeTrait {

    @Subject
    EventTypeService eventTypeService

    EventRepository eventRepository

    def setup() {
        eventRepository = Mock()

        eventTypeService = new EventTypeService(eventRepository)
    }

    def "FindAll"() {
        given:
        def eventList = [fakeEventType]
        def target = ImmutableList.copyOf(eventList)

        when:
        def result = eventTypeService.findAll()

        then:
        eventRepository.findAll() >> eventList

        target == result

    }

    def "GetByType() positive test scenario"() {
        given:
        def event = fakeEventType
        def type = 'Type'
        def target = event

        when:
        def result = eventTypeService.getByType(type)

        then:
        eventRepository.findByType(type) >> Optional.of(event)

        target == result

    }

    def "GetByType() negative test scenario"() {
        given:
        def type = 'Type'

        when:
        eventTypeService.getByType(type)

        then:
        eventRepository.findByType(type) >> Optional.empty()
        thrown(NotFoundException)

    }
}
