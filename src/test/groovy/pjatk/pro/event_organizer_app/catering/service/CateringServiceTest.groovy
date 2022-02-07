package pjatk.pro.event_organizer_app.catering.service

import com.google.common.collect.ImmutableList
import com.google.common.collect.ImmutableSet
import org.codehaus.groovy.runtime.InvokerHelper
import org.springframework.data.domain.PageImpl
import pjatk.pro.event_organizer_app.address.service.AddressService
import pjatk.pro.event_organizer_app.business.repository.BusinessRepository
import pjatk.pro.event_organizer_app.businesshours.catering.service.CateringBusinessHoursService
import pjatk.pro.event_organizer_app.catering.model.Catering
import pjatk.pro.event_organizer_app.catering.model.dto.FilterCateringsDto
import pjatk.pro.event_organizer_app.catering.repository.CateringItemRepository
import pjatk.pro.event_organizer_app.catering.repository.CateringRepository
import pjatk.pro.event_organizer_app.common.convertors.Converter
import pjatk.pro.event_organizer_app.common.helper.TimestampHelper
import pjatk.pro.event_organizer_app.cuisine.model.Cuisine
import pjatk.pro.event_organizer_app.cuisine.service.CuisineService
import pjatk.pro.event_organizer_app.image.repository.CateringImageRepository
import pjatk.pro.event_organizer_app.location.service.LocationService
import pjatk.pro.event_organizer_app.reviews.catering.repository.CateringReviewRepository
import pjatk.pro.event_organizer_app.security.service.SecurityService
import pjatk.pro.event_organizer_app.trait.address.AddressTrait
import pjatk.pro.event_organizer_app.trait.business.BusinessTrait
import pjatk.pro.event_organizer_app.trait.businesshours.BusinessHoursTrait
import pjatk.pro.event_organizer_app.trait.catering.CateringTrait
import pjatk.pro.event_organizer_app.trait.catering.CuisineTrait
import pjatk.pro.event_organizer_app.trait.location.LocationTrait
import pjatk.pro.event_organizer_app.trait.page.PageTrait
import pjatk.pro.event_organizer_app.trait.user.UserCredentialsTrait
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDateTime

class CateringServiceTest extends Specification
        implements UserCredentialsTrait,
                AddressTrait,
                CateringTrait,
                LocationTrait,
                BusinessTrait,
                BusinessHoursTrait,
                PageTrait,
                CuisineTrait {

    @Subject
    CateringService cateringService

    CateringRepository cateringRepository
    CateringItemRepository cateringItemRepository
    LocationService locationService
    AddressService addressService
    SecurityService securityService
    BusinessRepository businessRepository
    CateringBusinessHoursService cateringBusinessHoursService
    CuisineService cuisineService
    CateringImageRepository cateringImageRepository
    CateringReviewRepository cateringReviewRepository
    TimestampHelper timestampHelper

    LocalDateTime now = LocalDateTime.parse('2007-12-03T10:15:30')

    def setup() {
        cateringRepository = Mock()
        cateringItemRepository = Mock()
        cateringItemRepository = Mock()
        locationService = Mock()
        addressService = Mock()
        securityService = Mock()
        businessRepository = Mock()
        cateringBusinessHoursService = Mock()
        cuisineService = Mock()
        timestampHelper = Mock()
        cateringImageRepository = Mock()
        cateringReviewRepository = Mock()

        timestampHelper.now() >> now

        cateringService = new CateringService(cateringRepository,
                cateringItemRepository,
                locationService,
                addressService,
                securityService,
                businessRepository,
                cateringBusinessHoursService,
                cuisineService,
                timestampHelper,
                cateringImageRepository,
                cateringReviewRepository)
    }

    def "list() positive test scenario"() {
        given:
        def customPage = fakePage
        def keyword = 'no'

        def paging = fakePaging
        def page = new PageImpl<>([fakeCateringOffersOutsideCatering])

        def target = ImmutableList.of(fakeCateringOffersOutsideCatering)

        when:
        def result = cateringService.list(customPage, keyword)

        then:
        1 * cateringRepository.findAllWithKeyword(paging, keyword) >> page

        result == target
    }

    def "get() positive test scenario"() {
        given:
        def id = 1L
        def catering = fakeCateringOffersOutsideCatering

        def target = catering
        when:
        def result = cateringService.get(id)

        then:
        1 * cateringRepository.findById(id) >> Optional.of(catering)

        result == target
    }

    def "getWithDetail() positive test scenario"() {
        given:
        def id = 1L
        def catering = fakeCateringOffersOutsideCatering

        def target = catering
        when:
        def result = cateringService.getWithDetail(id)

        then:
        1 * cateringRepository.findByIdWithDetail(id) >> Optional.of(catering)

        result == target
    }

    def "IsOpen"() {
        given:
        def catering = fakeCateringWithDetails
        def day = 'SUNDAY'

        when:
        def result = cateringService.isOpen(catering, day)

        then:

        result
    }

    def "getByBusinessId() positive test scenario"() {
        given:
        def id = 1L
        def caterings = [fakeCateringOffersOutsideCatering]

        def target = ImmutableList.of(fakeCateringOffersOutsideCatering)
        when:
        def result = cateringService.getByBusinessId(id)

        then:
        1 * cateringRepository.findAllByBusiness_Id(id) >> caterings

        result == target
    }

    def "create  positive test scenario"() {
        given:
        def userCredentials = fakeBusinessUserCredentials
        def address = fakeAddress
        def cateringBusinessHours = Set.of(fakeCateringBusinessHours)
        def business = fakeVerifiedBusiness
        def cateringDto = fakeCateringDtoOffersOutsideCatering
        def businessHoursDto = [fakeBusinessHoursDto]
        cateringDto.setBusinessHours(businessHoursDto)

        def cuisineDto = cateringDto.getCuisines().get(0)
        def catering = fakeCatering
        catering.setImages(null)
        fakeCatering.setId(null)
        def cuisine = Cuisine.builder().name(cuisineDto.getName()).build()

        def cateringSet = new HashSet<Catering>()
        cateringSet.add(catering)
        def location = fakeLocation
        location.setCaterings(cateringSet)
        def locations = ImmutableList.of(location)

        catering.setCateringAddress(address)
        catering.setBusiness(business)
        catering.setCateringBusinessHours(ImmutableSet.copyOf(cateringBusinessHours))
        catering.setCreatedAt(now)
        catering.setModifiedAt(now)
        catering.setLocations(new HashSet<>())
        catering.setServiceCost(new BigDecimal("100.20"))
        catering.setRating(0.0)

        def target = Catering.builder().build()
        InvokerHelper.setProperties(target, catering.properties)
        target.setCuisines(Set.of(cuisine))

        when:
        def result = cateringService.create(cateringDto, null)

        then:
        1 * securityService.getUserCredentials() >> userCredentials
        1 * businessRepository.findById(userCredentials.getUserId()) >> Optional.of(business)

        1 * addressService.create(cateringDto.getAddress()) >> address
        1 * cateringBusinessHoursService.create(businessHoursDto) >> cateringBusinessHours
        1 * cuisineService.getByName(cuisineDto.getName()) >> cuisine

        1 * locationService.findByCity(address.getCity()) >> locations

        1 * cateringRepository.saveAndFlush(_)

        result == target
    }

    def "create with location Id positive test scenario"() {
        given:
        def locationId = 1L
        def userCredentials = fakeBusinessUserCredentials
        def address = fakeAddress
        def cateringBusinessHours = Set.of(fakeCateringBusinessHours)
        def business = fakeVerifiedBusiness
        def cateringDto = fakeCateringDtoOffersOutsideCatering
        def businessHoursDto = [fakeBusinessHoursDto]
        cateringDto.setBusinessHours(businessHoursDto)

        def cuisineDto = cateringDto.getCuisines().get(0)
        def catering = fakeCatering
        catering.setImages(null)

        fakeCatering.setId(null)
        def cuisine = Cuisine.builder().name(cuisineDto.getName()).build()

        def cateringSet = new HashSet<Catering>()
        cateringSet.add(catering)
        def location = fakeLocation
        location.setCaterings(cateringSet)
        def locations = ImmutableList.of(location)

        catering.setCateringAddress(address)
        catering.setBusiness(business)
        catering.setCateringBusinessHours(ImmutableSet.copyOf(cateringBusinessHours))
        catering.setCreatedAt(now)
        catering.setModifiedAt(now)
        catering.setLocations(new HashSet<>())
        catering.setServiceCost(new BigDecimal("100.20"))
        catering.setRating(0.0)

        def target = Catering.builder().build()
        InvokerHelper.setProperties(target, catering.properties)
        target.setCuisines(Set.of(cuisine))

        when:
        def result = cateringService.create(cateringDto, locationId)

        then:
        1 * securityService.getUserCredentials() >> userCredentials
        1 * businessRepository.findById(userCredentials.getUserId()) >> Optional.of(business)

        1 * addressService.create(cateringDto.getAddress()) >> address
        1 * cateringBusinessHoursService.create(businessHoursDto) >> cateringBusinessHours
        1 * cuisineService.getByName(cuisineDto.getName()) >> cuisine

        1 * locationService.findByCity(address.getCity()) >> locations

        1 * cateringRepository.saveAndFlush(_)

        result == target
    }

    def "getWithBusinessHours() positive test scenario"() {
        given:
        def cateringId = 1L
        def catering = fakeCatering

        def target = catering
        when:
        def result = cateringService.getWithBusinessHours(cateringId)

        then:
        1 * cateringRepository.getWithBusinessHours(cateringId) >> Optional.of(catering)

        result == target
    }


    def "search no params"() {
        given:
        def dto = FilterCateringsDto.builder().build();
        def locationId = null

        def caterings = [fakeCateringWithDetails]
        def target = ImmutableList.of(fakeCateringWithDetails)

        when:
        def result = cateringService.search(dto, locationId)

        then:
        1 * cateringRepository.search(null, "", locationId) >> caterings

        result == target
    }

    def "search with cuisines"() {
        given:
        def dto = FilterCateringsDto.builder()
                .cuisines(List.of('Greek'))
                .build()
        def locationId = null

        def catering = fakeCateringWithDetails
        catering.setBusiness(null)
        catering.setCateringItems(null)
        def caterings = [catering]
        def target = ImmutableList.of(catering)
        def cuisine =
                Cuisine.builder()
                        .id(1l)
                        .name('Greek')
                        .build()

        def cuisines = cuisine
        def cuisinesIds = Set.of(cuisine.getId())

        when:
        def result = cateringService.search(dto, locationId)

        then:
        1 * cuisineService.getByName(dto.getCuisines().iterator().next()) >> cuisine
        1 * cateringRepository.search(cuisinesIds, "", locationId) >> caterings

        result == target
    }

    def "search with date and price"() {
        given:
        def dto = FilterCateringsDto.builder()
                .date('2022-01-30')
                .minPrice(1)
                .maxPrice(10000)
                .build()
        def locationId = null


        def caterings = [fakeCateringWithDetails]
        def target = ImmutableList.of(fakeCateringWithDetails)

        when:
        def result = cateringService.search(dto, locationId)

        then:
        1 * cateringRepository.search(null, "", locationId) >> caterings

        result == target
    }


    def "edit() positive test scenario"() {
        given:
        def cateringId = 1L
        def dto = fakeCateringDtoOffersOutsideCatering

        def catering = fakeCatering
        catering.setEmail(dto.getEmail())
        catering.setName(dto.getName())
        catering.setPhoneNumber(Converter.convertPhoneNumberString(dto.getPhoneNumber()))
        catering.setServiceCost(Converter.convertPriceString(dto.getServiceCost()))
        catering.setDescription(dto.getDescription())
        catering.setModifiedAt(now)

        def inputDtoCuisines = dto.getCuisines()

        def target = catering

        when:
        def result = cateringService.edit(cateringId, dto)

        then:
        1 * cateringRepository.findById(cateringId) >> Optional.of(catering)
        1 * cuisineService.getByName(inputDtoCuisines.iterator().next().getName())
        1 * cateringRepository.save(catering)

        result == target
    }

    def "deleteLogical() positive test scenario"() {
        given:
        def id = 1L
        def catering = fakeCatering

        when:
        cateringService.delete(id)

        then:
        1 * cateringRepository.findAllCateringInformation(id) >> Optional.of(catering)
        1 * addressService.delete(catering.getCateringAddress())
        1 * cateringRepository.delete(catering)

        noExceptionThrown()
    }

    def "cateringWithIdExists() positive test scenario"() {
        given:
        def id = 1L

        when:
        def result = cateringService.cateringWithIdExists(id)

        then:
        1 * cateringRepository.existsById(id) >> true

        result
    }

    def "getWithImages() positive test scenario"() {
        given:
        def id = 1L
        def catering = fakeCatering

        def target = catering

        when:
        def result = cateringService.getWithImages(id)

        then:
        1 * cateringRepository.findWithImages(id) >> Optional.of(catering)

        result == target
    }

    def "getByLocationId() positive test scenario"() {
        given:
        def id = 1L
        def catering = fakeCatering

        def target = ImmutableList.of(catering)

        when:
        def result = cateringService.getByLocationId(id)

        then:
        1 * cateringRepository.findAllByLocationId(id) >> [catering]

        result == target
    }

    def "count() positive test scenario"() {
        given:
        def keyword = '123'

        def target = 123L

        when:
        def result = cateringService.count(keyword)

        then:
        1 * cateringRepository.countAll(keyword) >> target

        result == target
    }

}
