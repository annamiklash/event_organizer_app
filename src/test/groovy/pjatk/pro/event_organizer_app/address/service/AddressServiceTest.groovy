package pjatk.pro.event_organizer_app.address.service

import org.springframework.data.domain.Page
import pjatk.pro.event_organizer_app.address.repository.AddressRepository
import pjatk.pro.event_organizer_app.common.helper.TimestampHelper
import pjatk.pro.event_organizer_app.exceptions.NotFoundException
import pjatk.pro.event_organizer_app.trait.address.AddressTrait
import pjatk.pro.event_organizer_app.trait.page.PageTrait
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDateTime

class AddressServiceTest extends Specification implements PageTrait, AddressTrait {

    @Subject
    AddressService addressService

    AddressRepository addressRepository

    TimestampHelper timestampUtil

    def setup() {
        addressRepository = Mock()

        timestampUtil = Mock()

        addressService = new AddressService(addressRepository, timestampUtil)
    }

    def "list all service positive scenario"() {
        given:
        def page = fakePage

        when:
        addressService.list(page)

        then:
        addressRepository.findAll(fakePaging) >> Page.empty()
    }

    def "get by id positive scenario"() {
        given:
        def addressId = 1
        def fakeAddress = fakeAddress

        when:
        addressService.get(addressId)

        then:
        addressRepository.findById(addressId) >> Optional.of(fakeAddress)
    }

    def "get by id negative scenario"() {
        given:
        def addressId = 0

        when:
        addressService.get(addressId)

        then:
        thrown(NotFoundException)
        addressRepository.findById(addressId) >> { throw new NotFoundException('') }
    }

    def "get by user id positive scenario"() {
        given:
        def id = 1
        def fakeAddress = fakeAddress

        when:
        addressService.getByUserId(id)

        then:
        addressRepository.findByUserId(id) >> Optional.of(fakeAddress)
    }

    def "get by user id negative scenario"() {
        given:
        def id = 0

        when:
        addressService.getByUserId(id)

        then:
        thrown(NotFoundException)
        addressRepository.findByUserId(id) >> { throw new NotFoundException('') }
    }

    def "get by location id positive scenario"() {
        given:
        def id = 1
        def fakeAddress = fakeAddress

        when:
        addressService.getByLocationId(id)

        then:
        addressRepository.findByLocationId(id) >> Optional.of(fakeAddress)
    }

    def "get by location id negative scenario"() {
        given:
        def id = 0

        when:
        addressService.getByLocationId(id)

        then:
        thrown(NotFoundException)
        addressRepository.findByLocationId(id) >> { throw new NotFoundException('') }
    }

    def "get by catering id positive scenario"() {
        given:
        def id = 1
        def fakeAddress = fakeAddress

        when:
        addressService.getByCateringId(id)

        then:
        addressRepository.findByCateringId(id) >> Optional.of(fakeAddress)
    }

    def "get by catering id negative scenario"() {
        given:
        def id = 0

        when:
        addressService.getByCateringId(id)

        then:
        thrown(NotFoundException)
        addressRepository.findByCateringId(id) >> { throw new NotFoundException('') }
    }


    def "get by service id positive scenario"() {
        given:
        def id = 1
        def fakeAddress = fakeAddress

        when:
        addressService.getByServiceId(id)

        then:
        addressRepository.findByServiceId(id) >> Optional.of(fakeAddress)
    }

    def "get by service id negative scenario"() {
        given:
        def id = 0

        when:
        addressService.getByServiceId(id)

        then:
        thrown(NotFoundException)
        addressRepository.findByServiceId(id) >> { throw new NotFoundException('') }
    }

    def "exists by id positive scenario"() {
        given:
        def id = 1

        when:
        addressService.addressWithIdExists(id)

        then:
        addressRepository.existsById(id) >> true
    }

    def "save positive scenario"() {
        given:
        def address = fakeAddress

        when:
        addressService.save(address)

        then:
        1 * addressRepository.save(address)
    }

    def "create positive scenario"() {
        given:
        def dto = fakeAddressDto
        def address = fakeAddress
        def now = LocalDateTime.parse('2007-12-03T10:15:30')
        address.setCreatedAt(now)
        address.setModifiedAt(now)

        when:
        addressService.create(dto)

        then:
        2 * timestampUtil.now() >> now
        1 * addressRepository.save(address)
    }


    def "edit positive scenario"() {
        given:
        def id = 1
        def dto = fakeAddressDto
        def address = fakeAddressWithId
        def now = LocalDateTime.parse('2007-12-03T10:15:30')
        address.setModifiedAt(now)

        when:
        addressService.edit(id, dto)

        then:
        1 * addressRepository.findById(id) >> Optional.of(address)
        1 * timestampUtil.now() >> now
        1 * addressRepository.save(address)
        1 * addressRepository.existsById(id) >> true
    }

    def "edit not found exception test scenario"() {
        given:
        def id = 999
        def dto = fakeAddressDto

        when:
        addressService.edit(id, dto)

        then:
        1 * addressRepository.existsById(id) >> false
        thrown(NotFoundException)
    }

    def "delete by id positive scenario"() {
        given:
        def id = 999
        def address = fakeAddressWithId
        def now = LocalDateTime.parse('2007-12-03T10:15:30')
        address.setModifiedAt(now)
        address.setDeletedAt(now)

        when:
        addressService.delete(id)

        then:
        2 * timestampUtil.now() >> now
        1 * addressRepository.findById(id) >> Optional.of(address)
        1 * addressRepository.save(address)
    }

    def "delete positive scenario"() {
        given:
        def address = fakeAddressWithId

        when:
        addressService.delete(address)

        then:
        1 * addressRepository.delete(address)
    }

    def "count positive scenario"() {
        given:
        def total = 0

        when:
        addressService.count()

        then:
        1 * addressRepository.count() >> total
    }
}
