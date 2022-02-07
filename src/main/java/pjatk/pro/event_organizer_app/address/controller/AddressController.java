package pjatk.pro.event_organizer_app.address.controller;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pjatk.pro.event_organizer_app.address.mapper.AddressMapper;
import pjatk.pro.event_organizer_app.address.model.Address;
import pjatk.pro.event_organizer_app.address.model.dto.AddressDto;
import pjatk.pro.event_organizer_app.address.service.AddressService;
import pjatk.pro.event_organizer_app.common.paginator.CustomPage;

@Slf4j
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("api/address")
public class AddressController {

    private final AddressService addressService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<AddressDto>> list(@RequestParam(defaultValue = "0") Integer pageNo,
                                                          @RequestParam(defaultValue = "50") Integer pageSize,
                                                          @RequestParam(defaultValue = "id") String sort,
                                                          @RequestParam(defaultValue = "desc") String order) {

        final CustomPage customPage = CustomPage.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .sortBy(sort)
                .order(order)
                .build();
        final ImmutableList<Address> addressList = addressService.list(customPage);
        final ImmutableList<AddressDto> resultList = addressList.stream()
                .map(AddressMapper::toDto)
                .collect(ImmutableList.toImmutableList());

        return ResponseEntity.ok(resultList);
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(
            method = RequestMethod.GET,
            params = {"id"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AddressDto> get(@RequestParam long id) {
        log.info("GET " + id);

        final Address address = addressService.get(id);

        return ResponseEntity.ok(AddressMapper.toDto(address));
    }

}
