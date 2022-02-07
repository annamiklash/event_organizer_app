package pjatk.pro.event_organizer_app.location.controller;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pjatk.pro.event_organizer_app.location.mapper.LocationDescriptionItemMapper;
import pjatk.pro.event_organizer_app.location.model.LocationDescriptionItem;
import pjatk.pro.event_organizer_app.location.model.dto.LocationDescriptionItemDto;
import pjatk.pro.event_organizer_app.location.service.LocationDescriptionItemService;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("api/location_description")
public class LocationDescriptionItemController {

    private final LocationDescriptionItemService service;

    @RequestMapping(
            path = "allowed/all",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<LocationDescriptionItemDto>> list() {
        log.info("GET LOCATION DESCRIPTIONS");
        final List<LocationDescriptionItem> list = service.listAllDistinct();

        return ResponseEntity.ok(ImmutableList.copyOf(
                list.stream()
                        .map(LocationDescriptionItemMapper::toDto)
                        .collect(Collectors.toList())));
    }
}
