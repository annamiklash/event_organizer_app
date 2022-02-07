package pjatk.pro.event_organizer_app.cuisine.mapper;

import lombok.experimental.UtilityClass;
import pjatk.pro.event_organizer_app.cuisine.model.Cuisine;
import pjatk.pro.event_organizer_app.cuisine.model.dto.CuisineDto;

@UtilityClass
public class CuisineMapper {

    public CuisineDto toDto(Cuisine cuisine) {
        return CuisineDto.builder()
                .name(cuisine.getName())
                .build();
    }

    public Cuisine fromDto(CuisineDto dto) {
        return Cuisine.builder()
                .name(dto.getName())
                .build();
    }
}
