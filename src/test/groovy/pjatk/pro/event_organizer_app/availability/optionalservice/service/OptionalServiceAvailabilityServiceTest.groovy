package pjatk.pro.event_organizer_app.availability.optionalservice.service


import pjatk.pro.event_organizer_app.availability.optionalservice.repository.OptionalServiceAvailabilityRepository
import pjatk.pro.event_organizer_app.exceptions.IllegalArgumentException
import pjatk.pro.event_organizer_app.optional_service.service.OptionalServiceService
import pjatk.pro.event_organizer_app.trait.availability.AvailabilityTrait
import pjatk.pro.event_organizer_app.trait.availability.optionalservice.OptionalServiceAvailabilityTrait
import pjatk.pro.event_organizer_app.trait.optional_service.OptionalServiceTrait
import spock.lang.Specification
import spock.lang.Subject

class OptionalServiceAvailabilityServiceTest extends Specification
        implements AvailabilityTrait,
                OptionalServiceTrait,
                OptionalServiceAvailabilityTrait {

    @Subject
    OptionalServiceAvailabilityService optionalServiceAvailabilityService

    OptionalServiceAvailabilityRepository optionalServiceAvailabilityRepository
    OptionalServiceService optionalServiceService

    def setup() {
        optionalServiceAvailabilityRepository = Mock()
        optionalServiceService = Mock()

        optionalServiceAvailabilityService = new OptionalServiceAvailabilityService(
                optionalServiceAvailabilityRepository,
                optionalServiceService
        )
    }
    //todo: implement
    def "Update"() {


    }

    def "findAllByServiceIdAndDatePeriod positive scenario"() {
        given:
        def id = 1
        def dateFrom = '2022-01-31'
        def dateTo = '2022-02-15'
        def serviceAvailability = fakeOptionalServiceAvailability

        when:
        optionalServiceAvailabilityService.findAllByServiceIdAndDatePeriod(id, dateFrom, dateTo)

        then:
        optionalServiceAvailabilityRepository.findByIdAndPeriodDate(id, dateFrom, dateTo) >> List.of(serviceAvailability)

    }

    def "findAllByServiceIdAndDatePeriod throws exception"() {
        given:
        def id = 1
        def dateTo = '2022-01-31'
        def dateFrom = '2022-02-15'

        when:
        optionalServiceAvailabilityService.findAllByServiceIdAndDatePeriod(id, dateFrom, dateTo)

        then:
        thrown(IllegalArgumentException)
    }

    def "FindAllByServiceIdAndDate"() {
        given:
        def id = 1L
        def date = "11.22.1964"

        def target = [fakeOptionalServiceAvailability]

        when:
        def result = optionalServiceAvailabilityService.findAllByServiceIdAndDate(id, date)

        then:
        1 * optionalServiceAvailabilityRepository.findAvailabilitiesByServiceIdAndDate(id, date) >> target

        result == target

    }

    def "Delete"() {
        given:
        def locationId = 1L
        def date = "11.22.63"
        def optionalServiceAvailability = fakeOptionalServiceAvailability

        when:
        optionalServiceAvailabilityService.delete(locationId, date)

        then:
        1 * optionalServiceAvailabilityRepository.findAvailabilitiesByServiceIdAndDate(locationId, date) >> [fakeOptionalServiceAvailability]
        1 * optionalServiceAvailabilityRepository.delete(optionalServiceAvailability)
    }

    def "deleteById"() {
        given:
        def id = 1l

        when:
        optionalServiceAvailabilityService.deleteById(id)

        then:
        1 * optionalServiceAvailabilityRepository.deleteById(id)
        1 * optionalServiceAvailabilityRepository.flush()
    }

    def "GetByDateAndTime"() {
        given:
        def date = "11.22.63"
        def timeFrom = "10:00"
        def timeTo = "15:00"
        def target = fakeOptionalServiceAvailability

        when:
        def result = optionalServiceAvailabilityService.getByDateAndTime(date, timeFrom, timeTo)

        then:
        1 * optionalServiceAvailabilityRepository.getByDateAndTime(date, timeFrom, timeTo) >> Optional.of(target)

        result == target

    }
}
