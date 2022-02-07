package pjatk.pro.event_organizer_app.location.service

import com.google.common.base.Strings
import com.google.common.collect.ImmutableList
import org.springframework.data.domain.PageImpl
import pjatk.pro.event_organizer_app.address.service.AddressService
import pjatk.pro.event_organizer_app.availability.location.model.LocationAvailability
import pjatk.pro.event_organizer_app.availability.location.repository.LocationAvailabilityRepository
import pjatk.pro.event_organizer_app.business.repository.BusinessRepository
import pjatk.pro.event_organizer_app.businesshours.location.model.LocationBusinessHours
import pjatk.pro.event_organizer_app.businesshours.location.service.LocationBusinessHoursService
import pjatk.pro.event_organizer_app.catering.model.Catering
import pjatk.pro.event_organizer_app.catering.repository.CateringRepository
import pjatk.pro.event_organizer_app.common.convertors.Converter
import pjatk.pro.event_organizer_app.common.helper.TimestampHelper
import pjatk.pro.event_organizer_app.common.util.DateTimeUtil
import pjatk.pro.event_organizer_app.image.model.LocationImage
import pjatk.pro.event_organizer_app.image.repository.LocationImageRepository
import pjatk.pro.event_organizer_app.location.model.LocationDescriptionItem
import pjatk.pro.event_organizer_app.location.model.dto.FilterLocationsDto
import pjatk.pro.event_organizer_app.location.repository.LocationRepository
import pjatk.pro.event_organizer_app.reviews.location.model.LocationReview
import pjatk.pro.event_organizer_app.reviews.location.service.LocationReviewService
import pjatk.pro.event_organizer_app.security.service.SecurityService
import pjatk.pro.event_organizer_app.trait.address.AddressTrait
import pjatk.pro.event_organizer_app.trait.availability.AvailabilityTrait
import pjatk.pro.event_organizer_app.trait.business.BusinessTrait
import pjatk.pro.event_organizer_app.trait.businesshours.BusinessHoursTrait
import pjatk.pro.event_organizer_app.trait.catering.CateringTrait
import pjatk.pro.event_organizer_app.trait.location.LocationTrait
import pjatk.pro.event_organizer_app.trait.location.locationforevent.LocationForEventTrait
import pjatk.pro.event_organizer_app.trait.page.PageTrait
import pjatk.pro.event_organizer_app.trait.user.UserCredentialsTrait
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month

class LocationServiceTest extends Specification implements LocationTrait,
        AddressTrait,
        CateringTrait,
        BusinessTrait,
        BusinessHoursTrait,
        PageTrait,
        UserCredentialsTrait,
        AvailabilityTrait,
        LocationForEventTrait {

    @Subject
    LocationService locationService

    LocationRepository locationRepository
    LocationDescriptionItemService locationDescriptionItemService
    CateringRepository cateringRepository
    AddressService addressService
    BusinessRepository businessRepository
    LocationAvailabilityRepository locationAvailabilityRepository
    LocationBusinessHoursService locationBusinessHoursService
    SecurityService securityService
    LocationReviewService locationReviewService
    LocationImageRepository locationImageRepository
    TimestampHelper timestampHelper

    LocalDateTime now = LocalDateTime.parse('2007-12-03T10:15:30')

    def setup() {
        locationRepository = Mock()
        locationDescriptionItemService = Mock()
        cateringRepository = Mock()
        addressService = Mock()
        businessRepository = Mock()
        locationAvailabilityRepository = Mock()
        locationBusinessHoursService = Mock()
        securityService = Mock()
        locationReviewService = Mock()
        locationImageRepository = Mock()
        timestampHelper = Mock()

        timestampHelper.now() >> now

        locationService = new LocationService(locationRepository,
                locationDescriptionItemService,
                cateringRepository,
                addressService,
                businessRepository,
                locationAvailabilityRepository,
                locationBusinessHoursService,
                securityService,
                locationReviewService,
                locationImageRepository,
                timestampHelper)
    }

    def "List"() {
        given:
        def customPage = fakePage
        def keyword = 'no'

        def paging = fakePaging
        def page = new PageImpl<>([fakeFullLocation])

        def target = ImmutableList.of(fakeFullLocation)

        when:
        def result = locationService.list(customPage, keyword)

        then:
        1 * locationRepository.findAllWithKeyword(paging, keyword) >> page

        result == target
    }

    def "Count"() {
        given:
        def keyword = '123'

        def target = 123L

        when:
        def result = locationService.count(keyword)

        then:
        1 * locationRepository.countAll(keyword) >> target

        result == target
    }

    def "Get"() {
        given:
        def id = 1L
        def location = fakeFullLocation

        def target = location
        when:
        def result = locationService.get(id)

        then:
        1 * locationRepository.findById(id) >> Optional.of(location)

        result == target
    }


    def "GetWithDetail"() {
        given:
        def id = 1L
        def location = fakeFullLocation
        def rating = 4
        location.setRating(rating)

        def target = location
        when:
        def result = locationService.getWithDetail(id)

        then:
        1 * locationRepository.getByIdWithDetail(id) >> Optional.of(location)
        1 * locationReviewService.getRating(id) >> rating

        result == target
    }

    def "IsAvailable"() {
        given:
        def id = 1l
        def date = '2022-02-01'
        def timeFrom = '10:00'
        def timeTo = '18:00'

        def dateTimeFrom = DateTimeUtil.joinDateAndTime(date, timeFrom);
        def dateTimeTo = DateTimeUtil.joinDateAndTime(date, timeTo);

        def location = fakeFullLocationWithAvailability

        when:
        def result = locationService.isAvailable(id, date, timeFrom, timeTo)

        then:
        1 * locationRepository.available(id, date, dateTimeFrom, dateTimeTo) >> Optional.of(location)

        result

    }

    def "GetCities"() {
        given:
        def cities = ['Warsaw, Poland']
        def target = cities

        when:
        def result = locationService.getCities()

        then:
        1 * locationRepository.findDistinctCities() >> cities

        result == target
    }

    def "Exists"() {
        given:
        def id = 1L

        when:
        def result = locationService.exists(id)

        then:
        1 * locationRepository.existsById(id) >> true

        result
    }

    def "Search"() {
        given:
        def dto = FilterLocationsDto.builder()
                .city('Warsaw, Poland')
                .descriptionItems(['Has WiFi'])
                .date('2022-02-01')
                .guestCount(10)
                .isSeated(true)
                .build()
        def city = dto.getCity();
        city = Strings.isNullOrEmpty(dto.getCity()) ? "" : city.substring(0, city.indexOf(','));

        def location = fakeFullLocationWithAvailability
        def rating = 4.0
        def locationDescription = LocationDescriptionItem.builder()
                .id('Has WiFi')
                .description('Description')
                .build()
//        location.setRating(rating)
        location.setDescriptions(
                Set.of(locationDescription))
        location.setLocationBusinessHours(null)

        def newLoc = location

        def locations = [newLoc]

        def target = ImmutableList.copyOf(location)

        when:
        def result = locationService.search(dto)

        then:
        1 * locationDescriptionItemService.getById(dto.getDescriptionItems().iterator().next()) >> locationDescription
        1 * locationRepository.searchWithDate(city, dto.getDate()) >> locations


        result == target
    }

    def "FindByCity"() {
        given:
        def city = 'Warsaw'
        def locations = ImmutableList.of(fakeFullLocation)
        def target = locations

        when:
        def result = locationService.findByCity(city)

        then:
        1 * locationRepository.findByLocationAddress_City(city) >> locations

        result == target
    }

    def "Create"() {
        given:
        def userCredentials = fakeBusinessUserCredentials
        def business = fakeVerifiedBusiness
        def dto = fakeLocationDtoCreate
        def address = fakeAddress
        def businessHours = [fakeLocationBusinessHours]

        def descriptionsDto = dto.getDescriptions()
        def description = LocationDescriptionItem.builder()
                .id('Outside Catering Available')
                .description('desc')
                .build()
        def descriptions = Set.of(description)

        def caterings = [fakeCatering]

        def locationToSave = fakeLocationToCreate
        locationToSave.setLocationAddress(address)
        locationToSave.setBusiness(business)
        locationToSave.setDescriptions(descriptions)
        locationToSave.setLocationBusinessHours(new HashSet<>(businessHours))
        locationToSave.setImages(new HashSet<>())
        locationToSave.setRating(0.0)
        locationToSave.setCreatedAt(now)
        locationToSave.setModifiedAt(now)
        locationToSave.setCaterings(new HashSet<Catering>(caterings))

        def target = locationToSave

        when:
        def result = locationService.create(dto)

        then:
        1 * securityService.getUserCredentials() >> userCredentials
        1 * businessRepository.findById(userCredentials.getUserId()) >> Optional.of(business)
        1 * addressService.create(dto.getAddress()) >> address
        1 * locationBusinessHoursService.create(dto.getBusinessHours()) >> businessHours
        1 * locationDescriptionItemService.getById(descriptionsDto.iterator().next()) >> description
        1 * cateringRepository.findByCateringAddress_City(address.getCity()) >> caterings
        1 * locationRepository.save(target)

        result == target
    }

    def "Save"() {
        given:
        def location = fakeFullLocation

        when:
        locationService.save(location)

        then:
        1 * locationRepository.save(location)
    }

    def "GetWithAvailability"() {
        given:
        def id = 1L
        def date = '2021-02-01'
        def rating = 5
        def locations = fakeFullLocation
        locations.setRating(rating)

        def target = fakeFullLocation
        when:
        def result = locationService.getWithAvailability(id, date)

        then:
        1 * locationRepository.getByIdWithAvailability(id, date) >> Optional.of(locations)
        1 * locationReviewService.getRating(id) >> rating

        result == target
    }

    def "Edit"() {
        given:
        def locationId = 1l
        def dto = fakeLocationDtoCreate
        dto.setDescriptions(Set.of('Has WiFi'))

        def inputDescriptionEnums = dto.getDescriptions()

        def location = fakeFullLocation
        location.setCaterings(new HashSet<Catering>())
        location.setAvailability(Set.of(fakeLocationAvailabilityWithId))
        def description = LocationDescriptionItem.builder()
                .id('Has WiFi')
                .description('desc')
                .build()
        location.setDescriptions(Set.of(description))

        def target = location

        target.setEmail(dto.getEmail())
        target.setName(dto.getName())
        target.setPhoneNumber(Converter.convertPhoneNumberString(dto.getPhoneNumber()))
        target.setDescription(dto.getDescription())
        target.setSeatingCapacity(dto.getSeatingCapacity())
        target.setStandingCapacity(dto.getStandingCapacity())
        target.setDailyRentCost(Converter.convertPriceString(dto.getDailyRentCost()))
        target.setModifiedAt(now)

        when:
        def result = locationService.edit(dto, locationId)

        then:
        1 * locationRepository.getByIdWithDetail(locationId) >> Optional.of(location)
        1 * locationDescriptionItemService.getById(inputDescriptionEnums.iterator().next()) >> description
        1 * locationRepository.save(location)

        result == target
    }

    def "GetByBusinessId"() {
        given:
        def id = 1L
        def locations = [fakeFullLocation]

        def target = ImmutableList.of(fakeFullLocation)
        when:
        def result = locationService.getByBusinessId(id)

        then:
        1 * locationRepository.findAllByBusiness_Id(id) >> locations

        result == target
    }

    def "GetWithImages"() {
        given:
        def id = 1L
        def locations = fakeFullLocation

        def target = fakeFullLocation
        when:
        def result = locationService.getWithImages(id)

        then:
        1 * locationRepository.findWithImages(id) >> Optional.of(locations)

        result == target

    }

    def "GetByCateringId"() {
        given:
        def id = 1L
        def location = fakeFullLocation

        def target = ImmutableList.of(location)

        when:
        def result = locationService.getByCateringId(id)

        then:
        1 * locationRepository.findAllByCateringId(id) >> [location]

        result == target
    }

    def "Delete"() {
        given:
        def id = 1l
        def locationToDelete = fakeFullLocationWithAvailability
        def locationReservation = fakeFullLocationForEvent
        locationReservation.setLocation(null)
        def event = locationReservation.getEvent()
        event.setEventStatus('FINISHED')

        locationToDelete.setLocationForEvent(Set.of(locationReservation))

        locationToDelete.setCaterings(
                Set.of(
                        Catering.builder()
                                .id(1l)
                                .name('Name')
                                .build()))
        locationToDelete.setReviews(
                Set.of(
                        LocationReview.builder()
                                .id(1l)
                                .title('title')
                                .build()))
        locationToDelete.setImages(
                Set.of(
                        LocationImage.builder()
                                .id(1l)
                                .build()))
        locationToDelete.setLocationBusinessHours(
                Set.of(
                        LocationBusinessHours.builder()
                                .id(1l)
                                .build()))
        def description = LocationDescriptionItem.builder()
                .id('Has WiFi')
                .description('desc')
                .build()
        locationToDelete.setDescriptions(Set.of(description))

        def finalToDelete = locationToDelete
        finalToDelete.setDescriptions(null)
        finalToDelete.setCaterings(null)

        when:
        locationService.delete(id)

        then:
        1 * locationRepository.getAllLocationInformation(id) >> Optional.of(locationToDelete)
        1 * locationBusinessHoursService.delete(locationToDelete.getLocationBusinessHours().iterator().next())
        1 * locationImageRepository.delete(locationToDelete.getImages().iterator().next())
        1 * locationReviewService.delete(locationToDelete.getReviews().iterator().next())
        1 * addressService.delete(locationToDelete.getLocationAddress())
        1 * locationRepository.delete(finalToDelete)

    }

    def "ModifyAvailabilityAfterBooking"() {
        def location = fakeFullLocationWithAvailability
        def eventDate = '2022-02-01'
        def dateTimeFrom = '2022-02-01 13:00'
        def dateTimeTo = '2022-02-01 18:00'

        def date = DateTimeUtil.fromStringToFormattedDate(eventDate);
        def timeFrom = DateTimeUtil.fromStringToFormattedDateTime(dateTimeFrom);
        def timeTo = DateTimeUtil.fromStringToFormattedDateTime(dateTimeTo);
        def availability = location.getAvailability()[0]
        availability.setLocation(location)

        def newAvailabilities = List.of(
                LocationAvailability.builder()
                        .status('AVAILABLE')
                        .date(LocalDate.of(2022, Month.FEBRUARY, 1))
                        .timeFrom(LocalDateTime.of(2022, Month.FEBRUARY, 1, 9, 0, 0))
                        .timeTo(LocalDateTime.of(2022, Month.FEBRUARY, 1, 13, 0, 0))
                        .location(location)
                        .build(),
                LocationAvailability.builder()
                        .status('NOT_AVAILABLE')
                        .date(LocalDate.of(2022, Month.FEBRUARY, 1))
                        .timeFrom(LocalDateTime.of(2022, Month.FEBRUARY, 1, 13, 0, 0))
                        .timeTo(LocalDateTime.of(2022, Month.FEBRUARY, 1, 18, 0, 0))
                        .location(location)
                        .build(),
                LocationAvailability.builder()
                        .status('AVAILABLE')
                        .date(LocalDate.of(2022, Month.FEBRUARY, 1))
                        .timeFrom(LocalDateTime.of(2022, Month.FEBRUARY, 1, 18, 0, 0))
                        .timeTo(LocalDateTime.of(2022, Month.FEBRUARY, 1, 23, 0, 0))
                        .location(location)
                        .build())


        when:
        locationService.modifyAvailabilityAfterBooking(location, eventDate, dateTimeFrom, dateTimeTo)

        then:
        1 * locationAvailabilityRepository.delete(availability)
        1 * locationAvailabilityRepository.saveAndFlush(newAvailabilities.iterator().next())

    }
}
