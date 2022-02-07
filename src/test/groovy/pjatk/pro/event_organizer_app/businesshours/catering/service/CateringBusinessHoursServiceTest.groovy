package pjatk.pro.event_organizer_app.businesshours.catering.service

import pjatk.pro.event_organizer_app.businesshours.catering.repository.CateringBusinessHoursRepository
import pjatk.pro.event_organizer_app.trait.businesshours.BusinessHoursTrait
import pjatk.pro.event_organizer_app.trait.catering.CateringTrait
import spock.lang.Specification
import spock.lang.Subject

class CateringBusinessHoursServiceTest extends Specification implements CateringTrait, BusinessHoursTrait {

    @Subject
    CateringBusinessHoursService cateringBusinessHoursService

    CateringBusinessHoursRepository cateringBusinessHoursRepository

    def setup() {
        cateringBusinessHoursRepository = Mock()
        cateringBusinessHoursService = new CateringBusinessHoursService(cateringBusinessHoursRepository)
    }

    def "create() positive scenario"() {
        given:
        def businessHoursDto = fakeBusinessHoursDto
        def businessHoursDtoList = [businessHoursDto]
        def cateringBusinessHours = fakeCateringBusinessHours
        def target = Set.of(fakeCateringBusinessHours)

        when:
        def result = cateringBusinessHoursService.create(businessHoursDtoList)

        then:
        1 * cateringBusinessHoursRepository.save(cateringBusinessHours)

        result == target
    }

    def "delete() positive scenario"() {
        given:
        def cateringBusinessHours = fakeCateringBusinessHours

        when:
        cateringBusinessHoursService.delete(cateringBusinessHours)

        then:
        1 * cateringBusinessHoursRepository.delete(cateringBusinessHours)
    }

    def "edit() positive scenario"() {
        given:
        def id = 1
        def businessHoursDto = fakeBusinessHoursDto
        def cateringBusinessHours = fakeCateringBusinessHoursWithId
        def target = cateringBusinessHours

        when:
        def result = cateringBusinessHoursService.edit(id, businessHoursDto)

        then:
        1 * cateringBusinessHoursRepository.findById(id) >> Optional.of(cateringBusinessHours)
        1 * cateringBusinessHoursRepository.save(cateringBusinessHours)

        result == target
    }
}
