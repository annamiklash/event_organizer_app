package pjatk.pro.event_organizer_app.availability.location.service

import pjatk.pro.event_organizer_app.availability.location.model.LocationAvailability
import pjatk.pro.event_organizer_app.availability.location.repository.LocationAvailabilityRepository
import pjatk.pro.event_organizer_app.availability.mapper.AvailabilityMapper
import pjatk.pro.event_organizer_app.common.util.DateTimeUtil
import pjatk.pro.event_organizer_app.exceptions.IllegalArgumentException
import pjatk.pro.event_organizer_app.exceptions.NotFoundException
import pjatk.pro.event_organizer_app.location.repository.LocationRepository
import pjatk.pro.event_organizer_app.trait.availability.AvailabilityTrait
import pjatk.pro.event_organizer_app.trait.availability.LocationAvailabilityTrait
import pjatk.pro.event_organizer_app.trait.location.LocationTrait
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month

class LocationAvailabilityServiceTest extends Specification
        implements LocationAvailabilityTrait,
                LocationTrait,
                AvailabilityTrait {

    @Subject
    LocationAvailabilityService locationAvailabilityService

    LocationAvailabilityRepository locationAvailabilityRepository

    LocationRepository locationRepository

    def setup() {
        locationRepository = Mock()
        locationAvailabilityRepository = Mock()

        locationAvailabilityService = new LocationAvailabilityService(locationAvailabilityRepository, locationRepository)
    }

    def "findAllByLocationIdAndDate positive scenario"() {
        given:
        def id = 1
        def date = '2021-12-31'
        def locationAvailability = fakeLocationAvailability

        when:
        locationAvailabilityService.findAllByLocationIdAndDate(id, date)

        then:
        locationAvailabilityRepository.find(id, date) >> List.of(locationAvailability)

    }

    def "findAllByLocationIdAndDatePeriod positive scenario"() {
        given:
        def id = 1
        def dateFrom = '2022-01-31'
        def dateTo = '2022-02-15'
        def locationAvailability = fakeLocationAvailability

        when:
        locationAvailabilityService.findAllByLocationIdAndDatePeriod(id, dateFrom, dateTo)

        then:
        locationAvailabilityRepository.findByIdAndPeriodDate(id, dateFrom, dateTo) >> List.of(locationAvailability)

    }


    def "findAllByLocationIdAndDatePeriod throws exception"() {
        given:
        def id = 1
        def dateFrom = '2023-01-31'
        def dateTo = '2022-02-15'

        when:
        locationAvailabilityService.findAllByLocationIdAndDatePeriod(id, dateFrom, dateTo)

        then:
        thrown(IllegalArgumentException)
    }

    def "updateToAvailable positive scenario"() {
        given:
        def location = fakeFullLocationWithAvailability

        def newAvailabilities = List.of(
                LocationAvailability.builder()
                        .id(1l)
                        .status('AVAILABLE')
                        .date(LocalDate.of(2022, Month.FEBRUARY, 1))
                        .timeFrom(LocalDateTime.of(2022, Month.FEBRUARY, 1, 9, 0, 0))
                        .timeTo(LocalDateTime.of(2022, Month.FEBRUARY, 1, 13, 0, 0))
                        .location(location)
                        .build(),
                LocationAvailability.builder()
                        .id(2l)
                        .status('NOT_AVAILABLE')
                        .date(LocalDate.of(2022, Month.FEBRUARY, 1))
                        .timeFrom(LocalDateTime.of(2022, Month.FEBRUARY, 1, 13, 0, 0))
                        .timeTo(LocalDateTime.of(2022, Month.FEBRUARY, 1, 18, 0, 0))
                        .location(location)
                        .build(),
                LocationAvailability.builder()
                        .id(3l)
                        .status('AVAILABLE')
                        .date(LocalDate.of(2022, Month.FEBRUARY, 1))
                        .timeFrom(LocalDateTime.of(2022, Month.FEBRUARY, 1, 18, 0, 0))
                        .timeTo(LocalDateTime.of(2022, Month.FEBRUARY, 1, 23, 0, 0))
                        .location(location)
                        .build())

        def availabilityDto = AvailabilityMapper.toDto(newAvailabilities.get(1));
        def locationAvailability = LocationAvailability.builder()
                .status('AVAILABLE')
                .date(LocalDate.of(2022, Month.FEBRUARY, 1))
                .timeFrom(LocalDateTime.of(2022, Month.FEBRUARY, 1, 9, 0, 0))
                .timeTo(LocalDateTime.of(2022, Month.FEBRUARY, 1, 23, 0, 0))
                .location(location)
                .build()

        location.setAvailability(new HashSet<LocationAvailability>(newAvailabilities))
        def locationAvailabilityUpper = newAvailabilities.get(0)
        def locationAvailabilityLower = newAvailabilities.get(2)

        final String timeFrom = DateTimeUtil.joinDateAndTime(availabilityDto.getDate(), availabilityDto.getTimeFrom())
        final String timeTo = DateTimeUtil.joinDateAndTime(availabilityDto.getDate(), availabilityDto.getTimeTo())

        when:
        locationAvailabilityService.updateToAvailable(newAvailabilities.get(1), location)

        then:
        _ * locationAvailabilityRepository.find(location.getId(), availabilityDto.getDate()) >> newAvailabilities
        1 * locationAvailabilityRepository.findByLocationIdAndTimeTo(location.getId(), timeFrom) >> Optional.of(locationAvailabilityUpper)
        1 * locationAvailabilityRepository.findByLocationIdAndTimeFrom(location.getId(), timeTo) >> Optional.of(locationAvailabilityLower)
        1 * locationAvailabilityRepository.save(locationAvailability)
        1 * locationAvailabilityRepository.deleteById(locationAvailabilityUpper.getId())
        1 * locationAvailabilityRepository.deleteById(locationAvailabilityLower.getId())
        _ * locationAvailabilityRepository.flush()
    }

    def "getByDateAndTime positive scenario"() {
        given:
        def timeFrom = '10:00'
        def timeTo = '20:00'
        def date = '2021-12-31'
        def locationAvailability = fakeLocationAvailability

        when:
        locationAvailabilityService.getByDateAndTime(date, timeFrom, timeTo)

        then:
        locationAvailabilityRepository.getByDateAndTime(date, timeFrom, timeTo) >> Optional.of(locationAvailability)
    }

    def "getByDateAndTime negative scenario"() {
        given:
        def timeFrom = '10:00'
        def timeTo = '20:00'
        def date = '2021-12-31'

        when:
        locationAvailabilityService.getByDateAndTime(date, timeFrom, timeTo)

        then:
        thrown(NotFoundException)
        locationAvailabilityRepository.getByDateAndTime(date, timeFrom, timeTo) >> { throw new NotFoundException('') }
    }


    def "delete positive test scenario"() {
        given:
        def locationId = 1
        def availabilityDtos = [fakeAvailabilityDto]
        def date = fakeAvailabilityDto.getDate()
        def locationAvailability = fakeLocationAvailability
        def locationAvailabilities = [fakeLocationAvailability]

        when:
        locationAvailabilityService.delete(availabilityDtos, locationId)

        then:
        1 * locationAvailabilityRepository.findWithStatusAvailable(locationId, date) >> locationAvailabilities
        1 * locationAvailabilityRepository.delete(locationAvailability)
    }
}
