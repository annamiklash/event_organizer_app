package pjatk.pro.event_organizer_app.customer.service;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.encoder.org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import pjatk.pro.event_organizer_app.catering.model.Catering;
import pjatk.pro.event_organizer_app.catering.service.CateringService;
import pjatk.pro.event_organizer_app.cateringforchosenevent.model.CateringForChosenEventLocation;
import pjatk.pro.event_organizer_app.common.convertors.Converter;
import pjatk.pro.event_organizer_app.common.helper.TimestampHelper;
import pjatk.pro.event_organizer_app.common.mapper.PageableMapper;
import pjatk.pro.event_organizer_app.common.paginator.CustomPage;
import pjatk.pro.event_organizer_app.common.util.CollectionUtil;
import pjatk.pro.event_organizer_app.common.util.ComposeInviteEmailUtil;
import pjatk.pro.event_organizer_app.common.util.DateTimeUtil;
import pjatk.pro.event_organizer_app.common.util.EmailUtil;
import pjatk.pro.event_organizer_app.customer.avatar.model.CustomerAvatar;
import pjatk.pro.event_organizer_app.customer.avatar.service.CustomerAvatarService;
import pjatk.pro.event_organizer_app.customer.avatar.validator.ImageValidator;
import pjatk.pro.event_organizer_app.customer.guest.model.Guest;
import pjatk.pro.event_organizer_app.customer.guest.model.dto.GuestDto;
import pjatk.pro.event_organizer_app.customer.guest.service.GuestService;
import pjatk.pro.event_organizer_app.customer.mapper.CustomerMapper;
import pjatk.pro.event_organizer_app.customer.message.dto.MessageDto;
import pjatk.pro.event_organizer_app.customer.model.Customer;
import pjatk.pro.event_organizer_app.customer.model.dto.CustomerDto;
import pjatk.pro.event_organizer_app.customer.repository.CustomerRepository;
import pjatk.pro.event_organizer_app.event.mapper.OrganizedEventMapper;
import pjatk.pro.event_organizer_app.event.model.OrganizedEvent;
import pjatk.pro.event_organizer_app.event.model.dto.OrganizedEventDto;
import pjatk.pro.event_organizer_app.event.service.OrganizedEventService;
import pjatk.pro.event_organizer_app.exceptions.ActionNotAllowedException;
import pjatk.pro.event_organizer_app.exceptions.IllegalArgumentException;
import pjatk.pro.event_organizer_app.exceptions.NotFoundException;
import pjatk.pro.event_organizer_app.exceptions.UserExistsException;
import pjatk.pro.event_organizer_app.location.locationforevent.model.LocationForEvent;
import pjatk.pro.event_organizer_app.location.locationforevent.service.LocationForEventService;
import pjatk.pro.event_organizer_app.location.model.Location;
import pjatk.pro.event_organizer_app.location.service.LocationService;
import pjatk.pro.event_organizer_app.optional_service.model.OptionalService;
import pjatk.pro.event_organizer_app.optional_service.optional_service_for_location.model.OptionalServiceForChosenLocation;
import pjatk.pro.event_organizer_app.optional_service.service.OptionalServiceService;
import pjatk.pro.event_organizer_app.security.password.PasswordEncoderSecurity;
import pjatk.pro.event_organizer_app.user.model.User;
import pjatk.pro.event_organizer_app.user.model.dto.CustomerUserRegistrationDto;
import pjatk.pro.event_organizer_app.user.service.EmailService;
import pjatk.pro.event_organizer_app.user.service.UserService;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static pjatk.pro.event_organizer_app.enums.ConfirmationStatusEnum.CONFIRMED;
import static pjatk.pro.event_organizer_app.enums.ConfirmationStatusEnum.NOT_CONFIRMED;
import static pjatk.pro.event_organizer_app.enums.EventStatusEnum.*;
import static pjatk.pro.event_organizer_app.exceptions.UserExistsException.ENUM.USER_EXISTS;

@Service
@AllArgsConstructor
@Slf4j
public class CustomerService {

    private final CustomerRepository customerRepository;

    private final UserService userService;
    private final EmailService emailService;
    private final GuestService guestService;
    private final OrganizedEventService organizedEventService;
    private final LocationForEventService locationForEventService;
    private final CustomerAvatarService customerAvatarService;
    private final LocationService locationService;
    private final CateringService cateringService;
    private final OptionalServiceService optionalServiceService;
    private final TimestampHelper timestampHelper;
    private final PasswordEncoderSecurity passwordEncoderSecurity;

    public ImmutableList<Customer> list(CustomPage customPage, String keyword) {
        keyword = Strings.isNullOrEmpty(keyword) ? "" : keyword.toLowerCase();

        final Pageable paging = PageableMapper.map(customPage);
        final Page<Customer> page = customerRepository.findAllWithKeyword(paging, keyword);

        return ImmutableList.copyOf(page.get().collect(Collectors.toList()));
    }

    @Transactional(rollbackOn = Exception.class)
    public Customer createCustomerAccount(CustomerUserRegistrationDto dto) {
        if (userService.userExists(dto.getEmail())) {
            throw new UserExistsException(USER_EXISTS);
        }
        final Customer customer = CustomerMapper.fromCustomerRegistrationDto(dto);

        final String hashedPassword = passwordEncoderSecurity.bcryptEncryptor(dto.getPassword());
        customer.setPassword(hashedPassword);

        customer.setCreatedAt(timestampHelper.now());
        customer.setModifiedAt(timestampHelper.now());

        log.info("TRYING TO SAVE CUSTOMER");
        customerRepository.save(customer);

        return customer;
    }

    public <T> void sendMessage(long customerId, long receiverId, MessageDto messageDto, Class<T> clazz) {
        final User user = userService.get(customerId);
        final Customer customer = get(customerId);

        String className = clazz.getName();
        className = className.substring(className.lastIndexOf(".") + 1);

        switch (className) {
            case "Location":
                final Location location = locationService.getWithDetail(receiverId);
                messageDto.setReceiverEmail(location.getBusiness().getEmail());
                break;

            case "Catering":
                final Catering catering = cateringService.getWithDetail(receiverId);
                messageDto.setReceiverEmail(catering.getBusiness().getEmail());
                break;

            case "OptionalService":
                final OptionalService optionalService = optionalServiceService.getWithDetail(receiverId);
                messageDto.setReceiverEmail(optionalService.getBusiness().getEmail());
                break;

            default:
                throw new IllegalArgumentException("Incorrect receiver type");
        }
        messageDto.setReplyToEmail(customer.getEmail());
        final String content = "Message send from user " +
                customer.getFirstName() + " " +
                customer.getLastName() + " " +
                "with email" + " " + user.getEmail() +
                "\n\n" + messageDto.getContent() + "\n\nSent via Event Organizer app";

        final SimpleMailMessage inviteEmail = EmailUtil.buildEmail(content,
                messageDto.getReceiverEmail(), messageDto.getSubject(), messageDto.getReplyToEmail());

        emailService.sendEmail(inviteEmail);

    }

    public Customer getWithDetail(long id) {
        return customerRepository.getWithDetail(id)
                .orElseThrow(() -> new NotFoundException("Customer with id " + id + " DOES NOT EXIST"));
    }

    public CustomerDto getWithGuests(long id) {
        return customerRepository.getByIdWithAllGuests(id)
                .map(CustomerMapper::toDtoWithGuests)
                .orElseThrow(() -> new NotFoundException("Customer with id " + id + " DOES NOT EXIST"));
    }

    public CustomerDto getWithProblems(long id) {
        return customerRepository.getByIdWithProblems(id)
                .map(CustomerMapper::toDtoWithProblems)
                .orElseThrow(() -> new NotFoundException("Customer with id " + id + " DOES NOT EXIST"));
    }

    public Customer getWithAllEvents(long id) {
        return customerRepository.getWithDetail(id)
                .orElseThrow(() -> new NotFoundException("Customer with id " + id + " DOES NOT EXIST"));
    }

    public Customer get(long id) {
        return customerRepository.getByIdWithUser(id)
                .orElseThrow(() -> new NotFoundException("Customer with id " + id + " DOES NOT EXIST"));
    }

    private boolean customerExists(long id) {
        return customerRepository.findById(id).isPresent();
    }

    @Transactional(rollbackOn = Exception.class)
    public void delete(long id) {
        final Customer customerToDelete = customerRepository.getAllCustomerInformation(id)
                .orElseThrow(() -> new NotFoundException("Location with id " + id + " DOES NOT EXIST"));

        boolean hasPendingReservations = hasPendingReservations(customerToDelete);
        if (hasPendingReservations) {
            throw new ActionNotAllowedException("Cannot delete customer with reservations pending");
        }

        CollectionUtil.emptyListIfNull(customerToDelete.getGuests())
                .forEach(guestService::delete);

//        CollectionUtil.emptyListIfNull(customerToDelete.getEvents())
//                .forEach(organizedEventService::delete);

        deleteAvatarById(customerToDelete.getAvatar().getId());

        customerRepository.delete(customerToDelete);
    }


    public Customer edit(CustomerDto dto, long id) {
        final Customer customer = get(id);

        customer.setBirthdate(DateTimeUtil.fromStringToFormattedDate(dto.getBirthdate()));
        customer.setLastName(dto.getLastName());
        customer.setFirstName(dto.getFirstName());
        customer.setPhoneNumber(Converter.convertPhoneNumberString(dto.getPhoneNumber()));
        customer.setModifiedAt(timestampHelper.now());

        customerRepository.save(customer);

        return customer;
    }

    public void addGuestsToEvent(long id, long eventId, long[] guestIds) {
        if (!customerExists(id)) {
            throw new NotFoundException("No customer with id " + id);
        }
        final LocationForEvent locationForEvent = locationForEventService.findByEventId(eventId);

        if (!CONFIRMED.name().equals(locationForEvent.getConfirmationStatus())) {
            throw new IllegalArgumentException("Cannot invite guests while reservation for location is not confirmed");
        }

        final List<Guest> guests = guestService.getGuestsByIds(Arrays.asList(ArrayUtils.toObject(guestIds)));
        final OrganizedEvent organizedEvent = organizedEventService.get(eventId);

        organizedEvent.setGuests(new HashSet<>(guests));
        organizedEvent.setModifiedAt(timestampHelper.now());

        organizedEventService.save(organizedEvent);
    }

    public void deleteAvatarById(long id) {
        customerAvatarService.deleteById(id);

    }

    @SneakyThrows(IOException.class)
    public void uploadAvatar(long customerId, MultipartFile file) {
        if (file.getOriginalFilename() == null) {
            throw new ActionNotAllowedException("Cannot upload from empty path");
        }
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        ImageValidator.validateFileExtension(fileName);

        final Customer customer = get(customerId);

        final CustomerAvatar customerAvatar = customer.getAvatar();
        if (customerAvatar != null) {

            customerAvatar.setImage(file.getBytes());
            customerAvatarService.save(customerAvatar);

            customer.setAvatar(customerAvatar);
            customerRepository.save(customer);

        } else {
            final CustomerAvatar avatar = CustomerAvatar.builder()
                    .fileName(fileName)
                    .image(file.getBytes())
                    .build();
            customerAvatarService.save(avatar);

            customer.setAvatar(avatar);
            customerRepository.save(customer);
        }
    }

    @Transactional(rollbackOn = Exception.class)
    public void sendInvitationToGuest(long eventId, long customerId) {
        final OrganizedEvent organizedEvent = organizedEventService.getWithAllInformationForSendingInvitations(eventId, customerId);

        final Set<LocationForEvent> locationForEventSet = organizedEvent.getLocationForEvent();

        if (CollectionUtils.isEmpty(locationForEventSet)) {
            throw new ActionNotAllowedException("Cannot send invitations if no location booked");
        }

        final LocationForEvent locationForEvent = locationForEventSet.stream()
                .filter(location -> !CANCELLED.name().equals(location.getConfirmationStatus()))
                .findFirst()
                .orElseThrow(() -> new ActionNotAllowedException("No actual location reservation"));

        if (NOT_CONFIRMED.name().equals(locationForEvent.getConfirmationStatus())) {
            throw new ActionNotAllowedException("Cannot send invitations while location reservation not confirmed");
        }

        final Set<CateringForChosenEventLocation> cateringsForEventLocation = locationForEvent.getCateringsForEventLocation();

        if (!CollectionUtils.isEmpty(cateringsForEventLocation) && !cateringReservationsConfirmed(cateringsForEventLocation)) {
            throw new ActionNotAllowedException("Cannot send invitations while catering reservation not confirmed");
        }

        final Set<OptionalServiceForChosenLocation> services = locationForEvent.getServices();

        if (!CollectionUtils.isEmpty(services) && !servicesReservationsConfirmed(services)) {
            throw new ActionNotAllowedException("Cannot send invitations while services reservation not confirmed");
        }

        final OrganizedEventDto invitationContent = OrganizedEventMapper.toDtoForInvite(organizedEvent);

        final List<GuestDto> guests = invitationContent.getGuests();

        if (CollectionUtils.isEmpty(guests)) {
            throw new IllegalArgumentException("There are no guests invited to the event");
        }

        for (GuestDto guest : guests) {
            final String emailContent = ComposeInviteEmailUtil.composeEmail(guest, invitationContent);
            final String emailSubject = "Invitation From " + invitationContent.getCustomer().getFirstName() +
                    " " + invitationContent.getCustomer().getLastName();
            final SimpleMailMessage inviteEmail = EmailUtil.buildEmail(emailContent, guest.getEmail(), emailSubject, null);

            emailService.sendEmail(inviteEmail);
        }

        organizedEvent.setEventStatus(READY.name());
        organizedEventService.save(organizedEvent);
    }

    private boolean servicesReservationsConfirmed(Set<OptionalServiceForChosenLocation> services) {
        return services.stream()
                .allMatch(catering -> CONFIRMED.name().equals(catering.getConfirmationStatus()));
    }

    private boolean cateringReservationsConfirmed(Set<CateringForChosenEventLocation> cateringsForEventLocation) {
        return cateringsForEventLocation.stream()
                .allMatch(catering -> catering.isCateringOrderConfirmed() && CONFIRMED.name().equals(catering.getConfirmationStatus()));
    }

    private boolean hasPendingReservations(Customer customerToDelete) {
        return customerToDelete.getEvents().stream()
                .anyMatch(organizedEvent ->
                        IN_PROGRESS.name().equals(organizedEvent.getEventStatus())
                                || READY.name().equals(organizedEvent.getEventStatus()));

    }
}
