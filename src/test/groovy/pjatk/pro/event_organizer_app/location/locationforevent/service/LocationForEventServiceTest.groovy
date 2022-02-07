package pjatk.pro.event_organizer_app.location.locationforevent.service

import pjatk.pro.event_organizer_app.availability.location.model.LocationAvailability
import pjatk.pro.event_organizer_app.availability.location.service.LocationAvailabilityService
import pjatk.pro.event_organizer_app.common.helper.TimestampHelper
import pjatk.pro.event_organizer_app.common.util.DateTimeUtil
import pjatk.pro.event_organizer_app.customer.repository.CustomerRepository
import pjatk.pro.event_organizer_app.event.repository.OrganizedEventRepository
import pjatk.pro.event_organizer_app.exceptions.ActionNotAllowedException
import pjatk.pro.event_organizer_app.location.locationforevent.repository.LocationForEventRepository
import pjatk.pro.event_organizer_app.location.service.LocationService
import pjatk.pro.event_organizer_app.trait.availability.AvailabilityTrait
import pjatk.pro.event_organizer_app.trait.customer.CustomerTrait
import pjatk.pro.event_organizer_app.trait.event.OrganizedEventTrait
import pjatk.pro.event_organizer_app.trait.location.LocationTrait
import pjatk.pro.event_organizer_app.trait.location.locationforevent.LocationForEventTrait
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month

class LocationForEventServiceTest extends Specification implements LocationForEventTrait,
        CustomerTrait, OrganizedEventTrait, AvailabilityTrait, LocationTrait {

    @Subject
    LocationForEventService locationForEventService

    LocationForEventRepository locationForEventRepository
    OrganizedEventRepository organizedEventRepository
    CustomerRepository customerRepository
    LocationService locationService
    LocationAvailabilityService locationAvailabilityService
    TimestampHelper timestampHelper

    LocalDateTime now = LocalDateTime.parse('2007-12-03T10:15:30')

    def setup() {
        locationForEventRepository = Mock()
        organizedEventRepository = Mock()
        customerRepository = Mock()
        locationService = Mock()
        locationAvailabilityService = Mock()
        timestampHelper = Mock()

        timestampHelper.now() >> now

        locationForEventService = new LocationForEventService(locationForEventRepository, organizedEventRepository,
                customerRepository, locationService, locationAvailabilityService, timestampHelper)
    }

    def "Create"() {
        def customerId = 1l
        def eventId = 1l
        def locationId = 1l
        def dto = createLocationReservationDto
        def customer = fakeCustomer
        def event = fakeFullOrganizedEvent
        event.setDate(LocalDate.of(2022, Month.FEBRUARY, 1))
        def date = DateTimeUtil.fromLocalDateToDateString(event.getDate())
        def timeFrom = DateTimeUtil.joinDateAndTime(date, dto.getTimeFrom());
        def timeTo = DateTimeUtil.joinDateAndTime(date, dto.getTimeTo());
        def location = fakeFullLocationWithAvailability
        def availability = location.getAvailability()[0]
        availability.setLocation(location)

        def newAvailabilities = List.of(
                LocationAvailability.builder()
                        .status('AVAILABLE')
                        .date(LocalDate.of(2022, Month.FEBRUARY, 1))
                        .timeFrom(LocalDateTime.of(2022, Month.FEBRUARY, 1, 9, 0, 0))
                        .timeTo(LocalDateTime.of(2022, Month.FEBRUARY, 1, 13, 0, 0))
                        .location(location)
                        .build(),
                LocationAvailability.builder()
                        .status('NOT_AVAILABLE')
                        .date(LocalDate.of(2022, Month.FEBRUARY, 1))
                        .timeFrom(LocalDateTime.of(2022, Month.FEBRUARY, 1, 13, 0, 0))
                        .timeTo(LocalDateTime.of(2022, Month.FEBRUARY, 1, 18, 0, 0))
                        .location(location)
                        .build(),
                LocationAvailability.builder()
                        .status('AVAILABLE')
                        .date(LocalDate.of(2022, Month.FEBRUARY, 1))
                        .timeFrom(LocalDateTime.of(2022, Month.FEBRUARY, 1, 18, 0, 0))
                        .timeTo(LocalDateTime.of(2022, Month.FEBRUARY, 1, 23, 0, 0))
                        .location(location)
                        .build())

        def locationReservation = fakeLocationForEventCreate
        locationReservation.setLocation(location)
        locationReservation.setEvent(event)

        def target = locationReservation
        when:
        def result = locationForEventService.create(customerId, eventId, locationId, dto)

        then:
        1 * customerRepository.findById(customerId) >> Optional.of(customer)
        1 * organizedEventRepository.findById(eventId) >> Optional.of(event)
        1 * locationService.isAvailable(locationId, date, dto.getTimeFrom(), dto.getTimeTo()) >> true
        1 * locationService.getWithAvailability(locationId, date) >> location
        1 * locationService.modifyAvailabilityAfterBooking(location, date, timeFrom, timeTo)
        1 * locationForEventRepository.save(locationReservation)

        result == target
    }


    def "CancelReservation"() {
        given:
        def locationForEventId = 1l
        def locationReservation = fakeFullLocationForEvent
        def event = locationReservation.getEvent();
        event.setDate(LocalDate.of(2022, Month.APRIL, 1))

        def timeFrom = locationReservation.getTimeFrom()
        def timeTo = locationReservation.getTimeTo()
        def date = event.getDate()

        final String stringTimeFrom = DateTimeUtil.joinDateAndTime(DateTimeUtil.fromLocalDateToDateString(date), DateTimeUtil.fromLocalTimeToString(timeFrom));
        final String stringTimeTo = DateTimeUtil.joinDateAndTime(DateTimeUtil.fromLocalDateToDateString(date), DateTimeUtil.fromLocalTimeToString(timeTo));

        def location = fakeFullLocationWithAvailability
        def locationAvailabilities = Set.of(LocationAvailability.builder()
                .status('AVAILABLE')
                .date(LocalDate.of(2022, Month.APRIL, 1))
                .timeFrom(LocalDateTime.of(2022, Month.APRIL, 1, 9, 0, 0))
                .timeTo(LocalDateTime.of(2022, Month.APRIL, 1, 13, 0, 0))
                .location(location)
                .build(),
                LocationAvailability.builder()
                        .status('NOT_AVAILABLE')
                        .date(LocalDate.of(2022, Month.APRIL, 1))
                        .timeFrom(LocalDateTime.of(2022, Month.APRIL, 1, 13, 0, 0))
                        .timeTo(LocalDateTime.of(2022, Month.APRIL, 1, 18, 0, 0))
                        .location(location)
                        .build(),
                LocationAvailability.builder()
                        .status('AVAILABLE')
                        .date(LocalDate.of(2022, Month.APRIL, 1))
                        .timeFrom(LocalDateTime.of(2022, Month.APRIL, 1, 18, 0, 0))
                        .timeTo(LocalDateTime.of(2022, Month.APRIL, 1, 23, 0, 0))
                        .location(location)
                        .build())
        location.setAvailability(locationAvailabilities)
        locationReservation.setLocation(location)

        event.setModifiedAt(now)

        def cancelledAvailability = LocationAvailability.builder()
                .status('NOT_AVAILABLE')
                .date(LocalDate.of(2022, Month.APRIL, 1))
                .timeFrom(LocalDateTime.of(2022, Month.APRIL, 1, 13, 0, 0))
                .timeTo(LocalDateTime.of(2022, Month.APRIL, 1, 18, 0, 0))
                .location(location)
                .build()

        def cancelledReservation = locationReservation
        cancelledReservation.setConfirmationStatus('CANCELLED')

        def target = cancelledReservation

        when:
        def result = locationForEventService.cancelReservation(locationForEventId)

        then:
        1 * locationForEventRepository.getWithLocationAndEvent(locationForEventId) >> Optional.of(locationReservation)
        1 * locationAvailabilityService.getByDateAndTime(DateTimeUtil.fromLocalDateToDateString(date), stringTimeFrom, stringTimeTo) >> cancelledAvailability
        1 * locationAvailabilityService.updateToAvailable(cancelledAvailability, locationReservation.getLocation())
        1 * locationForEventRepository.save(cancelledReservation);
        1 * organizedEventRepository.save(event);

        result == target
    }

    def "CancelReservation with Service"() {
        given:
        def locationForEventId = 1l
        def locationReservation = fakeFullLocationForEventWithService


        when:
       locationForEventService.cancelReservation(locationForEventId)

        then:
        1 * locationForEventRepository.getWithLocationAndEvent(locationForEventId) >> Optional.of(locationReservation)
        thrown(ActionNotAllowedException)

    }

    def "CancelReservation with Catering"() {
        given:
        def locationForEventId = 1l
        def locationReservation = fakeFullLocationForEventWithCatering


        when:
        locationForEventService.cancelReservation(locationForEventId)

        then:
        1 * locationForEventRepository.getWithLocationAndEvent(locationForEventId) >> Optional.of(locationReservation)
        thrown(ActionNotAllowedException)

    }

    def "ConfirmReservation"() {
        given:
        def locationId = 1l
        def eventId = 1l

        def locationForEvent = fakeFullLocationForEvent

        def confirmedLocationForEvent = locationForEvent
        confirmedLocationForEvent.setConfirmationStatus('CONFIRMED')
        def event = locationForEvent.getEvent()
        event.setModifiedAt(now)

        def target = confirmedLocationForEvent

        when:
        def result = locationForEventService.confirmReservation(locationId, eventId)

        then:
        1 * locationForEventRepository.findByEventIdAndLocationId(eventId, locationId) >> Optional.of(locationForEvent)
        1 * locationForEventRepository.save(confirmedLocationForEvent)
        1 * organizedEventRepository.save(event)

        result == target
    }

    def "ListAllByStatus"() {
        given:
        def locationId = 1l
        def status = 'CONFIRMED'

        def locationForEvent = fakeFullLocationForEvent
        def target = [locationForEvent]

        when:
        def result = locationForEventService.listAllByStatus(locationId, status)

        then:
        1 * locationForEventRepository.findAllByLocationIdAndStatus(locationId, status) >> target

        result == target
    }

    def "ListAllByStatusAndBusinessId"() {
        given:
        def businessId = 1l
        def status = 'CONFIRMED'

        def locationForEvent = fakeFullLocationForEvent
        def target = [locationForEvent]

        when:
        def result = locationForEventService.listAllByStatusAndBusinessId(businessId, status)

        then:
        1 * locationForEventRepository.findAllBusinessIdAndStatus(businessId, status) >> target

        result == target
    }

    def "FindByEventId"() {
        given:
        def eventId = 1l

        def locationForEvent = fakeFullLocationForEvent
        def target = locationForEvent

        when:
        def result = locationForEventService.findByEventId(eventId)

        then:
        1 * locationForEventRepository.findByEventId(eventId) >> Optional.of(target)

        result == target
    }
}
