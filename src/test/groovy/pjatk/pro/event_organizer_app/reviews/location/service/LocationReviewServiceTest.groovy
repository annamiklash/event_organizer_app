package pjatk.pro.event_organizer_app.reviews.location.service

import com.google.common.collect.ImmutableList
import org.springframework.data.domain.PageImpl
import pjatk.pro.event_organizer_app.common.helper.TimestampHelper
import pjatk.pro.event_organizer_app.customer.repository.CustomerRepository
import pjatk.pro.event_organizer_app.location.repository.LocationRepository
import pjatk.pro.event_organizer_app.reviews.location.repository.LocationReviewRepository
import pjatk.pro.event_organizer_app.trait.customer.CustomerTrait
import pjatk.pro.event_organizer_app.trait.location.LocationTrait
import pjatk.pro.event_organizer_app.trait.page.PageTrait
import pjatk.pro.event_organizer_app.trait.reviews.ReviewTrait
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDateTime

class LocationReviewServiceTest extends Specification implements LocationTrait,
        CustomerTrait, ReviewTrait, PageTrait {

    @Subject
    LocationReviewService locationReviewService

    LocationReviewRepository locationReviewRepository
    CustomerRepository customerRepository
    LocationRepository locationRepository
    TimestampHelper timestampHelper

    LocalDateTime now = LocalDateTime.parse('2007-12-03T10:15:30')

    def setup() {
        locationReviewRepository = Mock()
        customerRepository = Mock()
        locationRepository = Mock()
        timestampHelper = Mock()

        timestampHelper.now() >> now

        locationReviewService = new LocationReviewService(locationReviewRepository,
                customerRepository,
                locationRepository,
                timestampHelper)
    }

    def "LeaveLocationReview"() {
        given:
        def customerId = 1l
        def locationId = 1l
        def reviewDto = fakeReviewDtoNoId
        def customer = fakeCustomer
        def location = fakeFullLocation

        def locationReview = fakeLocationReviewNoId
        locationReview.setLocation(location);
        locationReview.setCustomer(customer);
        locationReview.setCreatedAt(now);

        def target = locationReview

        when:
        def result = locationReviewService.leaveLocationReview(customerId, locationId, reviewDto)

        then:
        1 * customerRepository.findById(customerId) >> Optional.of(customer)
        1 * locationRepository.findById(locationId) >> Optional.of(location)
        1 * locationReviewRepository.save(locationReview)

        result == target

    }

    def "GetRating"() {
        given:
        def locationId = 1l
        def reviews = [fakeLocationReview]
        def target = fakeLocationReview.getStarRating()

        when:
        def result = locationReviewService.getRating(locationId)

        then:
        1 * locationReviewRepository.getByLocationId(locationId) >> reviews

        result == target
    }

    def "GetByLocationId with Paging"() {
        given:
        def locationId = 1l
        def customPage = fakePage
        def paging = fakePaging
        def page = new PageImpl<>([fakeLocationReview])

        def target = ImmutableList.of(fakeLocationReview)

        when:
        def result = locationReviewService.getByLocationId(customPage, locationId)

        then:
        1 * locationReviewRepository.existsLocationReviewByLocation_Id(locationId) >> true
        1 * locationReviewRepository.getByLocationId(locationId, paging) >> page

        result == target

    }

    def "GetByLocationId"() {
        given:
        def locationId = 1l
        def reviews = [fakeLocationReview]
        def target = reviews

        when:
        def result = locationReviewService.getByLocationId(locationId)

        then:
        1 * locationReviewRepository.existsLocationReviewByLocation_Id(locationId) >> true
        1 * locationReviewRepository.getByLocationId(locationId) >> reviews

        result == target
    }


    def "Count"() {
        given:
        def locationId = 1l
        def target = 1l

        when:
        def result = locationReviewService.count(locationId)

        then:
        1 *  locationReviewRepository.countLocationReviewsByLocation_Id(locationId) >> 1l

        result == target

    }

    def "Delete"() {
        given:
        def locationReview = fakeLocationReview

        when:
        locationReviewService.delete(locationReview)

        then:
        1 * locationReviewRepository.delete(locationReview)
    }
}
