package pjatk.pro.event_organizer_app.optional_service.optional_service_for_location.controller;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pjatk.pro.event_organizer_app.event.mapper.OrganizedEventMapper;
import pjatk.pro.event_organizer_app.event.model.OrganizedEvent;
import pjatk.pro.event_organizer_app.event.model.dto.OrganizedEventConfirmationDto;
import pjatk.pro.event_organizer_app.optional_service.optional_service_for_location.mapper.OptionalServiceForLocationMapper;
import pjatk.pro.event_organizer_app.optional_service.optional_service_for_location.model.OptionalServiceForChosenLocation;
import pjatk.pro.event_organizer_app.optional_service.optional_service_for_location.model.dto.OptionalServiceForChosenLocationDto;
import pjatk.pro.event_organizer_app.optional_service.optional_service_for_location.service.OptionalServiceForLocationService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("api/event/service")
public class OptionalServiceForLocationController {

    private final OptionalServiceForLocationService optionalServiceForLocationService;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'BUSINESS')")
    @RequestMapping(
            method = RequestMethod.PUT,
            path = "confirm",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OptionalServiceForChosenLocationDto> confirmReservation(@RequestParam long id,
                                                                                  @RequestParam long eventId) {

        final OptionalServiceForChosenLocation optionalServiceForChosenLocation =
                optionalServiceForLocationService.confirmReservation(id, eventId);

        return ResponseEntity.ok(OptionalServiceForLocationMapper.toDto(optionalServiceForChosenLocation));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'BUSINESS')")
    @RequestMapping(
            method = RequestMethod.GET,
            path = "status",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<OptionalServiceForChosenLocationDto>> listAllByConfirmationStatus(@RequestParam String status,
                                                                                                          @RequestParam long locationId) {

        final List<OptionalServiceForChosenLocation> optionalServiceForChosenLocationList =
                optionalServiceForLocationService.listAllByStatus(locationId, status);
        final ImmutableList<OptionalServiceForChosenLocationDto> resultList = optionalServiceForChosenLocationList.stream()
                .map(OptionalServiceForLocationMapper::toDto)
                .collect(ImmutableList.toImmutableList());

        return ResponseEntity.ok(resultList);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'BUSINESS')")
    @RequestMapping(
            method = RequestMethod.GET,
            path = "business/status",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<OptionalServiceForChosenLocationDto>> listAllByConfirmationStatusAndBusinessId(@RequestParam String status,
                                                                                                                       @RequestParam long businessId) {

        final List<OptionalServiceForChosenLocation> optionalServiceForChosenLocationList =
                optionalServiceForLocationService.listAllByStatusAndBusinessId(businessId, status);
        final ImmutableList<OptionalServiceForChosenLocationDto> resultList = optionalServiceForChosenLocationList.stream()
                .map(OptionalServiceForLocationMapper::toDtoWithLocationAndEvent)
                .collect(ImmutableList.toImmutableList());
        return ResponseEntity.ok(resultList);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'CUSTOMER')")
    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrganizedEventConfirmationDto> create(@RequestParam long customerId,
                                                                @RequestParam long eventId,
                                                                @RequestParam long serviceId,
                                                                @RequestBody @Valid OptionalServiceForChosenLocationDto dto) {
        final OptionalServiceForChosenLocation optionalServiceForChosenLocation =
                optionalServiceForLocationService.create(customerId, eventId, serviceId, dto);
        final OrganizedEvent event = optionalServiceForChosenLocation.getLocationForEvent().getEvent();

        return ResponseEntity.ok(OrganizedEventMapper.toDtoWithServices(event));
    }


    @PreAuthorize("hasAnyAuthority('ADMIN', 'BUSINESS', 'CUSTOMER')")
    @RequestMapping(
            path = "cancel",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OptionalServiceForChosenLocationDto> cancel(@RequestParam long id) {
        final OptionalServiceForChosenLocation serviceForChosenLocation = optionalServiceForLocationService.cancelReservation(id);

        return ResponseEntity.ok(OptionalServiceForLocationMapper.toDto(serviceForChosenLocation));
    }
}
