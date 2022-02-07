package pjatk.pro.event_organizer_app.optional_service.optional_service_for_location.service

import pjatk.pro.event_organizer_app.availability.optionalservice.model.OptionalServiceAvailability
import pjatk.pro.event_organizer_app.availability.optionalservice.repository.OptionalServiceAvailabilityRepository
import pjatk.pro.event_organizer_app.availability.optionalservice.service.OptionalServiceAvailabilityService
import pjatk.pro.event_organizer_app.common.helper.TimestampHelper
import pjatk.pro.event_organizer_app.common.util.DateTimeUtil
import pjatk.pro.event_organizer_app.customer.repository.CustomerRepository
import pjatk.pro.event_organizer_app.event.repository.OrganizedEventRepository
import pjatk.pro.event_organizer_app.optional_service.optional_service_for_location.repostory.OptionalServiceForChosenLocationRepository
import pjatk.pro.event_organizer_app.optional_service.service.OptionalServiceService
import pjatk.pro.event_organizer_app.trait.availability.AvailabilityTrait
import pjatk.pro.event_organizer_app.trait.customer.CustomerTrait
import pjatk.pro.event_organizer_app.trait.event.OrganizedEventTrait
import pjatk.pro.event_organizer_app.trait.location.locationforevent.LocationForEventTrait
import pjatk.pro.event_organizer_app.trait.optional_service.OptionalServiceForChosenLocationTrait
import pjatk.pro.event_organizer_app.trait.optional_service.OptionalServiceTrait
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month

class OptionalServiceForLocationServiceTest extends Specification implements OptionalServiceForChosenLocationTrait,
        CustomerTrait, OrganizedEventTrait, OptionalServiceTrait, AvailabilityTrait,
        LocationForEventTrait {

    @Subject
    OptionalServiceForLocationService optionalServiceForLocationService

    OptionalServiceForChosenLocationRepository optionalServiceForChosenLocationRepository
    CustomerRepository customerRepository
    OrganizedEventRepository organizedEventRepository
    OptionalServiceService optionalServiceService
    OptionalServiceAvailabilityRepository optionalServiceAvailabilityRepository
    OptionalServiceAvailabilityService optionalServiceAvailabilityService
    TimestampHelper timestampHelper

    LocalDateTime now = LocalDateTime.parse('2007-12-03T10:15:30')

    def setup() {
        optionalServiceForChosenLocationRepository = Mock()
        customerRepository = Mock()
        organizedEventRepository = Mock()
        optionalServiceService = Mock()
        optionalServiceAvailabilityRepository = Mock()
        optionalServiceAvailabilityService = Mock()
        timestampHelper = Mock()

        timestampHelper.now() >> now

        optionalServiceForLocationService = new OptionalServiceForLocationService(optionalServiceForChosenLocationRepository,
                customerRepository,
                organizedEventRepository,
                optionalServiceService,
                optionalServiceAvailabilityRepository,
                optionalServiceAvailabilityService,
                timestampHelper)
    }

    def "Create"() {
        given:
        def customerId = 1l
        def eventId = 1l
        def serviceId = 1l
        def dto = fakeOptionalServiceForChosenLocationDtoBasic
        def customer = fakeCustomer
        def event = fakeFullOrganizedEvent
        event.setDate(LocalDate.of(2022, Month.FEBRUARY, 1))
        def date = DateTimeUtil.fromLocalDateToDateString(event.getDate())
        def service = fakeOptionalHostWithAvailability
        def availability = service.getAvailability()[0]
        availability.setOptionalService(service)
        def newAvailabilities = List.of(OptionalServiceAvailability.builder()
                .status('AVAILABLE')
                .date(LocalDate.of(2022, Month.FEBRUARY, 1))
                .timeFrom(LocalDateTime.of(2022, Month.FEBRUARY, 1, 9, 0, 0))
                .timeTo(LocalDateTime.of(2022, Month.FEBRUARY, 1, 13, 0, 0))
                .optionalService(service)
                .build(),
                OptionalServiceAvailability.builder()
                        .status('NOT_AVAILABLE')
                        .date(LocalDate.of(2022, Month.FEBRUARY, 1))
                        .timeFrom(LocalDateTime.of(2022, Month.FEBRUARY, 1, 13, 0, 0))
                        .timeTo(LocalDateTime.of(2022, Month.FEBRUARY, 1, 18, 0, 0))
                        .optionalService(service)
                        .build(),
                OptionalServiceAvailability.builder()
                        .status('AVAILABLE')
                        .date(LocalDate.of(2022, Month.FEBRUARY, 1))
                        .timeFrom(LocalDateTime.of(2022, Month.FEBRUARY, 1, 18, 0, 0))
                        .timeTo(LocalDateTime.of(2022, Month.FEBRUARY, 1, 23, 0, 0))
                        .optionalService(service)
                        .build())

        def serviceReservation = fakeOptionalServiceForChosenLocationSimpleNoId
        serviceReservation.setOptionalService(service)
        serviceReservation.setLocationForEvent(null)

        def target = serviceReservation
        when:
        def result = optionalServiceForLocationService.create(customerId, eventId, serviceId, dto)

        then:
        1 * customerRepository.findById(customerId) >> Optional.of(customer)
        1 * organizedEventRepository.getWithLocation(eventId) >> Optional.of(event)
        1 * optionalServiceService.isAvailable(serviceId, date, dto.getTimeFrom(), dto.getTimeTo()) >> Optional.of(service)
        1 * optionalServiceService.get(serviceId) >> service
        1 * optionalServiceAvailabilityRepository.delete(availability)
        1 * optionalServiceAvailabilityRepository.saveAndFlush(newAvailabilities.iterator().next())
        1 * optionalServiceForChosenLocationRepository.save(serviceReservation)

        result == target
    }

    def "ModifyAvailabilityAfterBooking"() {

    }

    def "ConfirmReservation"() {
        given:
        def eventId = 1l
        def serviceId = 1l

        def serviceReservation = fakeOptionalServiceForChosenLocation
        def target = serviceReservation
        target.setConfirmationStatus('CONFIRMED')

        def event = target.getLocationForEvent().getEvent()
        event.setId(1l)
        event.setModifiedAt(now)
        when:
        def result = optionalServiceForLocationService.confirmReservation(serviceId, eventId)

        then:
        1 * optionalServiceForChosenLocationRepository.findByServiceIdAndEventId(serviceId, eventId) >> Optional.of(serviceReservation)
        1 * optionalServiceForChosenLocationRepository.save(target)
        1 * organizedEventRepository.save(event)

        result == target
    }

    def "ListAllByStatus"() {
        given:
        def serviceId = 1l
        def status = 'CONFIRMED'
        def target = [fakeOptionalServiceForChosenLocation]
        when:
        def result = optionalServiceForLocationService.listAllByStatus(serviceId, status)

        then:
        1 * optionalServiceForChosenLocationRepository.findAllByServiceIdAndStatus(serviceId, status) >> target

        result == target
    }

    def "CancelReservation"() {
        given:
        def serviceForEventId = 1l
        def serviceReservation = fakeOptionalServiceForChosenLocation
        def event = serviceReservation.getLocationForEvent().getEvent()
        event.setDate(LocalDate.of(2022, Month.APRIL, 1))
        def timeFrom = serviceReservation.getTimeFrom();
        def timeTo = serviceReservation.getTimeTo();
        def date = event.getDate();
        def dateTime = LocalDateTime.of(date, timeFrom)
        def stringTimeFrom = DateTimeUtil.joinDateAndTime(DateTimeUtil.fromLocalDateToDateString(date), DateTimeUtil.fromLocalTimeToString(timeFrom))
        def stringTimeTo = DateTimeUtil.joinDateAndTime(DateTimeUtil.fromLocalDateToDateString(date), DateTimeUtil.fromLocalTimeToString(timeTo))
        def service = fakeOptionalHostWithAvailability
        def serviceAvailability = Set.of(OptionalServiceAvailability.builder()
                .status('AVAILABLE')
                .date(LocalDate.of(2022, Month.APRIL, 1))
                .timeFrom(LocalDateTime.of(2022, Month.APRIL, 1, 9, 0, 0))
                .timeTo(LocalDateTime.of(2022, Month.APRIL, 1, 13, 0, 0))
                .optionalService(service)
                .build(),
                OptionalServiceAvailability.builder()
                        .status('NOT_AVAILABLE')
                        .date(LocalDate.of(2022, Month.APRIL, 1))
                        .timeFrom(LocalDateTime.of(2022, Month.APRIL, 1, 13, 0, 0))
                        .timeTo(LocalDateTime.of(2022, Month.APRIL, 1, 18, 0, 0))
                        .optionalService(service)
                        .build(),
                OptionalServiceAvailability.builder()
                        .status('AVAILABLE')
                        .date(LocalDate.of(2022, Month.APRIL, 1))
                        .timeFrom(LocalDateTime.of(2022, Month.APRIL, 1, 18, 0, 0))
                        .timeTo(LocalDateTime.of(2022, Month.APRIL, 1, 23, 0, 0))
                        .optionalService(service)
                        .build())
        service.setAvailability(serviceAvailability)
        serviceReservation.setOptionalService(service)

        event.setModifiedAt(now)

        def cancelledAvailability = OptionalServiceAvailability.builder()
                .status('NOT_AVAILABLE')
                .date(LocalDate.of(2022, Month.APRIL, 1))
                .timeFrom(LocalDateTime.of(2022, Month.APRIL, 1, 13, 0, 0))
                .timeTo(LocalDateTime.of(2022, Month.APRIL, 1, 18, 0, 0))
                .optionalService(service)
                .build()

        def cancelledReservation = serviceReservation
        cancelledReservation.setConfirmationStatus('CANCELLED')

        def target = cancelledReservation
        when:
        def result = optionalServiceForLocationService.cancelReservation(serviceForEventId)

        then:
        1 * optionalServiceForChosenLocationRepository.getWithServiceAndEvent(serviceForEventId) >> Optional.of(serviceReservation)
        1 * optionalServiceAvailabilityService.getByDateAndTime(DateTimeUtil.fromLocalDateToDateString(date), stringTimeFrom, stringTimeTo) >> cancelledAvailability
        1 * optionalServiceAvailabilityService.updateToAvailable(cancelledAvailability, serviceReservation.getOptionalService())
        1 * optionalServiceForChosenLocationRepository.save(cancelledReservation);
        1 * organizedEventRepository.save(event)

        result == target

    }

    def "GetWithServiceAndEvent"() {
        given:
        def locationForEventId = 1l
        def reservation = fakeOptionalServiceForChosenLocation
        def target = reservation
        when:
        def result = optionalServiceForLocationService.getWithServiceAndEvent(locationForEventId)

        then:
        1 * optionalServiceForChosenLocationRepository.getWithServiceAndEvent(locationForEventId) >> Optional.of(reservation)

        result == target
    }

    def "ListAllByStatusAndBusinessId"() {
        given:
        def status = 'CONFIRMED'
        def businessId = 1l
        def reservations = [fakeOptionalServiceForChosenLocation]
        def target = reservations
        when:
        def result = optionalServiceForLocationService.listAllByStatusAndBusinessId(businessId, status)

        then:
        1 * optionalServiceForChosenLocationRepository.findAllByBusinessIdAndStatus(businessId, status) >> reservations

        result == target
    }
}
