package pjatk.pro.event_organizer_app.trait.cateringforchosenevent

import pjatk.pro.event_organizer_app.catering.model.CateringItem
import pjatk.pro.event_organizer_app.cateringforchosenevent.model.CateringForChosenEventLocation
import pjatk.pro.event_organizer_app.cateringforchosenevent.model.CateringOrderChoice
import pjatk.pro.event_organizer_app.cateringforchosenevent.model.dto.CateringOrderChoiceDto

import static pjatk.pro.event_organizer_app.enums.ConfirmationStatusEnum.CONFIRMED

trait CateringOrderChoiceTrait {

    CateringOrderChoice fakeCateringOrderChoiceConfirmed = CateringOrderChoice.builder()
            .id(1L)
            .amount(10)
            .item(CateringItem.builder()
                    .name('Name')
                    .itemType('Appetizer')
                    .description('SAMPLE DESCRIPTION')
                    .isVegan(true)
                    .isVegetarian(true)
                    .isGlutenFree(true)
                    .servingPrice(new BigDecimal('123456.00'))
                    .build())
            .eventLocationCatering(CateringForChosenEventLocation.builder()
                    .id(1L)
                    .isCateringOrderConfirmed(true)
                    .confirmationStatus(CONFIRMED.name())
                    .build())
            .build()

    CateringOrderChoice fakeCateringOrderChoiceNotConfirmed = CateringOrderChoice.builder()
            .id(1L)
            .amount(10)
            .item(CateringItem.builder()
                    .name('Name')
                    .itemType('Appetizer')
                    .description('SAMPLE DESCRIPTION')
                    .isVegan(true)
                    .isVegetarian(true)
                    .isGlutenFree(true)
                    .servingPrice(new BigDecimal('123456.00'))
                    .build())
            .eventLocationCatering(CateringForChosenEventLocation.builder()
                    .id(1L)
                    .isCateringOrderConfirmed(false)
                    .confirmationStatus(CONFIRMED.name())
                    .build())
            .build()

    CateringOrderChoiceDto fakeCateringOrderChoiceDto = CateringOrderChoiceDto.builder()
            .id(1L)
            .itemId(1l)
            .amount(10)
            .build()

    CateringOrderChoice fakeCateringOrderChoiceNoId = CateringOrderChoice
            .builder()
            .amount(10)
            .build()
}