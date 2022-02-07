package pjatk.pro.event_organizer_app.location.mapper;

import lombok.experimental.UtilityClass;
import pjatk.pro.event_organizer_app.common.convertors.Converter;
import pjatk.pro.event_organizer_app.enums.LocationDescriptionItemEnum;
import pjatk.pro.event_organizer_app.location.model.LocationDescriptionItem;
import pjatk.pro.event_organizer_app.location.model.dto.LocationDescriptionItemDto;

import java.util.stream.Collectors;

@UtilityClass
public class LocationDescriptionItemMapper {

    public LocationDescriptionItemDto toDto(LocationDescriptionItem item) {
        return LocationDescriptionItemDto.builder()
                .id(item.getId())
                .description(item.getDescription())
                .build();
    }

    public LocationDescriptionItemDto toDtoWithLocations(LocationDescriptionItem item) {
        final LocationDescriptionItemDto dto = toDto(item);
        dto.setLocations(item.getLocations().stream().map(LocationMapper::toDto).collect(Collectors.toSet()));

        return dto;
    }

    public LocationDescriptionItem fromDto(LocationDescriptionItemDto dto) {
        return LocationDescriptionItem.builder()
                .id(dto.getId())
                .description(dto.getDescription())
                .build();
    }

    public LocationDescriptionItemEnum toEnum(LocationDescriptionItemDto dto) {
        return Enum.valueOf(LocationDescriptionItemEnum.class, Converter.capitalizeToEnum(dto.getId()));
    }
}
