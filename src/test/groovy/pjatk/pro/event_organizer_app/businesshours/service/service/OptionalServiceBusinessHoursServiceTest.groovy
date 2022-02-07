package pjatk.pro.event_organizer_app.businesshours.service.service


import pjatk.pro.event_organizer_app.businesshours.optionalservice.repository.OptionalServiceBusinessHoursRepository
import pjatk.pro.event_organizer_app.businesshours.optionalservice.service.OptionalServiceBusinessHoursService
import pjatk.pro.event_organizer_app.trait.businesshours.BusinessHoursTrait
import spock.lang.Specification
import spock.lang.Subject

class OptionalServiceBusinessHoursServiceTest extends Specification implements BusinessHoursTrait {

    @Subject
    OptionalServiceBusinessHoursService optionalServiceBusinessHoursService

    OptionalServiceBusinessHoursRepository serviceBusinessHoursRepository

    def setup() {
        serviceBusinessHoursRepository = Mock()

        optionalServiceBusinessHoursService = new OptionalServiceBusinessHoursService(serviceBusinessHoursRepository)
    }

    def "create() positive scenario"() {
        given:
        def businessHoursDto = fakeBusinessHoursDto
        def businessHoursDtoList = [businessHoursDto]
        def serviceBusinessHours = fakeServiceBusinessHours
        def target = [fakeServiceBusinessHours]

        when:
        def result = optionalServiceBusinessHoursService.create(businessHoursDtoList)

        then:
        1 * serviceBusinessHoursRepository.save(serviceBusinessHours)

        result == target
    }


    def "delete() positive scenario"() {
        given:
        def serviceBusinessHours = fakeServiceBusinessHours

        when:
        optionalServiceBusinessHoursService.delete(serviceBusinessHours)

        then:
        1 * serviceBusinessHoursRepository.delete(serviceBusinessHours)
    }

    def "edit() positive scenario"() {
        given:
        def id = 1
        def businessHoursDto = fakeBusinessHoursDto
        def serviceBusinessHours = fakeServiceBusinessHoursWithId
        def target = serviceBusinessHours

        when:
        def result = optionalServiceBusinessHoursService.edit(id, businessHoursDto)

        then:
        1 * serviceBusinessHoursRepository.findById(id) >> Optional.of(serviceBusinessHours)
        1 * serviceBusinessHoursRepository.save(serviceBusinessHours)

        result == target
    }

}
