package pjatk.pro.event_organizer_app.availability.mapper;

import lombok.experimental.UtilityClass;
import pjatk.pro.event_organizer_app.availability.Availability;
import pjatk.pro.event_organizer_app.availability.dto.AvailabilityDto;
import pjatk.pro.event_organizer_app.availability.location.model.LocationAvailability;
import pjatk.pro.event_organizer_app.availability.optionalservice.model.OptionalServiceAvailability;
import pjatk.pro.event_organizer_app.common.util.DateTimeUtil;

@UtilityClass
public class AvailabilityMapper {

    public AvailabilityDto toDto(Availability availability) {
        return AvailabilityDto.builder()
                .id(availability.getId())
                .date(DateTimeUtil.fromLocalDateToDateString(availability.getDate()))
                .timeFrom(DateTimeUtil.fromLocalDateTimeToTimeOnlyString(availability.getTimeFrom()))
                .timeTo(DateTimeUtil.fromLocalDateTimeToTimeOnlyString(availability.getTimeTo()))
                .status(availability.getStatus())
                .build();
    }

    public LocationAvailability fromDtoToLocationAvailability(AvailabilityDto dto) {
        return LocationAvailability.builder()
                .date(DateTimeUtil.fromStringToLocalDate(dto.getDate()))
                .timeFrom(DateTimeUtil.fromStringToFormattedDateTime(DateTimeUtil.joinDateAndTime(dto.getDate(), dto.getTimeFrom())))
                .timeTo(DateTimeUtil.fromStringToFormattedDateTime(DateTimeUtil.joinDateAndTime(dto.getDate(), dto.getTimeTo())))
                .status(dto.getStatus())
                .build();
    }

    public OptionalServiceAvailability fromDtoToOptionalServiceAvailability(AvailabilityDto dto) {
        return OptionalServiceAvailability.builder()
                .date(DateTimeUtil.fromStringToLocalDate(dto.getDate()))
                .timeFrom(DateTimeUtil.fromStringToFormattedDateTime(DateTimeUtil.joinDateAndTime(dto.getDate(), dto.getTimeFrom())))
                .timeTo(DateTimeUtil.fromStringToFormattedDateTime(DateTimeUtil.joinDateAndTime(dto.getDate(), dto.getTimeTo())))
                .status(dto.getStatus())
                .build();
    }


}
