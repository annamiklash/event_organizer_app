package pjatk.pro.event_organizer_app.cateringforchosenevent.mapper;

import lombok.experimental.UtilityClass;
import pjatk.pro.event_organizer_app.catering.mapper.CateringMapper;
import pjatk.pro.event_organizer_app.cateringforchosenevent.model.CateringForChosenEventLocation;
import pjatk.pro.event_organizer_app.cateringforchosenevent.model.dto.CateringForChosenEventLocationDto;
import pjatk.pro.event_organizer_app.common.convertors.Converter;
import pjatk.pro.event_organizer_app.common.util.DateTimeUtil;
import pjatk.pro.event_organizer_app.location.locationforevent.mapper.LocationForEventMapper;

import java.util.stream.Collectors;

import static pjatk.pro.event_organizer_app.enums.ConfirmationStatusEnum.NOT_CONFIRMED;

@UtilityClass
public class CateringForChosenLocationMapper {

    public CateringForChosenEventLocationDto toDto(CateringForChosenEventLocation catering) {
        return CateringForChosenEventLocationDto.builder()
                .id(catering.getId())
                .time(DateTimeUtil.fromLocalTimeToTimeString(catering.getTime()))
                .isOrderConfirmed(catering.isCateringOrderConfirmed())
                .comment(catering.getComment())
                .confirmationStatus(catering.getConfirmationStatus())
                .catering(CateringMapper.toDto(catering.getCatering()))
                .build();
    }

    public CateringForChosenEventLocationDto toDtoWithEvent(CateringForChosenEventLocation catering) {
        final CateringForChosenEventLocationDto dto = toDtoWithOrder(catering);
        dto.setEventLocation(LocationForEventMapper.toDtoWithEvent(catering.getEventLocation()));
        return dto;
    }

    public CateringForChosenEventLocationDto toDtoWithOrder(CateringForChosenEventLocation catering) {
        return CateringForChosenEventLocationDto.builder()
                .id(catering.getId())
                .time(DateTimeUtil.fromLocalTimeToTimeString(catering.getTime()))
                .comment(catering.getComment())
                .isOrderConfirmed(catering.isCateringOrderConfirmed())
                .confirmationStatus(catering.getConfirmationStatus())
                .catering(CateringMapper.toDto(catering.getCatering()))
                .order(catering.getCateringOrder().stream()
                        .map(CateringOrderChoiceMapper::toDtoWithItem)
                        .collect(Collectors.toList()))
                .build();
    }

    public CateringForChosenEventLocation fromDto(CateringForChosenEventLocationDto dto) {
        return CateringForChosenEventLocation.builder()
                .time(DateTimeUtil.fromTimeStringToLocalTime(dto.getTime()))
                .comment(Converter.convertDescriptionsString(dto.getComment()))
                .confirmationStatus(NOT_CONFIRMED.name())
                .isCateringOrderConfirmed(false)
                .build();
    }
}
