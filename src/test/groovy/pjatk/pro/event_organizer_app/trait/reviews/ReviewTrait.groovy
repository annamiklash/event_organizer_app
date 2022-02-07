package pjatk.pro.event_organizer_app.trait.reviews

import pjatk.pro.event_organizer_app.customer.avatar.model.CustomerAvatar
import pjatk.pro.event_organizer_app.customer.model.Customer
import pjatk.pro.event_organizer_app.reviews.ReviewDto
import pjatk.pro.event_organizer_app.reviews.catering.model.CateringReview
import pjatk.pro.event_organizer_app.reviews.location.model.LocationReview
import pjatk.pro.event_organizer_app.reviews.service.model.OptionalServiceReview

import java.time.LocalDate

trait ReviewTrait {

    ReviewDto fakeReviewDto = ReviewDto.builder()
            .id(1L)
            .title('SAMPLE TITLE')
            .comment('SAMPLE COMMENT')
            .starRating(5)
            .build()

    ReviewDto fakeReviewDtoNoId = ReviewDto.builder()
            .title('SAMPLE TITLE')
            .comment('SAMPLE COMMENT')
            .starRating(5)
            .build()

    LocationReview fakeLocationReview = LocationReview.builder()
            .id(1L)
            .title('SAMPLE TITLE')
            .comment('SAMPLE COMMENT')
            .starRating(5)
            .build()

    LocationReview fakeLocationReviewNoId = LocationReview.builder()
            .title('SAMPLE TITLE')
            .comment('SAMPLE COMMENT')
            .starRating(5)
            .build()

    LocationReview fakeLocationReviewWithCustomer = LocationReview.builder()
            .id(1L)
            .title('SAMPLE TITLE')
            .comment('SAMPLE COMMENT')
            .starRating(5)
            .customer(Customer.builder()
                    .id(1L)
                    .firstName('Geralt')
                    .lastName('Rivijski')
                    .birthdate(LocalDate.parse('2007-12-03'))
                    .phoneNumber(new BigInteger("123123123"))
                    .email('email@email.com')
                    .guests(new HashSet<>())
                    .events(new HashSet<>())
                    .appProblems(new HashSet<>())
                    .avatar(CustomerAvatar.builder().id(1L).image("image".getBytes()).fileName("name").build())
                    .build())
            .build()

    CateringReview fakeCateringReview = CateringReview.builder()
            .id(1L)
            .title('SAMPLE TITLE')
            .comment('SAMPLE COMMENT')
            .starRating(5)
            .build()

    CateringReview fakeCateringReviewNoId = CateringReview.builder()
            .title('SAMPLE TITLE')
            .comment('SAMPLE COMMENT')
            .starRating(5)
            .build()

    CateringReview fakeCateringReviewWithCustomer = CateringReview.builder()
            .id(1L)
            .title('SAMPLE TITLE')
            .comment('SAMPLE COMMENT')
            .starRating(5)
            .customer(Customer.builder()
                    .id(1L)
                    .firstName('Geralt')
                    .lastName('Rivijski')
                    .birthdate(LocalDate.parse('2007-12-03'))
                    .phoneNumber(new BigInteger("123123123"))
                    .email('email@email.com')
                    .guests(new HashSet<>())
                    .events(new HashSet<>())
                    .appProblems(new HashSet<>())
                    .avatar(CustomerAvatar.builder().id(1L).image("image".getBytes()).fileName("name").build())
                    .build())
            .build()

    OptionalServiceReview fakeServiceReview = OptionalServiceReview.builder()
            .id(1L)
            .title('SAMPLE TITLE')
            .comment('SAMPLE COMMENT')
            .starRating(5)
            .build()

    OptionalServiceReview fakeServiceReviewNoId = OptionalServiceReview.builder()
            .title('SAMPLE TITLE')
            .comment('SAMPLE COMMENT')
            .starRating(5)
            .build()

    OptionalServiceReview fakeServiceReviewWithCustomer = OptionalServiceReview.builder()
            .id(1L)
            .title('SAMPLE TITLE')
            .comment('SAMPLE COMMENT')
            .starRating(5)
            .customer(Customer.builder()
                    .id(1L)
                    .firstName('Geralt')
                    .lastName('Rivijski')
                    .birthdate(LocalDate.parse('2007-12-03'))
                    .phoneNumber(new BigInteger("123123123"))
                    .email('email@email.com')
                    .guests(new HashSet<>())
                    .events(new HashSet<>())
                    .appProblems(new HashSet<>())
                    .avatar(CustomerAvatar.builder().id(1L).image("image".getBytes()).fileName("name").build())
                    .build())
            .build()
}