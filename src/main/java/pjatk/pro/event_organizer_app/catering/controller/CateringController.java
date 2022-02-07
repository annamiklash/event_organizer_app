package pjatk.pro.event_organizer_app.catering.controller;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pjatk.pro.event_organizer_app.catering.mapper.CateringMapper;
import pjatk.pro.event_organizer_app.catering.model.Catering;
import pjatk.pro.event_organizer_app.catering.model.dto.CateringDto;
import pjatk.pro.event_organizer_app.catering.model.dto.FilterCateringsDto;
import pjatk.pro.event_organizer_app.catering.service.CateringService;
import pjatk.pro.event_organizer_app.common.paginator.CustomPage;
import pjatk.pro.event_organizer_app.exceptions.IllegalArgumentException;
import pjatk.pro.event_organizer_app.reviews.catering.service.CateringReviewService;
import pjatk.pro.event_organizer_app.table.TableDto;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("api/caterings")
public class CateringController {

    private final CateringService cateringService;

    private final CateringReviewService cateringReviewService;

//    @Cacheable(value = "caterings")
    @RequestMapping(
            method = RequestMethod.GET,
            path = "allowed/all",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TableDto<CateringDto>> list(@RequestParam(required = false) String keyword,
                                                      @RequestParam(defaultValue = "0") Integer pageNo,
                                                      @RequestParam(defaultValue = "50") Integer pageSize,
                                                      @RequestParam(defaultValue = "id") String sortBy,
                                                      @RequestParam(defaultValue = "asc") String order) {

        log.info("GET ALL CATERING");
        final CustomPage customPage = CustomPage.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .sortBy(sortBy)
                .order(order)
                .build();
        final List<Catering> cateringList = cateringService.list(customPage, keyword);
        final Long count = cateringService.count(keyword);

        final ImmutableList<CateringDto> resultList = cateringList.stream()
                .map(CateringMapper::toDto)
                .peek(this::setRating)
                .collect(ImmutableList.toImmutableList());

        final TableDto<CateringDto> cateringTableDto =
                new TableDto<>(new TableDto.MetaDto(count, pageNo, pageSize, sortBy), resultList);

        return ResponseEntity.ok(cateringTableDto);
    }

    @RequestMapping(
            method = RequestMethod.POST,
            path = "allowed/search",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TableDto<CateringDto>> searchByAppliedFilters(@RequestBody FilterCateringsDto dto, @RequestParam(required = false) Long locationId) {
        final ImmutableList<Catering> list = cateringService.search(dto, locationId);
        final int count = list.size();
        final ImmutableList<CateringDto> resultList = list.stream()
                .map(CateringMapper::toDto)
                .peek(this::setRating)
                .collect(ImmutableList.toImmutableList());

        return ResponseEntity.ok(new TableDto<>(new TableDto.MetaDto((long) count, null, null, null), resultList));
    }

    @RequestMapping(
            method = RequestMethod.GET,
            path = "allowed",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CateringDto> get(@RequestParam long id) {
        log.info("GET " + id);
        final Catering catering = cateringService.get(id);
        final CateringDto cateringDto = CateringMapper.toDto(catering);
        setRating(cateringDto);

        return ResponseEntity.ok(cateringDto);
    }


    @RequestMapping(
            method = RequestMethod.GET,
            path = "allowed/{id}/detail",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CateringDto> getWithDetail(@PathVariable long id) {
        log.info("GET " + id);
        final Catering catering = cateringService.getWithDetail(id);

        final CateringDto cateringDto = CateringMapper.toDtoWithDetail(catering);
        setRating(cateringDto);

        return ResponseEntity.ok(cateringDto);
    }

    @PreAuthorize("hasAnyAuthority('CUSTOMER', 'BUSINESS')")
    @RequestMapping(
            method = RequestMethod.GET,
            path = "business",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<CateringDto>> getByBusinessId(@RequestParam long id) {
        final ImmutableList<Catering> caterings = cateringService.getByBusinessId(id);
        final ImmutableList<CateringDto> resultList = caterings.stream()
                .map(CateringMapper::toDto)
                .peek(this::setRating)
                .collect(ImmutableList.toImmutableList());

        return ResponseEntity.ok(resultList);
    }

    @RequestMapping(
            method = RequestMethod.GET,
            path = "allowed/location",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<CateringDto>> getByLocationId(@RequestParam long id) {
        final ImmutableList<Catering> caterings = cateringService.getByLocationId(id);
        final ImmutableList<CateringDto> resultList = caterings.stream()
                .map(CateringMapper::toDto)
                .peek(this::setRating)
                .collect(ImmutableList.toImmutableList());

        return ResponseEntity.ok(resultList);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'BUSINESS')")
    @RequestMapping(
            method = RequestMethod.DELETE,
            path = "delete")
    public ResponseEntity<Void> delete(@RequestParam long id) {
        try {
            cateringService.delete(id);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'BUSINESS')")
    @RequestMapping(
            method = RequestMethod.POST,
            path = "new/location",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CateringDto> create(@Valid @RequestBody CateringDto dto, @RequestParam long locationId) {
        final Catering catering = cateringService.create(dto, locationId);

        return ResponseEntity.ok(CateringMapper.toDto(catering));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'BUSINESS')")
    @RequestMapping(
            method = RequestMethod.POST,
            path = "new",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CateringDto> createStandalone(@Valid @RequestBody CateringDto dto) {
        final Catering catering = cateringService.create(dto, null);

        return ResponseEntity.ok(CateringMapper.toDto(catering));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'BUSINESS')")
    @RequestMapping(
            method = RequestMethod.PUT,
            path = "edit",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CateringDto> edit(@Valid @RequestBody CateringDto dto, @RequestParam long id) {
        try {
            final Catering catering = cateringService.edit(id, dto);

            final CateringDto cateringDto = CateringMapper.toDtoWithDetailAndLocations(catering);
            setRating(cateringDto);

            return ResponseEntity.ok(cateringDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    private void setRating(CateringDto cateringDto) {
        final double rating = cateringReviewService.getRating(cateringDto.getId());
        cateringDto.setRating(rating);
    }
}
