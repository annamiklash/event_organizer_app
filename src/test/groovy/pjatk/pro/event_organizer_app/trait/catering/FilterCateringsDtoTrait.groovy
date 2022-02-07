package pjatk.pro.event_organizer_app.trait.catering

import com.google.common.collect.ImmutableList
import pjatk.pro.event_organizer_app.catering.model.dto.FilterCateringsDto

trait FilterCateringsDtoTrait {

    FilterCateringsDto fakeFilterCateringsDto = FilterCateringsDto.builder()
            .cuisines(ImmutableList.of('Greek'))
            .minPrice(10)
            .maxPrice(25)
            .city("Warszawa")
            .date("01.01.2022")
            .build()


}