package pjatk.pro.event_organizer_app.customer.guest.service

import com.google.common.collect.ImmutableList
import org.springframework.data.domain.PageImpl
import pjatk.pro.event_organizer_app.common.helper.TimestampHelper
import pjatk.pro.event_organizer_app.customer.guest.repository.GuestRepository
import pjatk.pro.event_organizer_app.customer.repository.CustomerRepository
import pjatk.pro.event_organizer_app.trait.customer.CustomerTrait
import pjatk.pro.event_organizer_app.trait.customer.guest.GuestTrait
import pjatk.pro.event_organizer_app.trait.page.PageTrait
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDateTime


class GuestServiceTest extends Specification
        implements GuestTrait,
                PageTrait,
                CustomerTrait {

    @Subject
    GuestService guestService

    GuestRepository guestRepository
    CustomerRepository customerRepository
    TimestampHelper timestampHelper

    LocalDateTime now = LocalDateTime.parse('2007-12-03T10:15:30')


    def setup() {
        guestRepository = Mock()
        customerRepository = Mock()
        timestampHelper = Mock()

        timestampHelper.now() >> now

        guestService = new GuestService(guestRepository,
                customerRepository,
                timestampHelper)
    }

    def "List"() {
        def customPage = fakePage
        def keyword = 'no'

        def paging = fakePaging
        def page = new PageImpl<>([fakeGuest])

        def target = ImmutableList.of(fakeGuest)

        when:
        def result = guestService.list(customPage, keyword)

        then:
        1 * guestRepository.findAllWithKeyword(paging, keyword) >> page

        result == target

    }

    def "ListAllByCustomerId"() {
        given:
        def id = 1L

        def guests = [fakeGuest]
        def target = guests
        when:
        def result = guestService.listAllByCustomerId(id)

        then:
        1 * customerRepository.existsById(id) >> true
        1 * guestRepository.getAllByCustomer_Id(id) >> guests

        result == target
    }

    def "GetGuestsByIds"() {
        given:
        def guestIds = [1L]
        def guests = [fakeGuest]
        def guest = fakeGuest
        def target = guests

        when:
        def result = guestService.getGuestsByIds([1L])

        then:
        1 * guestRepository.findById(guestIds.iterator().next()) >> Optional.of(guest)

        target == result
    }

    def "Get"() {
        given:
        def id = 1L
        def guest = fakeGuest

        def target = guest
        when:
        def result = guestService.get(id)

        then:
        1 * guestRepository.findById(id) >> Optional.of(guest)

        result == target
    }

    def "Create"() {
        given:
        def id = 1L
        def dto = fakeGuestWithoutId
        def customer = fakeCustomer
        def guest = fakeGuest

        guest.setId(null)
        guest.setCustomer(customer)
        guest.setCreatedAt(now)
        guest.setModifiedAt(now)

        def target = guest

        when:
        def result = guestService.create(id, dto)

        then:
        1 * customerRepository.getByIdWithUser(id) >> Optional.of(customer)
        1 * guestRepository.save(guest)

        result == target
    }

    def "Edit"() {
        given:
        def id = 1L
        def dto = fakeGuestWithoutId
        def guest = fakeGuest

        guest.setFirstName(dto.getFirstName())
        guest.setLastName(dto.getLastName())
        guest.setEmail(dto.getEmail())
        guest.setModifiedAt(now)

        def target = guest

        when:
        def result = guestService.edit(id, dto)

        then:
        1 * guestRepository.findById(id) >> Optional.of(guest)
        1 * guestRepository.save(guest)

        result == target
    }

    def "Delete guest"() {
        given:
        def guest = fakeGuest

        when:
        guestService.delete(guest)

        then:
        1 * guestRepository.delete(guest)

    }

    def "Delete by customerId and guestId"() {
        given:
        def customerId = 1L
        def guestId = 1L
        def customer = fakeCustomer
        def guest = fakeGuest

        when:
        guestService.delete(customerId, guestId)

        then:
        1 * customerRepository.getByIdWithUser(customerId) >> Optional.of(customer)
        1 * guestRepository.findById(guestId) >> Optional.of(guest)
        1 * guestRepository.delete(guest)
    }
}
