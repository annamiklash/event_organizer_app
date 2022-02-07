package pjatk.pro.event_organizer_app.availability.optionalservice.controller;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pjatk.pro.event_organizer_app.availability.dto.AvailabilityDto;
import pjatk.pro.event_organizer_app.availability.mapper.AvailabilityMapper;
import pjatk.pro.event_organizer_app.availability.optionalservice.model.OptionalServiceAvailability;
import pjatk.pro.event_organizer_app.availability.optionalservice.service.OptionalServiceAvailabilityService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("api/availability/service")
public class OptionalServiceAvailabilityController {

    private final OptionalServiceAvailabilityService optionalServiceAvailabilityService;

    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<AvailabilityDto>> list(@RequestParam @NotNull long id,
                                                               @RequestParam @NotNull String date) {

        final List<OptionalServiceAvailability> availabilities =
                optionalServiceAvailabilityService.findAllByServiceIdAndDate(id, date);
        final ImmutableList<AvailabilityDto> resultList = availabilities.stream()
                .map(AvailabilityMapper::toDto)
                .collect(ImmutableList.toImmutableList());

        return ResponseEntity.ok(resultList);
    }

    @RequestMapping(
            path = "allowed/period",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<AvailabilityDto>> listForPeriod(@RequestParam long id,
                                                                     @RequestParam String dateFrom, @RequestParam String dateTo) {

        final List<OptionalServiceAvailability> availabilities = optionalServiceAvailabilityService.findAllByServiceIdAndDatePeriod(id, dateFrom, dateTo);
        final ImmutableList<AvailabilityDto> resultList = availabilities.stream()
                .map(AvailabilityMapper::toDto)
                .collect(ImmutableList.toImmutableList());

        return ResponseEntity.ok(resultList);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'BUSINESS')")
    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<AvailabilityDto>> create(@Valid @RequestBody AvailabilityDto[] dtos,
                                                                 @RequestParam @NotNull long id) {
        final ImmutableList<AvailabilityDto> availabilityDtoList = ImmutableList.copyOf(dtos);
        final List<OptionalServiceAvailability> availabilities =
                optionalServiceAvailabilityService.update(availabilityDtoList, id, true);

        final ImmutableList<AvailabilityDto> resultList = availabilities.stream()
                .map(AvailabilityMapper::toDto)
                .collect(ImmutableList.toImmutableList());

        return ResponseEntity.ok(resultList);
    }


    @PreAuthorize("hasAnyAuthority('ADMIN', 'BUSINESS')")
    @RequestMapping(
            method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@RequestParam @NotNull long id) {
        optionalServiceAvailabilityService.deleteById(id);

        return ResponseEntity.ok().build();
    }

}
