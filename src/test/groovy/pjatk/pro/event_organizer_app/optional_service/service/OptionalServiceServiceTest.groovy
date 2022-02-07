package pjatk.pro.event_organizer_app.optional_service.service

import com.google.common.base.Strings
import com.google.common.collect.ImmutableList
import com.google.common.collect.ImmutableSet
import org.springframework.data.domain.PageImpl
import pjatk.pro.event_organizer_app.address.service.AddressService
import pjatk.pro.event_organizer_app.availability.optionalservice.repository.OptionalServiceAvailabilityRepository
import pjatk.pro.event_organizer_app.business.repository.BusinessRepository
import pjatk.pro.event_organizer_app.businesshours.optionalservice.model.OptionalServiceBusinessHours
import pjatk.pro.event_organizer_app.businesshours.optionalservice.service.OptionalServiceBusinessHoursService
import pjatk.pro.event_organizer_app.common.convertors.Converter
import pjatk.pro.event_organizer_app.common.helper.TimestampHelper
import pjatk.pro.event_organizer_app.exceptions.BusinessVerificationException
import pjatk.pro.event_organizer_app.image.model.OptionalServiceImage
import pjatk.pro.event_organizer_app.image.repository.OptionalServiceImageRepository
import pjatk.pro.event_organizer_app.optional_service.model.dto.FilterOptionalServiceDto
import pjatk.pro.event_organizer_app.optional_service.model.interpreter.translation.model.TranslationLanguage
import pjatk.pro.event_organizer_app.optional_service.model.interpreter.translation.service.TranslationLanguageService
import pjatk.pro.event_organizer_app.optional_service.model.music.musicstyle.service.MusicStyleService
import pjatk.pro.event_organizer_app.optional_service.optional_service_for_location.repostory.OptionalServiceForChosenLocationRepository
import pjatk.pro.event_organizer_app.optional_service.repository.OptionalServiceRepository
import pjatk.pro.event_organizer_app.reviews.service.model.OptionalServiceReview
import pjatk.pro.event_organizer_app.reviews.service.service.OptionalServiceReviewService
import pjatk.pro.event_organizer_app.security.service.SecurityService
import pjatk.pro.event_organizer_app.trait.address.AddressTrait
import pjatk.pro.event_organizer_app.trait.availability.AvailabilityTrait
import pjatk.pro.event_organizer_app.trait.business.BusinessTrait
import pjatk.pro.event_organizer_app.trait.businesshours.BusinessHoursTrait
import pjatk.pro.event_organizer_app.trait.location.locationforevent.LocationForEventTrait
import pjatk.pro.event_organizer_app.trait.optional_service.OptionalServiceForChosenLocationTrait
import pjatk.pro.event_organizer_app.trait.optional_service.OptionalServiceTrait
import pjatk.pro.event_organizer_app.trait.page.PageTrait
import pjatk.pro.event_organizer_app.trait.user.UserCredentialsTrait
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDateTime

class OptionalServiceServiceTest extends Specification
        implements OptionalServiceTrait,
                AddressTrait,
                UserCredentialsTrait,
                PageTrait,
                BusinessTrait,
                AvailabilityTrait,
                LocationForEventTrait,
                OptionalServiceForChosenLocationTrait,
                BusinessHoursTrait {

    @Subject
    OptionalServiceService optionalServiceService

    OptionalServiceRepository optionalServiceRepository
    BusinessRepository businessRepository
    SecurityService securityService
    AddressService addressService
    MusicStyleService musicStyleService
    OptionalServiceBusinessHoursService optionalServiceBusinessService
    TranslationLanguageService translationLanguageService
    OptionalServiceAvailabilityRepository optionalServiceAvailabilityRepository
    OptionalServiceForChosenLocationRepository optionalServiceForChosenLocationRepository
    OptionalServiceImageRepository optionalServiceImageRepository
    OptionalServiceReviewService optionalServiceReviewService
    TimestampHelper timestampHelper

    LocalDateTime now = LocalDateTime.parse('2007-12-03T10:15:30')

    def setup() {

        optionalServiceRepository = Mock()
        businessRepository = Mock()
        securityService = Mock()
        addressService = Mock()
        musicStyleService = Mock()
        optionalServiceBusinessService = Mock()
        translationLanguageService = Mock()
        optionalServiceAvailabilityRepository = Mock()
        optionalServiceForChosenLocationRepository = Mock()
        optionalServiceImageRepository = Mock()
        optionalServiceReviewService = Mock()
        timestampHelper = Mock()

        timestampHelper.now() >> now

        optionalServiceService = new OptionalServiceService(
                optionalServiceRepository,
                businessRepository,
                securityService,
                addressService,
                musicStyleService,
                optionalServiceBusinessService,
                translationLanguageService,
                optionalServiceAvailabilityRepository,
                optionalServiceForChosenLocationRepository,
                optionalServiceReviewService,
                optionalServiceImageRepository,
                timestampHelper)
    }

    def "List"() {
        given:
        def customPage = fakePage
        def keyword = 'no'

        def paging = fakePaging
        def page = new PageImpl<>([fakeOptionalService])

        def target = ImmutableList.of(fakeOptionalService)

        when:
        def result = optionalServiceService.list(customPage, keyword)

        then:
        1 * optionalServiceRepository.findAllWithKeyword(paging, keyword) >> page

        result == target
    }

    def "Get"() {
        given:
        def id = 1L
        def optionalService = fakeOptionalService

        def target = optionalService
        when:
        def result = optionalServiceService.get(id)

        then:
        1 * optionalServiceRepository.findWithDetail(id) >> Optional.of(optionalService)

        result == target
    }

    def "GetCities"() {
        given:
        def target = List.of("Warsaw, Poland")

        when:
        def result = optionalServiceService.getCities()

        then:
        1 * optionalServiceRepository.findDistinctCities() >> List.of("Warsaw, Poland")

        result == target
    }

    def "GetWithDetail"() {
        given:
        def id = 1L
        def optionalService = fakeOptionalService

        def target = optionalService
        when:
        def result = optionalServiceService.getWithDetail(id)

        then:
        1 * optionalServiceRepository.findWithDetail(id) >> Optional.of(optionalService)

        result == target
    }

    def "Create BusinessVerificationException"() {
        given:
        def userCredentials = fakeBusinessUserCredentials
        def business = fakeVerifiedBusiness
        business.setVerificationStatus('NOT_VERIFIED')
        def serviceDto = fakeOptionalServiceHostDto
        def businessHoursDto = [fakeBusinessHoursDto]
        serviceDto.setBusinessHours(businessHoursDto)

        when:
        optionalServiceService.create(serviceDto)

        then:
        1 * securityService.getUserCredentials() >> userCredentials
        1 * businessRepository.findById(userCredentials.getUserId()) >> Optional.of(business)
        thrown(BusinessVerificationException.class)
    }

    def "Create HOST"() {
        given:
        def userCredentials = fakeBusinessUserCredentials
        def address = fakeAddress
        def serviceBusinessHours = List.of(fakeServiceBusinessHours)
        def business = fakeVerifiedBusiness
        def serviceDto = fakeOptionalServiceHostDto
        def host = fakeOptionalHost
        def businessHoursDto = [fakeBusinessHoursDto]
        def availability = fakeServiceAvailability
        availability.setOptionalService(host)
        serviceDto.setBusinessHours(businessHoursDto)

        host.setId(null)
        host.setServiceAddress(address)
        host.setBusiness(business)
        host.setOptionalServiceBusinessHours(ImmutableSet.copyOf(serviceBusinessHours))
        host.setCreatedAt(now)
        host.setModifiedAt(now)
        host.setServiceCost(new BigDecimal('123.00'))
        host.setRating(0.0)

        def target = host

        when:
        def result = optionalServiceService.create(serviceDto)

        then:
        1 * securityService.getUserCredentials() >> userCredentials
        1 * businessRepository.findById(userCredentials.getUserId()) >> Optional.of(business)
        1 * addressService.create(serviceDto.getAddress()) >> address
        1 * optionalServiceBusinessService.create(businessHoursDto) >> serviceBusinessHours
        1 * optionalServiceRepository.save(_)
//        1 * optionalServiceAvailabilityRepository.save(availability)

        result == target
    }

    def "Edit"() {
        given:
        def id = 1l
        def dto = fakeOptionalServiceHostDto
        def host = fakeOptionalHost

        host.setAlias(dto.getAlias())
        host.setFirstName(dto.getFirstName())
        host.setLastName(dto.getFirstName())
        host.setEmail(dto.getEmail())
        host.setServiceCost(Converter.convertPriceString(dto.getServiceCost()))
        host.setDescription(dto.getDescription())
        host.setModifiedAt(timestampHelper.now())

        def target = host

        when:
        def result = optionalServiceService.edit(dto, id)

        then:
        1 * optionalServiceRepository.findWithDetail(id) >> Optional.of(host)
        1 * optionalServiceRepository.save(_)

        result == target
    }

    def "Search"() {
        given:
        def dto = FilterOptionalServiceDto.builder()
                .city('Warsaw, Poland')
                .date('2022-02-01')
                .minPrice('0')
                .maxPrice('10000')
                .type('HOST')
                .build()

        def city = dto.getCity()
        city = Strings.isNullOrEmpty(dto.getCity()) ? "" : city.substring(0, city.indexOf(','));

        def host = fakeOptionalHostWithAvailability
        host.setServiceCost(200.00)
        def hosts = [host]

        def hostResult = host
        def rating = 3
        hostResult.setRating(rating);

        def target = ImmutableList.of(hostResult)

        when:
        def result = optionalServiceService.search(dto)

        then:
        1 * optionalServiceRepository.search(dto.getDate(), dto.getType(), city) >> hosts
        1 * optionalServiceReviewService.getRating(host.getId()) >> rating

        result == target
    }

    def "IsAvailable"() {
        given:
        def id = 1L
        def date = '2022-01-01'
        def timeFrom = '11:00'
        def timeTo = '17:00'
        def dateTimeFrom = date + " " + timeFrom
        def dateTimeTo = date + " " + timeTo

        def target = true

        when:
        def result = optionalServiceService.isAvailable(id, date, timeFrom, timeTo)

        then:
        1 * optionalServiceRepository.available(id, date, dateTimeFrom, dateTimeTo) >> Optional.of(fakeOptionalService)

        result == target
    }

    def "GetWithImages"() {
        given:
        def id = 1L
        def service = fakeOptionalService

        def target = fakeOptionalService

        when:
        def result = optionalServiceService.getWithImages(id)

        then:
        1 * optionalServiceRepository.findWithImages(id) >> Optional.of(service)

        result == target

    }

    def "Count"() {
        given:
        def keyword = 'no'

        def target = 1L

        when:
        def result = optionalServiceService.count(keyword)

        then:
        1 * optionalServiceRepository.countAll(keyword) >> target

        result == target
    }

    def "GetByBusinessId"() {
        given:
        def id = 1L
        def services = [fakeOptionalService]

        def target = ImmutableList.of(fakeOptionalService)

        when:
        def result = optionalServiceService.getByBusinessId(id)

        then:
        1 * optionalServiceRepository.findAllByBusiness_Id(id) >> services

        result == target
    }

    def "Delete"() {
        given:
        def id = 1l
        def serviceToDelete = fakeOptionalInterpreterWithAvailability

        def serviceReservation = fakeOptionalServiceForChosenLocation
        def event = serviceReservation.getLocationForEvent().getEvent()
        event.setEventStatus('FINISHED')

        serviceToDelete.setReviews(
                Set.of(
                        OptionalServiceReview.builder()
                                .id(1l)
                                .title('title')
                                .build()))

        serviceToDelete.setImages(
                Set.of(
                        OptionalServiceImage.builder()
                                .id(1l)
                                .build()))

        serviceToDelete.setOptionalServiceBusinessHours(
                Set.of(
                        OptionalServiceBusinessHours.builder()
                                .id(1l)
                                .day('MONDAY')
                                .build()))

        serviceToDelete.setServiceForLocation(Set.of(serviceReservation))
        def languages = List.of(
                TranslationLanguage.builder()
                        .id(1l)
                        .name('English')
                        .build())
        serviceToDelete.setLanguages(new HashSet<TranslationLanguage>(languages))

        def toDelete = serviceToDelete

        when:
        optionalServiceService.delete(id)

        then:
        1 * optionalServiceRepository.getAllServiceInformation(id) >> Optional.of(serviceToDelete)
        1 * optionalServiceBusinessService.delete(serviceToDelete.getOptionalServiceBusinessHours().iterator().next())
        1 * optionalServiceAvailabilityRepository.delete(serviceToDelete.getAvailability().iterator().next())
        1 * optionalServiceForChosenLocationRepository.delete(serviceToDelete.getServiceForLocation().iterator().next())
        1 * optionalServiceImageRepository.delete(serviceToDelete.getImages().iterator().next())
        1 * optionalServiceReviewService.delete(serviceToDelete.getReviews().iterator().next())
        1 * addressService.delete(serviceToDelete.getServiceAddress())
        1 * translationLanguageService.getAllByInterpreterId(serviceToDelete.getId()) >> languages
        1 * optionalServiceRepository.delete(toDelete);
    }
}
