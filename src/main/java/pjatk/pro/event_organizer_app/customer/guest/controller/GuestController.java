package pjatk.pro.event_organizer_app.customer.guest.controller;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pjatk.pro.event_organizer_app.common.paginator.CustomPage;
import pjatk.pro.event_organizer_app.customer.guest.mapper.GuestMapper;
import pjatk.pro.event_organizer_app.customer.guest.model.Guest;
import pjatk.pro.event_organizer_app.customer.guest.model.dto.GuestDto;
import pjatk.pro.event_organizer_app.customer.guest.service.GuestService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("api/guests")
public class GuestController {

    private GuestService guestService;

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @RequestMapping(
            method = RequestMethod.GET,
            path = "all",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<GuestDto>> listGuests(@RequestParam(required = false) String keyword,
                                                              @RequestParam(defaultValue = "0") Integer pageNo,
                                                              @RequestParam(defaultValue = "50") Integer pageSize,
                                                              @RequestParam(defaultValue = "id") String sortBy,
                                                              @RequestParam(defaultValue = "asc") String order) {
        log.info("GET GUESTS");
        final CustomPage customPage = CustomPage.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .sortBy(sortBy)
                .order(order)
                .build();

        final ImmutableList<Guest> guestList = guestService.list(customPage, keyword);
        final ImmutableList<GuestDto> resultList = guestList.stream()
                .map(GuestMapper::toDto)
                .collect(ImmutableList.toImmutableList());

        return ResponseEntity.ok(resultList);
    }

    @PreAuthorize("hasAnyAuthority('CUSTOMER', 'ADMIN')")
    @RequestMapping(
            method = RequestMethod.GET,
            path = "customer",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<GuestDto>> listGuestsByCustomerId(@RequestParam long customerId) {

        final List<Guest> guests = guestService.listAllByCustomerId(customerId);
        final ImmutableList<GuestDto> resultList = guests.stream()
                .map(GuestMapper::toDto)
                .collect(ImmutableList.toImmutableList());

        return ResponseEntity.ok(resultList);
    }

    @PreAuthorize("hasAuthority('CUSTOMER')")
    @RequestMapping(
            method = RequestMethod.GET,
            params = {"id"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GuestDto> get(@RequestParam long id) {

        final Guest guest = guestService.get(id);
        return ResponseEntity.ok(GuestMapper.toDto(guest));
    }

    @PreAuthorize("hasAuthority('CUSTOMER')")
    @RequestMapping(
            method = RequestMethod.POST,
            path = "new",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GuestDto> create(@RequestParam long customerId, @Valid @RequestBody GuestDto dto) {
        log.info("CREATE GUEST");
        final Guest guest = guestService.create(customerId, dto);
        return ResponseEntity.ok(GuestMapper.toDtoWithCustomer(guest));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'CUSTOMER')")
    @RequestMapping(
            method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@RequestParam long customerId, @RequestParam long id) {
        guestService.delete(customerId, id);
        return ResponseEntity.noContent().build();
    }
}
