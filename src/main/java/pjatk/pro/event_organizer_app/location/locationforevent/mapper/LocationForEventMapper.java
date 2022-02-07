package pjatk.pro.event_organizer_app.location.locationforevent.mapper;

import lombok.experimental.UtilityClass;
import org.apache.commons.collections4.CollectionUtils;
import pjatk.pro.event_organizer_app.cateringforchosenevent.mapper.CateringForChosenLocationMapper;
import pjatk.pro.event_organizer_app.common.util.DateTimeUtil;
import pjatk.pro.event_organizer_app.event.mapper.OrganizedEventMapper;
import pjatk.pro.event_organizer_app.location.locationforevent.model.LocationForEvent;
import pjatk.pro.event_organizer_app.location.locationforevent.model.dto.LocationForEventDto;
import pjatk.pro.event_organizer_app.location.mapper.LocationMapper;
import pjatk.pro.event_organizer_app.optional_service.optional_service_for_location.mapper.OptionalServiceForLocationMapper;

import java.util.stream.Collectors;

import static pjatk.pro.event_organizer_app.enums.ConfirmationStatusEnum.NOT_CONFIRMED;

@UtilityClass
public class LocationForEventMapper {

    public LocationForEventDto toDto(LocationForEvent location) {
        return LocationForEventDto.builder()
                .id(location.getId())
                .date(DateTimeUtil.fromLocalDateToDateString(location.getEvent().getDate()))
                .timeFrom(DateTimeUtil.fromLocalTimeToTimeString(location.getTimeFrom()))
                .timeTo(DateTimeUtil.fromLocalTimeToTimeString(location.getTimeTo()))
                .guestCount(location.getGuestCount())
                .confirmationStatus(location.getConfirmationStatus())
                .location(LocationMapper.toDto(location.getLocation()))
                .build();


    }

    public LocationForEventDto toDtoWithEvent(LocationForEvent location) {
        final LocationForEventDto dto = toDto(location);
        dto.setEvent(OrganizedEventMapper.toDto(location.getEvent()));

        return dto;
    }

    public static LocationForEventDto toDtoWithLocationAndEvent(LocationForEvent location) {
        final LocationForEventDto dto = toDto(location);
        dto.setEvent(OrganizedEventMapper.toDtoWithCustomer(location.getEvent()));

        return dto;
    }

    public static LocationForEventDto toDtoWithCatering(LocationForEvent location) {
        final LocationForEventDto dto = toDto(location);

        dto.setCaterings(location.getCateringsForEventLocation().stream()
                .map(CateringForChosenLocationMapper::toDto)
                .collect(Collectors.toList()));

        return dto;
    }

    public static LocationForEventDto toDtoWithDetail(LocationForEvent location) {
        final LocationForEventDto dto = toDto(location);

        if (!CollectionUtils.isEmpty(location.getCateringsForEventLocation())) {
            dto.setCaterings(location.getCateringsForEventLocation().stream()
                    .map(CateringForChosenLocationMapper::toDtoWithOrder)
                    .collect(Collectors.toList()));
        }

        if (!CollectionUtils.isEmpty(location.getServices())) {
            dto.setOptionalServices(location.getServices().stream()
                    .map(OptionalServiceForLocationMapper::toDtoWithOptionalService)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    public LocationForEvent fromDto(LocationForEventDto dto) {
        return LocationForEvent.builder()
                .guestCount(dto.getGuestCount())
                .timeFrom(DateTimeUtil.fromTimeStringToLocalTime(dto.getTimeFrom()))
                .timeTo(DateTimeUtil.fromTimeStringToLocalTime(dto.getTimeTo()))
                .confirmationStatus(NOT_CONFIRMED.name())
                .build();
    }
}
