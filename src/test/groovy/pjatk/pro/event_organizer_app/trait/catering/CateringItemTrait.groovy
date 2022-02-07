package pjatk.pro.event_organizer_app.trait.catering

import pjatk.pro.event_organizer_app.catering.model.CateringItem
import pjatk.pro.event_organizer_app.catering.model.dto.CateringItemDto

trait CateringItemTrait {

    CateringItem fakeCateringItem = CateringItem.builder()
            .name('Name')
            .itemType('Appetizer')
            .description('SAMPLE DESCRIPTION')
            .isVegan(true)
            .isVegetarian(true)
            .isGlutenFree(true)
            .servingPrice(new BigDecimal('123456.00'))
            .build()

    CateringItem fakeCateringItemWithId = CateringItem.builder()
            .id(1)
            .name('Name')
            .build()

    CateringItemDto fakeCateringItemDto = CateringItemDto.builder()
            .name('Name')
            .type('Appetizer')
            .description('SAMPLE DESCRIPTION')
            .isVegan(true)
            .isVegetarian(true)
            .isGlutenFree(true)
            .servingPrice('123456.00')
            .build()

}