package pjatk.pro.event_organizer_app.event.mapper;

import lombok.experimental.UtilityClass;
import pjatk.pro.event_organizer_app.cateringforchosenevent.mapper.CateringForChosenLocationMapper;
import pjatk.pro.event_organizer_app.common.util.DateTimeUtil;
import pjatk.pro.event_organizer_app.customer.guest.mapper.GuestMapper;
import pjatk.pro.event_organizer_app.customer.mapper.CustomerMapper;
import pjatk.pro.event_organizer_app.event.model.OrganizedEvent;
import pjatk.pro.event_organizer_app.event.model.dto.OrganizedEventConfirmationDto;
import pjatk.pro.event_organizer_app.event.model.dto.OrganizedEventDto;
import pjatk.pro.event_organizer_app.exceptions.NotFoundException;
import pjatk.pro.event_organizer_app.location.locationforevent.mapper.LocationForEventMapper;
import pjatk.pro.event_organizer_app.location.locationforevent.model.LocationForEvent;
import pjatk.pro.event_organizer_app.location.locationforevent.model.dto.LocationForEventDto;
import pjatk.pro.event_organizer_app.optional_service.optional_service_for_location.mapper.OptionalServiceForLocationMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static pjatk.pro.event_organizer_app.enums.EventStatusEnum.CANCELLED;
import static pjatk.pro.event_organizer_app.enums.EventStatusEnum.IN_PROGRESS;

@UtilityClass
public class OrganizedEventMapper {

    public OrganizedEventDto toDto(OrganizedEvent organizedEvent) {
        return OrganizedEventDto.builder()
                .id(organizedEvent.getId())
                .name(organizedEvent.getName())
                .guestCount(organizedEvent.getGuestCount())
                .date(DateTimeUtil.fromLocalDateToDateString(organizedEvent.getDate()))
                .startTime(DateTimeUtil.fromLocalTimeToTimeString(organizedEvent.getStartTime()))
                .endTime(DateTimeUtil.fromLocalTimeToTimeString(organizedEvent.getEndTime()))
                .eventStatus(organizedEvent.getEventStatus())
                .eventType(EventTypeMapper.toDto(organizedEvent.getEventType()).getType())
                .createdAt(DateTimeUtil.fromLocalDateTimetoString(organizedEvent.getCreatedAt()))
                .modifiedAt(DateTimeUtil.fromLocalDateTimetoString(organizedEvent.getModifiedAt()))
                .build();
    }

    public OrganizedEventDto toDtoWithCustomerAndEventType(OrganizedEvent organizedEvent) {
        final OrganizedEventDto dto = toDto(organizedEvent);

        dto.setCustomer(CustomerMapper.toDto(organizedEvent.getCustomer()));
        dto.setEventType(organizedEvent.getEventType().getType());

        final List<LocationForEventDto> locationForEventDtos = new ArrayList<>();
        final Set<LocationForEvent> locationReservations = organizedEvent.getLocationForEvent();
        for (LocationForEvent locationReservation : locationReservations) {
            if (locationReservation.getConfirmationStatus().equals(CANCELLED.name())) {
                locationForEventDtos.add(LocationForEventMapper.toDto(locationReservation));
            } else {
                locationForEventDtos.add(LocationForEventMapper.toDtoWithDetail(locationReservation));
            }
        }
        dto.setLocation(locationForEventDtos);

        return dto;
    }

    public OrganizedEventDto toDtoWithCustomer(OrganizedEvent organizedEvent) {
        return OrganizedEventDto.builder()
                .id(organizedEvent.getId())
                .name(organizedEvent.getName())
                .date(DateTimeUtil.fromLocalDateToDateString(organizedEvent.getDate()))
                .startTime(DateTimeUtil.fromLocalTimeToTimeString(organizedEvent.getStartTime()))
                .endTime(DateTimeUtil.fromLocalTimeToTimeString(organizedEvent.getEndTime()))
                .guestCount(organizedEvent.getGuestCount())
                .eventStatus(organizedEvent.getEventStatus())
                .eventType(EventTypeMapper.toDto(organizedEvent.getEventType()).getType())
                .customer(CustomerMapper.toDto(organizedEvent.getCustomer()))
                .createdAt(DateTimeUtil.fromLocalDateTimetoString(organizedEvent.getCreatedAt()))
                .modifiedAt(DateTimeUtil.fromLocalDateTimetoString(organizedEvent.getModifiedAt()))
                .build();
    }

    public OrganizedEventDto toDtoWithLocationCustomer(OrganizedEvent organizedEvent) {
        final OrganizedEventDto dto = toDto(organizedEvent);

        dto.setCustomer(CustomerMapper.toDto(organizedEvent.getCustomer()));
        dto.setEventType(organizedEvent.getEventType().getType());

        final List<LocationForEventDto> locationForEventDtos = new ArrayList<>();
        final Set<LocationForEvent> locationReservations = organizedEvent.getLocationForEvent();
        for (LocationForEvent locationReservation : locationReservations) {

            locationForEventDtos.add(LocationForEventMapper.toDto(locationReservation));

        }
        dto.setLocation(locationForEventDtos);

        return dto;
    }

    public OrganizedEventConfirmationDto toDtoWithLocation(OrganizedEvent organizedEvent) {
        return OrganizedEventConfirmationDto.builder()
                .id(organizedEvent.getId())
                .name(organizedEvent.getName())
                .eventType(organizedEvent.getEventType().getType())
                .eventStatus(organizedEvent.getEventStatus())
                .locationForEvent(LocationForEventMapper.toDto(organizedEvent.getLocationForEvent().stream()
                        .filter(location -> !CANCELLED.name().equals(location.getConfirmationStatus()))
                        .findFirst()
                        .orElseThrow(() -> new NotFoundException("No current reservation"))))
                .build();
    }

    public OrganizedEventConfirmationDto toDtoWithServices(OrganizedEvent organizedEvent) {
        final OrganizedEventConfirmationDto dto = OrganizedEventConfirmationDto.builder()
                .id(organizedEvent.getId())
                .name(organizedEvent.getName())
                .eventType(organizedEvent.getEventType().getType())
                .eventStatus(organizedEvent.getEventStatus())
                .build();

        final LocationForEvent locationForEvent = organizedEvent.getLocationForEvent().stream()
                .filter(location -> !CANCELLED.name().equals(location.getConfirmationStatus()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("No current reservation"));

        dto.setLocationForEvent(LocationForEventMapper.toDto(locationForEvent));
        dto.setOptionalServices(locationForEvent.getServices().stream()
                .map(OptionalServiceForLocationMapper::toDto)
                .collect(Collectors.toList()));

        return dto;

    }

    public OrganizedEventConfirmationDto toDtoWithCatering(OrganizedEvent organizedEvent) {
        final OrganizedEventConfirmationDto dto = OrganizedEventConfirmationDto.builder()
                .id(organizedEvent.getId())
                .name(organizedEvent.getName())
                .eventType(organizedEvent.getEventType().getType())
                .eventStatus(organizedEvent.getEventStatus())
                .build();

        final LocationForEvent locationForEvent = organizedEvent.getLocationForEvent().stream()
                .filter(location -> !CANCELLED.name().equals(location.getConfirmationStatus()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("No current reservation"));

        dto.setLocationForEvent(LocationForEventMapper.toDto(locationForEvent));
        dto.setCatering(locationForEvent.getCateringsForEventLocation().stream()
                .map(CateringForChosenLocationMapper::toDto)
                .collect(Collectors.toList()));

        return dto;
    }


    public static OrganizedEventDto toDtoWithDetail(OrganizedEvent organizedEvent) {
        final OrganizedEventDto dto = toDto(organizedEvent);
        dto.setCustomer(CustomerMapper.toDto(organizedEvent.getCustomer()));
        dto.setEventType(organizedEvent.getEventType().getType());
        dto.setGuests(organizedEvent.getGuests().stream()
                .map(GuestMapper::toDto)
                .collect(Collectors.toList()));

        final List<LocationForEventDto> locationForEventDtos = new ArrayList<>();
        final Set<LocationForEvent> locationReservations = organizedEvent.getLocationForEvent();
        for (LocationForEvent locationReservation : locationReservations) {
            if (locationReservation.getConfirmationStatus().equals(CANCELLED.name())) {
                locationForEventDtos.add(LocationForEventMapper.toDto(locationReservation));
            } else {
                locationForEventDtos.add(LocationForEventMapper.toDtoWithDetail(locationReservation));
            }
        }

        dto.setLocation(locationForEventDtos);

        return dto;
    }

    public OrganizedEventDto toDtoForInvite(OrganizedEvent organizedEvent) {
        final OrganizedEventDto dto = OrganizedEventDto.builder()
                .id(organizedEvent.getId())
                .name(organizedEvent.getName())
                .date(DateTimeUtil.fromLocalDateToDateString(organizedEvent.getDate()))
                .startTime(DateTimeUtil.fromLocalTimeToTimeString(organizedEvent.getStartTime()))
                .endTime(DateTimeUtil.fromLocalTimeToTimeString(organizedEvent.getStartTime()))
                .eventType(EventTypeMapper.toDto(organizedEvent.getEventType()).getType())
                .customer(CustomerMapper.toDto(organizedEvent.getCustomer()))
                .guests(organizedEvent.getGuests().stream().map(GuestMapper::toDto).collect(Collectors.toList()))
                .build();

        final List<LocationForEventDto> locationForEventDtos = new ArrayList<>();
        final Set<LocationForEvent> locationReservations = organizedEvent.getLocationForEvent();
        for (LocationForEvent locationReservation : locationReservations) {
            if (locationReservation.getConfirmationStatus().equals(CANCELLED.name())) {
                locationForEventDtos.add(LocationForEventMapper.toDto(locationReservation));
            } else {
                locationForEventDtos.add(LocationForEventMapper.toDtoWithDetail(locationReservation));
            }
        }

        dto.setLocation(locationForEventDtos);

        return dto;

    }

    public OrganizedEvent fromDto(OrganizedEventDto dto) {
        return OrganizedEvent.builder()
                .name(dto.getName())
                .date(DateTimeUtil.fromStringToFormattedDate(dto.getDate()))
                .startTime(DateTimeUtil.fromTimeStringToLocalTime(dto.getStartTime()))
                .endTime(DateTimeUtil.fromTimeStringToLocalTime(dto.getEndTime()))
                .guestCount(dto.getGuestCount())
                .eventStatus(IN_PROGRESS.name())
                .build();
    }


}
