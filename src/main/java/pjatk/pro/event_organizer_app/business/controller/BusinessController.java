package pjatk.pro.event_organizer_app.business.controller;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pjatk.pro.event_organizer_app.business.mapper.BusinessMapper;
import pjatk.pro.event_organizer_app.business.model.Business;
import pjatk.pro.event_organizer_app.business.model.dto.BusinessDto;
import pjatk.pro.event_organizer_app.business.service.BusinessService;
import pjatk.pro.event_organizer_app.common.paginator.CustomPage;

import javax.validation.Valid;

@Slf4j
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("api/business")
public class BusinessController {

    private final BusinessService businessService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<BusinessDto>> list(@RequestParam(defaultValue = "0") Integer pageNo,
                                                           @RequestParam(defaultValue = "50") Integer pageSize,
                                                           @RequestParam(defaultValue = "id") String sort,
                                                           @RequestParam(defaultValue = "asc") String order) {
        final CustomPage customPage = CustomPage.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .sortBy(sort)
                .order(order)
                .build();
        final ImmutableList<Business> businessList = businessService.list(customPage);
        final ImmutableList<BusinessDto> resultList = businessList.stream()
                .map(BusinessMapper::toDto)
                .collect(ImmutableList.toImmutableList());

        return ResponseEntity.ok(resultList);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(
            method = RequestMethod.GET,
            params = {"id"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BusinessDto> get(@RequestParam long id) {
        log.info("GET " + id);
        final Business business = businessService.get(id);

        return ResponseEntity.ok(BusinessMapper.toDto(business));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'BUSINESS', 'CUSTOMER')")
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/{id}/detail",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BusinessDto> detail(@PathVariable long id) {
        log.info("GET " + id);
        final Business business = businessService.getWithDetail(id);
        return ResponseEntity.ok(BusinessMapper.toDtoWithDetail(business));
    }


    @PreAuthorize("hasAuthority('BUSINESS')")
    @RequestMapping(
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BusinessDto> edit(@RequestParam long id, @Valid @RequestBody BusinessDto dto) {

        final Business business = businessService.edit(id, dto);

        return ResponseEntity.ok(BusinessMapper.toDto(business));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','BUSINESS')")
    @RequestMapping(
            method = RequestMethod.DELETE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@RequestParam long id) {
        businessService.delete(id);

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(
            method = RequestMethod.PUT,
            value = "verify",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BusinessDto> verify(@RequestParam long id) {
        final Business business = businessService.verify(id);

        return ResponseEntity.ok(BusinessMapper.toDto(business));
    }

}
