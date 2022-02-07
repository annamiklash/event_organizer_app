package pjatk.pro.event_organizer_app.trait.reviews.location

import pjatk.pro.event_organizer_app.address.model.Address
import pjatk.pro.event_organizer_app.customer.avatar.model.CustomerAvatar
import pjatk.pro.event_organizer_app.customer.model.Customer
import pjatk.pro.event_organizer_app.location.model.Location
import pjatk.pro.event_organizer_app.reviews.location.model.LocationReview

import java.time.LocalDate
import java.time.LocalDateTime

trait LocationReviewTrait {

    LocationReview fakeLocationReview = LocationReview.builder()
            .id(1L)
            .title("SAMPLE TITLE")
            .comment("SAMPLE COMMENT")
            .createdAt(LocalDateTime.parse('2007-12-03T10:15:30'))
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
            .location(Location.builder()
                    .id(1)
                    .name('Name')
                    .email('email@email.com')
                    .locationAddress(Address.builder()
                            .id(1)
                            .country('Poland')
                            .city('Warsaw')
                            .streetName('Piękna')
                            .streetNumber(1)
                            .zipCode('01-157')
                            .build())
                    .build())
            .build()

}