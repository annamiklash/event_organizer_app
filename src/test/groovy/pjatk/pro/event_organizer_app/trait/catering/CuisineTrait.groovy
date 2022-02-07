package pjatk.pro.event_organizer_app.trait.catering

import pjatk.pro.event_organizer_app.cuisine.model.Cuisine
import pjatk.pro.event_organizer_app.cuisine.model.dto.CuisineDto

trait CuisineTrait {

    Cuisine fakeCuisine = Cuisine.builder()
            .id(1L)
            .name('Greek')
            .build()

    CuisineDto fakeCuisineDto = CuisineDto.builder()
            .id(1L)
            .name('Greek')
            .build()
}