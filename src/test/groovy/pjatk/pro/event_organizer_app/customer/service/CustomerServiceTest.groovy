package pjatk.pro.event_organizer_app.customer.service

import com.google.common.collect.ImmutableList
import org.springframework.data.domain.PageImpl
import org.springframework.mock.web.MockMultipartFile
import pjatk.pro.event_organizer_app.catering.model.Catering
import pjatk.pro.event_organizer_app.catering.service.CateringService
import pjatk.pro.event_organizer_app.common.convertors.Converter
import pjatk.pro.event_organizer_app.common.helper.TimestampHelper
import pjatk.pro.event_organizer_app.common.util.ComposeInviteEmailUtil
import pjatk.pro.event_organizer_app.common.util.DateTimeUtil
import pjatk.pro.event_organizer_app.common.util.EmailUtil
import pjatk.pro.event_organizer_app.customer.avatar.service.CustomerAvatarService
import pjatk.pro.event_organizer_app.customer.guest.service.GuestService
import pjatk.pro.event_organizer_app.customer.mapper.CustomerMapper
import pjatk.pro.event_organizer_app.customer.message.dto.MessageDto
import pjatk.pro.event_organizer_app.customer.repository.CustomerRepository
import pjatk.pro.event_organizer_app.event.mapper.OrganizedEventMapper
import pjatk.pro.event_organizer_app.event.model.EventType
import pjatk.pro.event_organizer_app.event.service.OrganizedEventService
import pjatk.pro.event_organizer_app.location.locationforevent.service.LocationForEventService
import pjatk.pro.event_organizer_app.location.model.Location
import pjatk.pro.event_organizer_app.location.service.LocationService
import pjatk.pro.event_organizer_app.optional_service.model.OptionalService
import pjatk.pro.event_organizer_app.optional_service.service.OptionalServiceService
import pjatk.pro.event_organizer_app.security.password.PasswordEncoderSecurity
import pjatk.pro.event_organizer_app.trait.address.AddressTrait
import pjatk.pro.event_organizer_app.trait.catering.CateringTrait
import pjatk.pro.event_organizer_app.trait.customer.CustomerTrait
import pjatk.pro.event_organizer_app.trait.customer.guest.GuestTrait
import pjatk.pro.event_organizer_app.trait.event.OrganizedEventTrait
import pjatk.pro.event_organizer_app.trait.location.LocationTrait
import pjatk.pro.event_organizer_app.trait.location.locationforevent.LocationForEventTrait
import pjatk.pro.event_organizer_app.trait.optional_service.OptionalServiceTrait
import pjatk.pro.event_organizer_app.trait.page.PageTrait
import pjatk.pro.event_organizer_app.trait.user.CustomerUserRegistrationDtoTrait
import pjatk.pro.event_organizer_app.trait.user.UserTrait
import pjatk.pro.event_organizer_app.user.service.EmailService
import pjatk.pro.event_organizer_app.user.service.UserService
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDateTime

import static pjatk.pro.event_organizer_app.enums.EventStatusEnum.READY

class CustomerServiceTest extends Specification
        implements PageTrait,
                CustomerTrait,
                UserTrait,
                LocationTrait,
                CateringTrait,
                OptionalServiceTrait,
                OrganizedEventTrait,
                GuestTrait,
                LocationForEventTrait,
                AddressTrait,
                CustomerUserRegistrationDtoTrait {

    @Subject
    CustomerService customerService

    CustomerRepository customerRepository
    UserService userService
    EmailService emailService
    GuestService guestService
    OrganizedEventService organizedEventService
    LocationForEventService locationForEventService
    CustomerAvatarService customerAvatarService
    LocationService locationService
    CateringService cateringService
    OptionalServiceService optionalServiceService
    TimestampHelper timestampHelper
    PasswordEncoderSecurity passwordEncoderSecurity

    LocalDateTime now

    def setup() {
        customerRepository = Mock()
        userService = Mock()
        emailService = Mock()
        guestService = Mock()
        organizedEventService = Mock()
        locationForEventService = Mock()
        customerAvatarService = Mock()
        locationService = Mock()
        cateringService = Mock()
        optionalServiceService = Mock()
        timestampHelper = Mock()
        passwordEncoderSecurity = Mock()

        now = LocalDateTime.parse('2007-12-03T10:15:30')
        timestampHelper.now() >> now

        customerService = new CustomerService(
                customerRepository,
                userService,
                emailService,
                guestService,
                organizedEventService,
                locationForEventService,
                customerAvatarService,
                locationService,
                cateringService,
                optionalServiceService,
                timestampHelper,
                passwordEncoderSecurity
        )
    }

    def "List"() {
        given:
        def keyword = 'keyword'
        def customPagination = fakePage

        def paging = fakePaging
        def page = new PageImpl<>([fakeCustomer])

        def target = ImmutableList.of(fakeCustomer)
        when:
        def result = customerService.list(customPagination, keyword)

        then:
        1 * customerRepository.findAllWithKeyword(paging, keyword) >> page

        result == target
    }


    def "Create"() {
        given:
        def dto = fakeCustomerUserRegistrationDto

        def customer = CustomerMapper.fromCustomerRegistrationDto(dto)
        def hashedPassword = 'password'

        customer.setPassword(hashedPassword)
        customer.setCreatedAt(now)
        customer.setModifiedAt(now)

        def target = customer
        when:
        def result = customerService.createCustomerAccount(dto)

        then:
        1 * userService.userExists(dto.getEmail()) >> false
        1 * passwordEncoderSecurity.bcryptEncryptor(dto.getPassword()) >> hashedPassword
        1 * customerRepository.save(customer)

        result == target
    }

    def "SendMessage(#Location)"() {
        given:
        def customerId = 1L
        def receiverId = 1l

        def user = fakeUser
        def customer = fakeCustomer
        customer.setEmail('test@email.com')

        def location = fakeFullLocation

        def messageDto = MessageDto.builder()
                .subject("SAMPLE SUBJECT")
                .content('SAMPLE CONTENT')
                .build()

        def locationMessageDto = messageDto
        locationMessageDto.setReceiverEmail(location.getBusiness().getEmail())
        locationMessageDto.setReplyToEmail(user.getEmail())

        def content = "Message send from user " +
                customer.getFirstName() + " " +
                customer.getLastName() + " " +
                "with email" + " " + user.getEmail() +
                "\n\n" + locationMessageDto.getContent() +  "\n\n" + "Sent via pro app"

        def inviteEmail = EmailUtil.buildEmail(content,
                locationMessageDto.getReceiverEmail(), locationMessageDto.getSubject(), locationMessageDto.getReplyToEmail())

        when:
        customerService.sendMessage(customerId, receiverId, messageDto, Location.class)

        then:
        1 * userService.get(customerId) >> user
        1 * locationService.getWithDetail(receiverId) >> location
        1 * customerRepository.getByIdWithUser(customerId) >> Optional.of(customer)
        1 * emailService.sendEmail(inviteEmail)
    }

    def "SendMessage(#Catering)"() {
        given:
        def customerId = 1L
        def receiverId = 1l

        def user = fakeUser
        def customer = fakeCustomer
        customer.setEmail('test@email.com')

        def catering = fakeCateringWithDetails

        def messageDto = MessageDto.builder()
                .subject("SAMPLE SUBJECT")
                .content('SAMPLE CONTENT')
                .build()

        def cateringMessageDto = messageDto
        cateringMessageDto.setReceiverEmail(catering.getBusiness().getEmail())
        cateringMessageDto.setReplyToEmail(user.getEmail())

        def content = "Message send from user " +
                customer.getFirstName() + " " +
                customer.getLastName() + " " +
                "with email" + " " + user.getEmail() +
                "\n\n" + cateringMessageDto.getContent() +  "\n\n" + "Sent via pro app"

        def inviteEmail = EmailUtil.buildEmail(content,
                cateringMessageDto.getReceiverEmail(), cateringMessageDto.getSubject(), cateringMessageDto.getReplyToEmail())

        when:
        customerService.sendMessage(customerId, receiverId, messageDto, Catering.class)

        then:
        1 * userService.get(customerId) >> user
        1 * cateringService.getWithDetail(receiverId) >> catering
        1 * customerRepository.getByIdWithUser(customerId) >> Optional.of(customer)
        1 * emailService.sendEmail(inviteEmail)
    }

    def "SendMessage(#Service)"() {
        given:
        def customerId = 1L
        def receiverId = 1l

        def user = fakeUser
        def customer = fakeCustomer
        customer.setEmail('test@email.com')

        def optionalService = fakeOptionalService

        def messageDto = MessageDto.builder()
                .subject("SAMPLE SUBJECT")
                .content('SAMPLE CONTENT')
                .build()

        def serviceMessageDto = messageDto
        serviceMessageDto.setReceiverEmail(optionalService.getBusiness().getEmail())
        serviceMessageDto.setReplyToEmail(user.getEmail())

        def content = "Message send from user " +
                customer.getFirstName() + " " +
                customer.getLastName() + " " +
                "with email" + " " + user.getEmail() +
                "\n\n" + serviceMessageDto.getContent() +  "\n\n" + "Sent via pro app"

        def inviteEmail = EmailUtil.buildEmail(content,
                serviceMessageDto.getReceiverEmail(), serviceMessageDto.getSubject(), serviceMessageDto.getReplyToEmail())

        when:
        customerService.sendMessage(customerId, receiverId, messageDto, OptionalService.class)

        then:
        1 * userService.get(customerId) >> user
        1 * optionalServiceService.getWithDetail(receiverId) >> optionalService
        1 * customerRepository.getByIdWithUser(customerId) >> Optional.of(customer)
        1 * emailService.sendEmail(inviteEmail)
    }

    def "GetWithDetail"() {
        given:
        def id = 1L
        def target = fakeCustomer

        when:
        def result = customerService.getWithDetail(id)

        then:
        1 * customerRepository.getWithDetail(id) >> Optional.of(target)

        result == target
    }

    def "GetWithGuests"() {
        given:
        def id = 1L
        def customer = fakeCustomer
        def target = CustomerMapper.toDtoWithGuests(customer)

        when:
        def result = customerService.getWithGuests(id)

        then:
        1 * customerRepository.getByIdWithAllGuests(id) >> Optional.of(customer)

        result == target
    }

    def "GetWithProblems"() {
        given:
        def id = 1L
        def customer = fakeCustomer
        def target = CustomerMapper.toDtoWithProblems(customer)

        when:
        def result = customerService.getWithProblems(id)

        then:
        1 * customerRepository.getByIdWithProblems(id) >> Optional.of(customer)

        result == target
    }

    def "GetWithAllEvents"() {
        given:
        def id = 1L
        def target = fakeCustomer

        when:
        def result = customerService.getWithAllEvents(id)

        then:
        1 * customerRepository.getWithDetail(id) >> Optional.of(target)

        result == target
    }

    def "Get"() {
        given:
        def id = 1L
        def target = fakeCustomer

        when:
        def result = customerService.get(id)

        then:
        1 * customerRepository.getByIdWithUser(id) >> Optional.of(target)

        result == target
    }


    def "Delete"() {
        given:
        def id = 1L

        def customer = fakeCustomer

        when:
        customerService.delete(id)

        then:
        1 * customerRepository.getAllCustomerInformation(id) >> Optional.of(customer)
        1 * customerAvatarService.deleteById(id)
        1 * customerRepository.delete(customer)
    }

    def "Edit"() {
        given:
        def dto = fakeCustomerDTO
        def id = 1L

        def target = fakeCustomer
        target.setBirthdate(DateTimeUtil.fromStringToFormattedDate(dto.getBirthdate()))
        target.setLastName(dto.getLastName())
        target.setFirstName(dto.getFirstName())
        target.setPhoneNumber(Converter.convertPhoneNumberString(dto.getPhoneNumber()))
        target.setModifiedAt(now)

        when:
        def result = customerService.edit(dto, id)

        then:
        1 * customerRepository.getByIdWithUser(id) >> Optional.of(target)
        1 * customerRepository.save(target)

        result == target
    }

    def "AddGuestsToEvent"() {
        given:
        def id = 1L
        def eventId = 2l
        def guestIds = [1L] as long[]

        def customer = fakeCustomer
        def locationForEvent = fakeLocationForEventDto
        def guests = [fakeGuest]
        def organizedEvent = fakeOrganizedEvent
        organizedEvent.setGuests(new HashSet<>(guests))
        organizedEvent.setModifiedAt(now)

        when:
        customerService.addGuestsToEvent(id, eventId, guestIds)

        then:
        1 * customerRepository.findById(id) >> Optional.of(customer)
        1 * locationForEventService.findByEventId(eventId) >> locationForEvent
        1 * guestService.getGuestsByIds([1L]) >> guests
        1 * organizedEventService.get(eventId) >> organizedEvent
        1 * organizedEventService.save(organizedEvent)
    }

    def "SendInvitationToGuest"() {
        given:
        def eventId = 1L
        def customerId = 2L

        def event = fakeOrganizedEvent
        event.setEventStatus(READY.name())
        def address = fakeAddress
        def location = fakeLocation
        location.setLocationAddress(address)
        def locationForEvent = fakeLocationForEventDto
        locationForEvent.setEvent(fakeOrganizedEvent)
        locationForEvent.setLocation(location)
        def customer = fakeCustomer

        def organizedEvent = fakeOrganizedEvent
        organizedEvent.setGuests(Set.of(fakeGuest))
        organizedEvent.setEventType(EventType.builder()
                .id(1l)
                .type("SAMPLE TYPE")
                .build())
        organizedEvent.setCustomer(customer)
        organizedEvent.setLocationForEvent(Set.of(locationForEvent))

        def guest = fakeGuestDTO

        def invitationContent = OrganizedEventMapper.toDtoForInvite(organizedEvent)
        def emailContent = ComposeInviteEmailUtil.composeEmail(guest, invitationContent)
        def emailSubject = "Invitation From " + invitationContent.getCustomer().getFirstName() +
                " " + invitationContent.getCustomer().getLastName()
        def inviteEmail = EmailUtil.buildEmail(emailContent, guest.getEmail(), emailSubject, null)

        when:
        customerService.sendInvitationToGuest(eventId, customerId)

        then:
        1 * organizedEventService.getWithAllInformationForSendingInvitations(eventId, customerId) >> organizedEvent
        1 * emailService.sendEmail(inviteEmail)
    }

    def "uploadAvatar"() {
        given:
        def customer = fakeCustomer
        def customerId = 1l
        def file = new MockMultipartFile("data", "filename.jpg", "image", "some xml".getBytes())
        def avatar = fakeCustomer.getAvatar()
        when:
        customerService.uploadAvatar(customerId, file)

        then:
        1 * customerRepository.getByIdWithUser(customerId) >> Optional.of(customer)
        1 * customerAvatarService.save(avatar)
        1 * customerRepository.save(customer)

    }
}
