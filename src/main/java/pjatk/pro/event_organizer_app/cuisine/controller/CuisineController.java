package pjatk.pro.event_organizer_app.cuisine.controller;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pjatk.pro.event_organizer_app.cuisine.mapper.CuisineMapper;
import pjatk.pro.event_organizer_app.cuisine.model.Cuisine;
import pjatk.pro.event_organizer_app.cuisine.model.dto.CuisineDto;
import pjatk.pro.event_organizer_app.cuisine.service.CuisineService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("api/cuisines")
public class CuisineController {

    private final CuisineService cuisineService;

    @RequestMapping(
            method = RequestMethod.GET,
            path = "allowed/all",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<CuisineDto>> list() {

        final List<Cuisine> cuisineList = cuisineService.list();
        final ImmutableList<CuisineDto> resultList = cuisineList.stream()
                .map(CuisineMapper::toDto)
                .collect(ImmutableList.toImmutableList());

        return ResponseEntity.ok(resultList);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CuisineDto> create(@Valid @RequestBody CuisineDto dto) {

        final Cuisine cuisine = cuisineService.create(dto);

        return ResponseEntity.ok(CuisineMapper.toDto(cuisine));
    }
}
