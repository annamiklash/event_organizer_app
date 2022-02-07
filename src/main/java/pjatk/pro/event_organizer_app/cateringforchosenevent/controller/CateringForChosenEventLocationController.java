package pjatk.pro.event_organizer_app.cateringforchosenevent.controller;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pjatk.pro.event_organizer_app.cateringforchosenevent.mapper.CateringForChosenLocationMapper;
import pjatk.pro.event_organizer_app.cateringforchosenevent.model.CateringForChosenEventLocation;
import pjatk.pro.event_organizer_app.cateringforchosenevent.model.dto.CateringForChosenEventLocationDto;
import pjatk.pro.event_organizer_app.cateringforchosenevent.service.CateringForChosenEventLocationService;
import pjatk.pro.event_organizer_app.event.mapper.OrganizedEventMapper;
import pjatk.pro.event_organizer_app.event.model.OrganizedEvent;
import pjatk.pro.event_organizer_app.event.model.dto.OrganizedEventConfirmationDto;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Validated
@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("api/event/catering")
public class CateringForChosenEventLocationController {

    private final CateringForChosenEventLocationService cateringForChosenEventLocationService;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'BUSINESS')")
    @RequestMapping(
            method = RequestMethod.PUT,
            path = "confirm",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CateringForChosenEventLocationDto> confirmReservation(@RequestParam long cateringId,
                                                                                @RequestParam long eventId) {

        final CateringForChosenEventLocation catering = cateringForChosenEventLocationService.confirmReservation(cateringId, eventId);

        return ResponseEntity.ok(CateringForChosenLocationMapper.toDto(catering));
    }

    @PreAuthorize("hasAnyAuthority('BUSINESS')")
    @RequestMapping(
            method = RequestMethod.PUT,
            path = "order/confirm")
    public ResponseEntity<Void> confirmOrderReservation(@RequestParam long reservationId) {

        cateringForChosenEventLocationService.confirmOrder(reservationId);

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'BUSINESS')")
    @RequestMapping(
            method = RequestMethod.GET,
            path = "status",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<CateringForChosenEventLocationDto>> listAllByConfirmationStatus(@RequestParam String status,
                                                                                                        @RequestParam long cateringId) {
        final List<CateringForChosenEventLocation> cateringForChosenEventLocationList =
                cateringForChosenEventLocationService.listAllByStatus(cateringId, status);
        final ImmutableList<CateringForChosenEventLocationDto> resultList = cateringForChosenEventLocationList.stream()
                .map(CateringForChosenLocationMapper::toDto)
                .collect(ImmutableList.toImmutableList());

        return ResponseEntity.ok(resultList);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'BUSINESS')")
    @RequestMapping(
            method = RequestMethod.GET,
            path = "business/status",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<CateringForChosenEventLocationDto>> listAllByConfirmationStatusAndBusinessId(@RequestParam String status,
                                                                                                                     @RequestParam long businessId) {

        List<CateringForChosenEventLocation> optionalServices = cateringForChosenEventLocationService.listAllByStatusAndBusinessId(businessId, status);

        return ResponseEntity.ok(
                ImmutableList.copyOf(optionalServices.stream()
                        .map(CateringForChosenLocationMapper::toDtoWithEvent)
                        .collect(Collectors.toList())));
    }


    @PreAuthorize("hasAnyAuthority('ADMIN', 'CUSTOMER')")
    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrganizedEventConfirmationDto> create(@RequestParam long customerId,
                                                                @RequestParam long eventId,
                                                                @RequestParam long cateringId,
                                                                @RequestBody @Valid CateringForChosenEventLocationDto dto) {
        final CateringForChosenEventLocation catering =
                cateringForChosenEventLocationService.create(customerId, eventId, cateringId, dto);
        final OrganizedEvent event = catering.getEventLocation().getEvent();
        final OrganizedEventConfirmationDto result = OrganizedEventMapper.toDtoWithCatering(event);
        return ResponseEntity.ok(result);
    }


    @PreAuthorize("hasAnyAuthority('ADMIN', 'BUSINESS', 'CUSTOMER')")
    @RequestMapping(
            path = "cancel",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CateringForChosenEventLocationDto> cancel(@RequestParam long id) {
        final CateringForChosenEventLocation cateringForChosenEventLocation = cateringForChosenEventLocationService.cancelReservation(id);

        return ResponseEntity.ok(CateringForChosenLocationMapper.toDto(cateringForChosenEventLocation));
    }
}

