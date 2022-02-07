package pjatk.pro.event_organizer_app.customer.controller;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pjatk.pro.event_organizer_app.catering.model.Catering;
import pjatk.pro.event_organizer_app.common.paginator.CustomPage;
import pjatk.pro.event_organizer_app.customer.mapper.CustomerMapper;
import pjatk.pro.event_organizer_app.customer.message.dto.MessageDto;
import pjatk.pro.event_organizer_app.customer.model.Customer;
import pjatk.pro.event_organizer_app.customer.model.dto.CustomerDto;
import pjatk.pro.event_organizer_app.customer.service.CustomerService;
import pjatk.pro.event_organizer_app.exceptions.NotFoundException;
import pjatk.pro.event_organizer_app.location.model.Location;
import pjatk.pro.event_organizer_app.optional_service.model.OptionalService;

import javax.validation.Valid;

@Slf4j
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("api/customers")
public class CustomerController {

    private final CustomerService customerService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<CustomerDto>> findAll(@RequestParam(required = false) String keyword,
                                                              @RequestParam(defaultValue = "0") Integer pageNo,
                                                              @RequestParam(defaultValue = "50") Integer pageSize,
                                                              @RequestParam(defaultValue = "id") String sortBy,
                                                              @RequestParam(defaultValue = "asc") String order) {
        log.info("GET ALL CUSTOMERS");
        final CustomPage customPage = CustomPage.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .sortBy(sortBy)
                .order(order)
                .build();
        // TODO: add count like in pjatk.pro.event_organizer_app.location.controller.LocationController.list
        final ImmutableList<Customer> customerList = customerService.list(customPage, keyword);
        final ImmutableList<CustomerDto> resultList = customerList.stream()
                .map(CustomerMapper::toDto)
                .collect(ImmutableList.toImmutableList());

        return ResponseEntity.ok(resultList);
    }


    @PreAuthorize("hasAnyAuthority('ADMIN', 'CUSTOMER')")
    @RequestMapping(
            method = RequestMethod.GET,
            params = {"id"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomerDto> get(@RequestParam long id) {
        log.info("GET CUSTOMER");
        try {
            final Customer customer = customerService.get(id);

            return ResponseEntity.ok(CustomerMapper.toDto(customer));
        } catch (NotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'CUSTOMER')")
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/{id}/detail",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomerDto> getWithDetail(@PathVariable long id) {

        try {
            final Customer customer = customerService.getWithDetail(id);
            return ResponseEntity.ok(CustomerMapper.toDtoWithDetail(customer));
        } catch (NotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'CUSTOMER')")
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/{id}/guests",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomerDto> getWithGuests(@PathVariable long id) {
        log.info("GET CUSTOMER GUESTS");
        try {
            return ResponseEntity.ok(customerService.getWithGuests(id));
        } catch (NotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'CUSTOMER')")
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/{id}/problems",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomerDto> getWithProblems(@PathVariable long id) {
        log.info("GET CUSTOMER REPORTED PROBLEMS");
        try {
            return ResponseEntity.ok(customerService.getWithProblems(id));
        } catch (NotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'CUSTOMER')")
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/{id}/events/all",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomerDto> getWithAllEvents(@PathVariable long id) {
        log.info("GET CUSTOMER ALL EVENTS");
        try {
            final Customer customerWithAllEvents = customerService.getWithAllEvents(id);
            final CustomerDto result = CustomerMapper.toDtoWithDetail(customerWithAllEvents);

            return ResponseEntity.ok(result);
        } catch (NotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'CUSTOMER')")
    @RequestMapping(
            method = RequestMethod.PUT,
            params = {"id"},
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomerDto> edit(@RequestBody @Valid CustomerDto dto,
                                            @RequestParam long id) {
        final Customer customer = customerService.edit(dto, id);

        return ResponseEntity.ok(CustomerMapper.toDto(customer));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'CUSTOMER')")
    @RequestMapping(
            method = RequestMethod.DELETE,
            params = {"id"})
    public ResponseEntity<Void> delete(@RequestParam long id) {
        log.info("DELETE CUSTOMER");
        customerService.delete(id);

        return ResponseEntity.ok().build();
    }


    @PreAuthorize("hasAuthority('CUSTOMER')")
    @RequestMapping(
            method = RequestMethod.POST,
            path = "invite/send",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> sendScheduleToAllGuests(@RequestParam long id,
                                                        @RequestParam long eventId) {
        log.info("SEND INVITE TO GUEST");
        customerService.sendInvitationToGuest(eventId, id);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAuthority('CUSTOMER')")
    @RequestMapping(
            method = RequestMethod.POST,
            path = "message/location/send",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> sendMessageLocation(@RequestParam long customerId,
                                                    @RequestParam long locationId,
                                                    @Valid @RequestBody MessageDto dto) {
        log.info("SEND MESSAGE");
        customerService.sendMessage(customerId, locationId, dto, Location.class);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAuthority('CUSTOMER')")
    @RequestMapping(
            method = RequestMethod.POST,
            path = "message/catering/send",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> sendMessageCatering(@RequestParam long customerId,
                                                    @RequestParam long cateringId,
                                                    @Valid @RequestBody MessageDto dto) {
        log.info("SEND MESSAGE");
        customerService.sendMessage(customerId, cateringId, dto, Catering.class);
        return ResponseEntity.ok().build();
    }


    @PreAuthorize("hasAuthority('CUSTOMER')")
    @RequestMapping(
            path = "avatar/upload",
            method = RequestMethod.POST,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> uploadAvatar(@RequestParam long customerId, @RequestParam("file") MultipartFile file) {
        customerService.uploadAvatar(customerId, file);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAuthority('CUSTOMER')")
    @RequestMapping(
            path = "avatar/delete",
            method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteAvatar(@RequestParam long customerId) {
        customerService.deleteAvatarById(customerId);
        return ResponseEntity.ok().build();

    }


    @PreAuthorize("hasAuthority('CUSTOMER')")
    @RequestMapping(
            method = RequestMethod.POST,
            path = "message/service/send",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> sendMessageService(@RequestParam long customerId,
                                                   @RequestParam long serviceId,
                                                   @Valid @RequestBody MessageDto dto) {
        log.info("SEND MESSAGE");
        customerService.sendMessage(customerId, serviceId, dto, OptionalService.class);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAuthority('CUSTOMER')")
    @RequestMapping(
            method = RequestMethod.PUT,
            path = "guests/invite")
    public ResponseEntity<Void> addGuestsToLocationEvent(@RequestParam long customerId,
                                                         @RequestParam long eventId,
                                                         @RequestParam long[] guestIds) {

        customerService.addGuestsToEvent(customerId, eventId, guestIds);
        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

}


