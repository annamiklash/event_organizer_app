package pjatk.pro.event_organizer_app.catering.controller;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pjatk.pro.event_organizer_app.catering.mapper.CateringItemMapper;
import pjatk.pro.event_organizer_app.catering.model.CateringItem;
import pjatk.pro.event_organizer_app.catering.model.dto.CateringItemDto;
import pjatk.pro.event_organizer_app.catering.service.CateringItemService;
import pjatk.pro.event_organizer_app.enums.CateringItemTypeEnum;

import javax.validation.Valid;
import java.util.stream.Stream;

@Slf4j
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("api/catering/items")
public class CateringItemController {

    private final CateringItemService cateringItemService;

    @RequestMapping(
            path = "allowed",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<CateringItemDto>> listAllByCateringId(@RequestParam long cateringId) {
        log.info("GET ALL CATERING_ITEM_TYPES");

        final ImmutableList<CateringItem> cateringItemList = cateringItemService.listAllByCateringId(cateringId);

        final ImmutableList<CateringItemDto> resultList = cateringItemList.stream()
                .map(CateringItemMapper::toDto)
                .collect(ImmutableList.toImmutableList());

        return ResponseEntity.ok(resultList);
    }

    @RequestMapping(
            path = "allowed/types",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<String>> types() {
        final ImmutableList<String> resultList = Stream.of(CateringItemTypeEnum.values())
                .map(CateringItemTypeEnum::getValue)
                .collect(ImmutableList.toImmutableList());

        return ResponseEntity.ok(resultList);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'BUSINESS')")
    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CateringItemDto> get(@RequestParam long id) {

        final CateringItem item = cateringItemService.get(id);

        return ResponseEntity.ok(CateringItemMapper.toDto(item));
    }


    @PreAuthorize("hasAnyAuthority('ADMIN', 'BUSINESS')")
    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CateringItemDto> create(@Valid @RequestBody CateringItemDto dto, @RequestParam long cateringId) {

        final CateringItem cateringItem = cateringItemService.create(dto, cateringId);
        return ResponseEntity.ok(CateringItemMapper.toDto(cateringItem));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'BUSINESS')")
    @RequestMapping(
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CateringItemDto> edit(@Valid @RequestBody CateringItemDto dto, @RequestParam long id) {
        final CateringItem cateringItem = cateringItemService.edit(id, dto);
        return ResponseEntity.ok(CateringItemMapper.toDto(cateringItem));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'BUSINESS')")
    @RequestMapping(
            method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@RequestParam long id) {
        cateringItemService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
