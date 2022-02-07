package pjatk.pro.event_organizer_app.catering.service

import com.google.common.collect.ImmutableList
import pjatk.pro.event_organizer_app.catering.repository.CateringItemRepository
import pjatk.pro.event_organizer_app.common.helper.TimestampHelper
import pjatk.pro.event_organizer_app.exceptions.NotFoundException
import pjatk.pro.event_organizer_app.trait.catering.CateringItemTrait
import pjatk.pro.event_organizer_app.trait.catering.CateringTrait
import pjatk.pro.event_organizer_app.trait.page.PageTrait
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDateTime

class CateringItemServiceTest extends Specification implements CateringItemTrait, CateringTrait, PageTrait {

    @Subject
    CateringItemService cateringItemService

    CateringItemRepository cateringItemRepository
    CateringService cateringService
    TimestampHelper timestampHelper

    LocalDateTime now = LocalDateTime.parse('2007-12-03T10:15:30')

    def setup() {
        cateringItemRepository = Mock()
        cateringService = Mock()
        timestampHelper = Mock()

        timestampHelper.now() >> now

        cateringItemService = new CateringItemService(cateringItemRepository, cateringService, timestampHelper)
    }

    def "listAllByCateringId() positive test scenario"() {
        given:
        def cateringId = 1
        def cateringItemList = [fakeCateringItem]
        def target = ImmutableList.copyOf(cateringItemList)

        when:
        def result = cateringItemService.listAllByCateringId(cateringId)

        then:
        1 * cateringItemRepository.findAllByCatering_Id(cateringId) >> cateringItemList

        result == target
    }

    def "get() positive test scenario"() {
        given:
        def id = 1
        def optionalCateringItem = Optional.of(fakeCateringItem)
        def target = fakeCateringItem
        when:
        def result = cateringItemService.get(id)

        then:
        1 * cateringItemRepository.findById(id) >> optionalCateringItem

        result == target
    }

    def "get() negative test scenario"() {
        given:
        def id = 1
        when:
        cateringItemService.get(id)

        then:
        1 * cateringItemRepository.findById(id) >> Optional.empty()

        thrown(NotFoundException)
    }

    def "create() positive test scenario"() {
        given:
        def dto = fakeCateringItemDto
        def cateringId = 1L
        def catering = fakeCateringOffersOutsideCatering
        catering.setModifiedAt(now)

        def target = fakeCateringItem
        target.setCatering(catering)
        target.setCreatedAt(now)
        target.setModifiedAt(now)
        when:
        def result = cateringItemService.create(dto, cateringId)

        then:
        1 * cateringService.get(cateringId) >> catering
        1 * cateringItemRepository.save(target)

        result == target
    }


    def "edit() positive test scenario"() {
        given:
        def cateringItemId = 1L
        def dto = fakeCateringItemDto
        def optionalCateringItem = Optional.of(fakeCateringItem)

        def target = fakeCateringItem
        target.setItemType(dto.getType())
        target.setName(dto.getName())
        target.setDescription(dto.getDescription())
        target.setVegan(dto.getIsVegan())
        target.setVegetarian(dto.getIsVegan())
        target.setGlutenFree(dto.getIsVegan())
        target.setServingPrice(new BigDecimal(123456))
        target.setModifiedAt(now)

        when:
        def result = cateringItemService.edit(cateringItemId, dto)

        then:
        1 * cateringItemRepository.findById(cateringItemId) >> optionalCateringItem
        1 * cateringItemRepository.save(target)

        result == target
    }

    def "delete() positive test scenario"() {
        given:
        def id = 1l

        when:
        cateringItemService.delete(id)

        then:
        1 * cateringItemRepository.deleteById(id)

    }
}
