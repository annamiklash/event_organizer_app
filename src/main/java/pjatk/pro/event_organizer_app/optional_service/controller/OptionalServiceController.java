package pjatk.pro.event_organizer_app.optional_service.controller;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pjatk.pro.event_organizer_app.common.paginator.CustomPage;
import pjatk.pro.event_organizer_app.optional_service.enums.KidPerformerTypeEnum;
import pjatk.pro.event_organizer_app.optional_service.enums.LanguagesEnum;
import pjatk.pro.event_organizer_app.optional_service.enums.MusicStyleEnum;
import pjatk.pro.event_organizer_app.optional_service.enums.OptionalServiceTypeEnum;
import pjatk.pro.event_organizer_app.optional_service.mapper.OptionalServiceMapper;
import pjatk.pro.event_organizer_app.optional_service.model.OptionalService;
import pjatk.pro.event_organizer_app.optional_service.model.dto.FilterOptionalServiceDto;
import pjatk.pro.event_organizer_app.optional_service.model.dto.OptionalServiceDto;
import pjatk.pro.event_organizer_app.optional_service.service.OptionalServiceService;
import pjatk.pro.event_organizer_app.reviews.service.service.OptionalServiceReviewService;
import pjatk.pro.event_organizer_app.table.TableDto;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("api/services")
public class OptionalServiceController {

    private final OptionalServiceService optionalServiceService;
    private final OptionalServiceReviewService optionalServiceReviewService;

    @RequestMapping(
            method = RequestMethod.GET,
            path = "allowed/all",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TableDto<OptionalServiceDto>> list(@RequestParam(required = false) String keyword,
                                                             @RequestParam(defaultValue = "0") Integer pageNo,
                                                             @RequestParam(defaultValue = "50") Integer pageSize,
                                                             @RequestParam(defaultValue = "id") String sortBy,
                                                             @RequestParam(defaultValue = "asc") String order) {
        log.info("GET ALL SERVICES");
        final CustomPage customPage = CustomPage.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .sortBy(sortBy)
                .order(order)
                .build();
        final ImmutableList<OptionalService> optionalServiceList = optionalServiceService.list(customPage, keyword);
        final Long count = optionalServiceService.count(keyword);

        final ImmutableList<OptionalServiceDto> result = optionalServiceList.stream()
                .map(OptionalServiceMapper::toDto)
                .map(this::setRating)
                .collect(ImmutableList.toImmutableList());

        return ResponseEntity.ok(new TableDto<>(TableDto.MetaDto.builder().pageNo(pageNo).pageSize(pageSize).sortBy(sortBy).total(count).build(), result));
    }

    @RequestMapping(
            method = RequestMethod.GET,
            path = "allowed",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OptionalServiceDto> get(@RequestParam long id) {
        log.info("GET " + id);

        final OptionalService optionalService = optionalServiceService.get(id);
        final OptionalServiceDto serviceDto = OptionalServiceMapper.toDto(optionalService);
        final OptionalServiceDto serviceDtoWithRating = setRating(serviceDto);

        return ResponseEntity.ok(serviceDtoWithRating);
    }

    @RequestMapping(
            method = RequestMethod.GET,
            path = "allowed/cities",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<String>> getCities() {
        final List<String> result = optionalServiceService.getCities();

        return ResponseEntity.ok(result);
    }

    @RequestMapping(
            method = RequestMethod.GET,
            path = "allowed/{id}/detail",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OptionalServiceDto> getWithDetail(@PathVariable long id) {
        log.info("GET " + id);

        final OptionalService optionalService = optionalServiceService.getWithDetail(id);
        final OptionalServiceDto serviceDto = OptionalServiceMapper.toDtoWithDetails(optionalService);
        final OptionalServiceDto serviceDtoWithRating = setRating(serviceDto);

        return ResponseEntity.ok(serviceDtoWithRating);
    }

    @RequestMapping(
            path = "allowed/available",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> isAvailable(@RequestParam long serviceId,
                                               @RequestParam String date,
                                               @RequestParam String timeFrom,
                                               @RequestParam String timeTo) {
        final boolean isAvailable = optionalServiceService.isAvailable(serviceId, date, timeFrom, timeTo);
        return ResponseEntity.ok(isAvailable);
    }

    @RequestMapping(
            method = RequestMethod.GET,
            value = "allowed/types",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<String>> types() {
        final ImmutableList<String> result = Stream.of(OptionalServiceTypeEnum.values())
                .map(OptionalServiceTypeEnum::getValue)
                .collect(ImmutableList.toImmutableList());
        return ResponseEntity.ok(result);
    }

    @RequestMapping(
            method = RequestMethod.GET,
            value = "allowed/kid/performer/types",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<String>> kidPerformerTypes() {
        final ImmutableList<String> resultList = Stream.of(KidPerformerTypeEnum.values())
                .map(KidPerformerTypeEnum::getValue)
                .collect(ImmutableList.toImmutableList());
        return ResponseEntity.ok(resultList);
    }

    @RequestMapping(
            method = RequestMethod.GET,
            value = "allowed/music/styles",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<String>> musicStyles() {
        final ImmutableList<String> resultList = Stream.of(MusicStyleEnum.values())
                .map(MusicStyleEnum::getValue)
                .collect(ImmutableList.toImmutableList());
        return ResponseEntity.ok(resultList);
    }

    @RequestMapping(
            method = RequestMethod.GET,
            value = "allowed/languages",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<String>> languages() {
        final ImmutableList<String> resultList = Stream.of(LanguagesEnum.values())
                .map(LanguagesEnum::getValue)
                .collect(ImmutableList.toImmutableList());
        return ResponseEntity.ok(resultList);
    }

    @RequestMapping(
            method = RequestMethod.POST,
            path = "allowed/search",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TableDto<OptionalServiceDto>> searchByAppliedFilters(@RequestBody FilterOptionalServiceDto dto) {

        final ImmutableList<OptionalService> optionalServiceList = optionalServiceService.search(dto);
        final int count = optionalServiceList.size();
        final ImmutableList<OptionalServiceDto> resultList = optionalServiceList.stream()
                .map(OptionalServiceMapper::toDto)
                .map(this::setRating)
                .collect(ImmutableList.toImmutableList());

        return ResponseEntity.ok(new TableDto<>(new TableDto.MetaDto((long) count, null, null, null), resultList));
    }

    @PreAuthorize("hasAnyAuthority('BUSINESS')")
    @RequestMapping(
            method = RequestMethod.GET,
            path = "business",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<OptionalServiceDto>> getByBusinessId(@RequestParam long id) {
        final ImmutableList<OptionalService> locationList = optionalServiceService.getByBusinessId(id);
        final ImmutableList<OptionalServiceDto> resultList = locationList.stream()
                .map(OptionalServiceMapper::toDto)
                .collect(ImmutableList.toImmutableList());

        return ResponseEntity.ok(resultList);
    }

    @PreAuthorize("hasAuthority('BUSINESS')")
    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OptionalServiceDto> create(@Valid @RequestBody OptionalServiceDto dto) {

        final OptionalService optionalService = optionalServiceService.create(dto);

        return ResponseEntity.ok(OptionalServiceMapper.toDto(optionalService));
    }

    @PreAuthorize("hasAuthority('BUSINESS')")
    @RequestMapping(
            method = RequestMethod.PUT,
            path = "edit",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OptionalServiceDto> edit(@Valid @RequestBody OptionalServiceDto dto,
                                                   @RequestParam long id) {

        final OptionalService optionalService = optionalServiceService.edit(dto, id);

        final OptionalServiceDto serviceDto = OptionalServiceMapper.toDto(optionalService);
        final OptionalServiceDto serviceDtoWithRating = setRating(serviceDto);

        return ResponseEntity.ok(serviceDtoWithRating);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'BUSINESS')")
    @RequestMapping(
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@RequestParam long id) {
        optionalServiceService.delete(id);

        return ResponseEntity.noContent().build();
    }

    private OptionalServiceDto setRating(OptionalServiceDto optionalServiceDto) {
        final long optionalServiceId = optionalServiceDto.getId();
        final double rating = optionalServiceReviewService.getRating(optionalServiceId);
        optionalServiceDto.setRating(rating);
        return optionalServiceDto;
    }

}
