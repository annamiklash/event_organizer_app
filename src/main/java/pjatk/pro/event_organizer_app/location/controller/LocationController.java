package pjatk.pro.event_organizer_app.location.controller;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pjatk.pro.event_organizer_app.common.paginator.CustomPage;
import pjatk.pro.event_organizer_app.location.mapper.LocationMapper;
import pjatk.pro.event_organizer_app.location.model.Location;
import pjatk.pro.event_organizer_app.location.model.dto.FilterLocationsDto;
import pjatk.pro.event_organizer_app.location.model.dto.LocationDto;
import pjatk.pro.event_organizer_app.location.service.LocationService;
import pjatk.pro.event_organizer_app.table.TableDto;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("api/locations")
public class LocationController {

    private final LocationService locationService;

    @RequestMapping(
            method = RequestMethod.GET,
            path = "allowed/all",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TableDto<LocationDto>> list(@RequestParam(required = false) String keyword,
                                                      @RequestParam(defaultValue = "0") Integer pageNo,
                                                      @RequestParam(defaultValue = "50") Integer pageSize,
                                                      @RequestParam(defaultValue = "id") String sortBy,
                                                      @RequestParam(defaultValue = "asc") String order) {
        log.info("GET ALL LOCATIONS");
        final CustomPage customPage = CustomPage.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .sortBy(sortBy)
                .order(order)
                .build();

        final List<Location> locationList = locationService.list(customPage, keyword);
        final Long count = locationService.count(keyword);

        final ImmutableList<LocationDto> resultList = locationList.stream()
                .map(LocationMapper::toDto)
                .collect(ImmutableList.toImmutableList());

        return ResponseEntity.ok(new TableDto<>(new TableDto.MetaDto(count, pageNo, pageSize, sortBy), resultList));
    }

    @RequestMapping(
            method = RequestMethod.POST,
            path = "allowed/search",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TableDto<LocationDto>> searchByAppliedFilters(
            @Valid @RequestBody FilterLocationsDto dto) {

        final ImmutableList<Location> list = locationService.search(dto);
        final int count = list.size();
        final ImmutableList<LocationDto> result = list.stream()
                .map(LocationMapper::toDto)
                .collect(ImmutableList.toImmutableList());

        return ResponseEntity.ok(new TableDto<>(new TableDto.MetaDto((long) count, null, null, null), result));
    }


    @RequestMapping(
            method = RequestMethod.GET,
            path = "allowed",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LocationDto> get(@RequestParam long id) {
        log.info("GET " + id);

        final Location location = locationService.getWithImages(id);
        final LocationDto dto = LocationMapper.toDto(location);

        return ResponseEntity.ok(dto);
    }

    @RequestMapping(
            path = "allowed/available",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> isAvailable(@RequestParam long locationId,
                                               @RequestParam String date,
                                               @RequestParam String timeFrom,
                                               @RequestParam String timeTo) {
        final boolean isAvailable = locationService.isAvailable(locationId, date, timeFrom, timeTo);
        return ResponseEntity.ok(isAvailable);
    }


    @RequestMapping(
            method = RequestMethod.GET,
            path = "allowed/cities",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<String>> getCities() {
        final List<String> result = locationService.getCities();

        return ResponseEntity.ok(result);
    }

    @RequestMapping(
            method = RequestMethod.GET,
            path = "allowed/{id}/detail",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LocationDto> getWithDetail(@PathVariable long id) {
        log.info("GET " + id);

        final Location location = locationService.getWithDetail(id);
        final LocationDto dto = LocationMapper.toDtoWithDetail(location);

        return ResponseEntity.ok(dto);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'BUSINESS')")
    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LocationDto> create(@Valid @RequestBody LocationDto dto) {
        final Location location = locationService.create(dto);

        return ResponseEntity.ok(LocationMapper.toDto(location));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'BUSINESS')")
    @RequestMapping(
            method = RequestMethod.PUT,
            path = "edit",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LocationDto> edit(@Valid @RequestBody LocationDto dto, @RequestParam long id) {

        final Location location = locationService.edit(dto, id);
        final LocationDto locationDto = LocationMapper.toDtoWithDetailWithCaterings(location);

        return ResponseEntity.ok(locationDto);
    }


    @PreAuthorize("hasAnyAuthority('CUSTOMER', 'BUSINESS', 'ADMIN')")
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/{id}/availability",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LocationDto> getWithAvailability(@PathVariable long id, @RequestParam String date) {
        final Location location = locationService.getWithAvailability(id, date);
        final LocationDto locationDto = LocationMapper.toDto(location);

        return ResponseEntity.ok(locationDto);
    }

    @PreAuthorize("hasAnyAuthority('CUSTOMER', 'BUSINESS')")
    @RequestMapping(
            method = RequestMethod.GET,
            path = "business",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<LocationDto>> getByBusinessId(@RequestParam long id) {
        final ImmutableList<Location> locations = locationService.getByBusinessId(id);
        final ImmutableList<LocationDto> resultList = locations.stream()
                .map(LocationMapper::toDto)
                .collect(ImmutableList.toImmutableList());
        return ResponseEntity.ok(resultList);
    }

    @RequestMapping(
            method = RequestMethod.GET,
            path = "allowed/catering",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<LocationDto>> getByCateringId(@RequestParam long cateringId) {
        final ImmutableList<Location> locations = locationService.getByCateringId(cateringId);
        final ImmutableList<LocationDto> resultList = locations.stream()
                .map(LocationMapper::toDto)
                .collect(ImmutableList.toImmutableList());
        return ResponseEntity.ok(resultList);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'BUSINESS')")
    @RequestMapping(
            method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@RequestParam long id) {
        locationService.delete(id);

        return ResponseEntity.noContent().build();
    }

}

