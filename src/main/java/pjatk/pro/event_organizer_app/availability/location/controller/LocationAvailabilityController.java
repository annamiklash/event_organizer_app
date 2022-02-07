package pjatk.pro.event_organizer_app.availability.location.controller;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pjatk.pro.event_organizer_app.availability.dto.AvailabilityDto;
import pjatk.pro.event_organizer_app.availability.location.model.LocationAvailability;
import pjatk.pro.event_organizer_app.availability.location.service.LocationAvailabilityService;
import pjatk.pro.event_organizer_app.availability.mapper.AvailabilityMapper;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("api/availability/location")
public class LocationAvailabilityController {

    private final LocationAvailabilityService locationAvailabilityService;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'BUSINESS')")
    @RequestMapping(
            path = "allowed",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<AvailabilityDto>> listForDate(@RequestParam long id,
                                                                      @RequestParam String date) {

        final List<LocationAvailability> availabilities = locationAvailabilityService.findAllByLocationIdAndDate(id, date);
        final ImmutableList<AvailabilityDto> resultList = availabilities.stream()
                .map(AvailabilityMapper::toDto)
                .collect(ImmutableList.toImmutableList());

        return ResponseEntity.ok(resultList);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'BUSINESS')")
    @RequestMapping(
            path = "allowed/period",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<AvailabilityDto>> listForPeriod(@RequestParam long id,
                                                                        @RequestParam String dateFrom, @RequestParam String dateTo) {

        final List<LocationAvailability> availabilities = locationAvailabilityService.findAllByLocationIdAndDatePeriod(id, dateFrom, dateTo);
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
    public ResponseEntity<ImmutableList<AvailabilityDto>> update(@Valid @RequestBody AvailabilityDto[] dtos,
                                                                 @RequestParam @NotNull long id) {
        final List<AvailabilityDto> availabilityDtos = ImmutableList.copyOf(dtos);
        final List<LocationAvailability> availabilities = locationAvailabilityService.update(availabilityDtos, id, true);
        final ImmutableList<AvailabilityDto> resultList = availabilities.stream()
                .map(AvailabilityMapper::toDto)
                .collect(ImmutableList.toImmutableList());

        return ResponseEntity.ok(resultList);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'BUSINESS')")
    @RequestMapping(
            method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@RequestParam @NotNull long id) {
        locationAvailabilityService.deleteById(id);

        return ResponseEntity.ok().build();
    }

}
