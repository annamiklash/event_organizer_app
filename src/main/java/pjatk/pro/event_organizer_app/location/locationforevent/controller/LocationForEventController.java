package pjatk.pro.event_organizer_app.location.locationforevent.controller;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pjatk.pro.event_organizer_app.event.mapper.OrganizedEventMapper;
import pjatk.pro.event_organizer_app.event.model.dto.OrganizedEventConfirmationDto;
import pjatk.pro.event_organizer_app.location.locationforevent.mapper.LocationForEventMapper;
import pjatk.pro.event_organizer_app.location.locationforevent.model.LocationForEvent;
import pjatk.pro.event_organizer_app.location.locationforevent.model.dto.LocationForEventDto;
import pjatk.pro.event_organizer_app.location.locationforevent.service.LocationForEventService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("api/event/location")
public class LocationForEventController {

    private final LocationForEventService locationForEventService;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'BUSINESS')")
    @RequestMapping(
            method = RequestMethod.PUT,
            path = "confirm",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LocationForEventDto> confirmReservation(@RequestParam long id, @RequestParam long eventId) {

        final LocationForEvent locationForEvent = locationForEventService.confirmReservation(id, eventId);

        return ResponseEntity.ok(LocationForEventMapper.toDto(locationForEvent));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'BUSINESS', 'CUSTOMER')")
    @RequestMapping(
            method = RequestMethod.GET,
            path = "status",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<LocationForEventDto>> listAllByConfirmationStatus(@RequestParam String status,
                                                                                          @RequestParam long locationId) {

        List<LocationForEvent> locationsForEvent = locationForEventService.listAllByStatus(locationId, status);

        return ResponseEntity.ok(
                ImmutableList.copyOf(locationsForEvent.stream()
                        .map(LocationForEventMapper::toDtoWithLocationAndEvent)
                        .collect(Collectors.toList())));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'BUSINESS')")
    @RequestMapping(
            method = RequestMethod.GET,
            path = "business/status",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<LocationForEventDto>> listAllByConfirmationStatusAndBusinessId(@RequestParam String status,
                                                                                                       @RequestParam long businessId) {

        List<LocationForEvent> optionalServices = locationForEventService.listAllByStatusAndBusinessId(businessId, status);

        return ResponseEntity.ok(
                ImmutableList.copyOf(optionalServices.stream()
                        .map(LocationForEventMapper::toDtoWithEvent)
                        .collect(Collectors.toList())));
    }


    @PreAuthorize("hasAnyAuthority('ADMIN', 'CUSTOMER')")
    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrganizedEventConfirmationDto> create(@RequestParam long customerId,
                                                                @RequestParam long eventId,
                                                                @RequestParam long locationId,
                                                                @RequestBody @Valid LocationForEventDto dto) {
        final LocationForEvent locationForEvent = locationForEventService.create(customerId, eventId, locationId, dto);
        return ResponseEntity.ok(OrganizedEventMapper.toDtoWithLocation(locationForEvent.getEvent()));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'BUSINESS', 'CUSTOMER')")
    @RequestMapping(
            path = "cancel",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LocationForEventDto> cancel(@RequestParam long id) {
        final LocationForEvent locationForEvent = locationForEventService.cancelReservation(id);

        return ResponseEntity.ok(LocationForEventMapper.toDtoWithLocationAndEvent(locationForEvent));
    }

}
