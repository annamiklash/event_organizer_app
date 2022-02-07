package pjatk.pro.event_organizer_app.businesshours.mapper;

import lombok.experimental.UtilityClass;
import pjatk.pro.event_organizer_app.businesshours.BusinessHours;
import pjatk.pro.event_organizer_app.businesshours.DayEnum;
import pjatk.pro.event_organizer_app.businesshours.catering.model.CateringBusinessHours;
import pjatk.pro.event_organizer_app.businesshours.dto.BusinessHoursDto;
import pjatk.pro.event_organizer_app.businesshours.location.model.LocationBusinessHours;
import pjatk.pro.event_organizer_app.businesshours.optionalservice.model.OptionalServiceBusinessHours;
import pjatk.pro.event_organizer_app.common.util.DateTimeUtil;

import java.time.LocalTime;

@UtilityClass
public class BusinessHoursMapper {

    public BusinessHoursDto toDto(BusinessHours businessHours) {
        return BusinessHoursDto.builder()
                .id(businessHours.getId())
                .day(DayEnum.valueOf(businessHours.getDay()))
                .timeFrom(DateTimeUtil.fromLocalTimeToTimeString(businessHours.getTimeFrom()))
                .timeTo(DateTimeUtil.fromLocalTimeToTimeString(businessHours.getTimeTo()))
                .build();
    }

    public LocationBusinessHours fromDtoToLocation(BusinessHoursDto dto) {
        return LocationBusinessHours.builder()
                .day(dto.getDay().name())
                .timeFrom(DateTimeUtil.fromTimeStringToLocalTime(dto.getTimeFrom()))
                .timeTo(DateTimeUtil.fromTimeStringToLocalTime(dto.getTimeTo()))
                .build();
    }

    public CateringBusinessHours fromDtoToCatering(BusinessHoursDto dto){
        return CateringBusinessHours.builder()
                .day(dto.getDay().name())
                .timeFrom(LocalTime.parse(dto.getTimeFrom()))
                .timeTo(LocalTime.parse(dto.getTimeTo()))
                .build();
    }

    public OptionalServiceBusinessHours fromDtoToOptionalService(BusinessHoursDto dto){
        return OptionalServiceBusinessHours.builder()
                .day(dto.getDay().name())
                .timeFrom(LocalTime.parse(dto.getTimeFrom()))
                .timeTo(LocalTime.parse(dto.getTimeTo()))
                .build();
    }

}
