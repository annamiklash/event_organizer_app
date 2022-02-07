package pjatk.pro.event_organizer_app.optional_service.optional_service_for_location.mapper;

import lombok.experimental.UtilityClass;
import pjatk.pro.event_organizer_app.common.convertors.Converter;
import pjatk.pro.event_organizer_app.common.util.DateTimeUtil;
import pjatk.pro.event_organizer_app.location.locationforevent.mapper.LocationForEventMapper;
import pjatk.pro.event_organizer_app.optional_service.mapper.OptionalServiceMapper;
import pjatk.pro.event_organizer_app.optional_service.optional_service_for_location.model.OptionalServiceForChosenLocation;
import pjatk.pro.event_organizer_app.optional_service.optional_service_for_location.model.dto.OptionalServiceForChosenLocationDto;

import static pjatk.pro.event_organizer_app.enums.ConfirmationStatusEnum.NOT_CONFIRMED;

@UtilityClass
public class OptionalServiceForLocationMapper {

    public OptionalServiceForChosenLocation fromDto(OptionalServiceForChosenLocationDto dto) {
        return OptionalServiceForChosenLocation.builder()
                .timeFrom(DateTimeUtil.fromTimeStringToLocalTime(dto.getTimeFrom()))
                .timeTo(DateTimeUtil.fromTimeStringToLocalTime(dto.getTimeTo()))
                .comment(Converter.convertDescriptionsString(dto.getComment()))
                .confirmationStatus(NOT_CONFIRMED.name())
                .build();
    }

    public OptionalServiceForChosenLocationDto toDto(OptionalServiceForChosenLocation optionalService) {
        return OptionalServiceForChosenLocationDto.builder()
                .id(optionalService.getId())
                .timeFrom(DateTimeUtil.fromLocalTimeToTimeString(optionalService.getTimeFrom()))
                .timeTo(DateTimeUtil.fromLocalTimeToTimeString(optionalService.getTimeTo()))
                .comment(optionalService.getComment())
                .confirmationStatus(optionalService.getConfirmationStatus())
                .build();
    }


    public OptionalServiceForChosenLocationDto toDtoWithOptionalService(OptionalServiceForChosenLocation optionalService) {
        return OptionalServiceForChosenLocationDto.builder()
                .id(optionalService.getId())
                .timeFrom(DateTimeUtil.fromLocalTimeToTimeString(optionalService.getTimeFrom()))
                .timeTo(DateTimeUtil.fromLocalTimeToTimeString(optionalService.getTimeTo()))
                .comment(optionalService.getComment())
                .confirmationStatus(optionalService.getConfirmationStatus())
                .optionalService(OptionalServiceMapper.toDto(optionalService.getOptionalService()))
                .build();
    }

    public OptionalServiceForChosenLocationDto toDtoWithOptionalServiceAndLocation(OptionalServiceForChosenLocation optionalService) {
        return OptionalServiceForChosenLocationDto.builder()
                .timeFrom(DateTimeUtil.fromLocalTimeToTimeString(optionalService.getTimeFrom()))
                .timeTo(DateTimeUtil.fromLocalTimeToTimeString(optionalService.getTimeTo()))
                .comment(optionalService.getComment())
                .confirmationStatus(optionalService.getConfirmationStatus())
                .optionalService(OptionalServiceMapper.toDto(optionalService.getOptionalService()))
                .locationForEvent(LocationForEventMapper.toDto(optionalService.getLocationForEvent()))
                .build();
    }

    public OptionalServiceForChosenLocationDto toDtoWithLocationAndEvent(OptionalServiceForChosenLocation optionalService) {
        return OptionalServiceForChosenLocationDto.builder()
                .timeFrom(DateTimeUtil.fromLocalTimeToTimeString(optionalService.getTimeFrom()))
                .timeTo(DateTimeUtil.fromLocalTimeToTimeString(optionalService.getTimeTo()))
                .comment(optionalService.getComment())
                .confirmationStatus(optionalService.getConfirmationStatus())
                .optionalService(OptionalServiceMapper.toDto(optionalService.getOptionalService()))
                .locationForEvent(LocationForEventMapper.toDtoWithEvent(optionalService.getLocationForEvent()))
                .build();
    }
}
