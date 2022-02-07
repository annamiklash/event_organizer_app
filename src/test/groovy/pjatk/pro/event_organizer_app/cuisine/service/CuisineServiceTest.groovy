package pjatk.pro.event_organizer_app.cuisine.service

import org.springframework.data.domain.Sort
import pjatk.pro.event_organizer_app.cuisine.mapper.CuisineMapper
import pjatk.pro.event_organizer_app.cuisine.repository.CuisineRepository
import pjatk.pro.event_organizer_app.trait.catering.CuisineTrait
import spock.lang.Specification
import spock.lang.Subject

class CuisineServiceTest extends Specification
        implements CuisineTrait {

    @Subject
    CuisineService cuisineService

    CuisineRepository cuisineRepository

    def setup() {
        cuisineRepository = Mock()

        cuisineService = new CuisineService(cuisineRepository)
    }

    def "List"() {
        given:
        def target = [fakeCuisine]

        when:
        def result = cuisineService.list()

        then:
        1 * cuisineRepository.findAll(Sort.by("name")) >> target

        result == target
    }

    def "Create"() {
        given:
        def dto = fakeCuisineDto
        def name = dto.getName()

        def target = CuisineMapper.fromDto(dto)

        when:
        def result = cuisineService.create(dto)

        then:
        1 * cuisineRepository.existsByName(name) >> false
        1 * cuisineRepository.save(target)

        result == target
    }

    def "GetByName"() {
        given:
        def name = "abc"
        def target = fakeCuisine

        when:
        def result = cuisineService.getByName(name)

        then:
        1 * cuisineRepository.findByName(name) >> target

        result == target
    }
}
