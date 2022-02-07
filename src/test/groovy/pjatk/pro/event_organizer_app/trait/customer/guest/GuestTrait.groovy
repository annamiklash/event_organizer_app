package pjatk.pro.event_organizer_app.trait.customer.guest

import com.google.common.collect.ImmutableSet
import pjatk.pro.event_organizer_app.customer.guest.model.Guest
import pjatk.pro.event_organizer_app.customer.guest.model.dto.GuestDto
import pjatk.pro.event_organizer_app.customer.model.Customer
import pjatk.pro.event_organizer_app.customer.model.dto.CustomerDto
import pjatk.pro.event_organizer_app.user.model.dto.UserDto

import java.time.LocalDate
import java.time.LocalDateTime

trait GuestTrait {

    Guest fakeGuest = Guest.builder()
            .id(1l)
            .firstName("Geralt")
            .lastName("Rivijski")
            .email('email@email.com')
            .createdAt(LocalDateTime.parse('2007-12-03T10:15:30'))
            .modifiedAt(LocalDateTime.parse('2007-12-03T10:15:30'))
            .organizedEvents(ImmutableSet.of())
            .customer(Customer.builder()
                    .id(1L)
                    .birthdate(LocalDate.parse('2007-12-03'))
                    .firstName("Geralt")
                    .lastName("Rivijski")
                    .build())
            .build()

    GuestDto fakeGuestDTO = GuestDto.builder()
            .id(1l)
            .firstName("Geralt")
            .lastName("Rivijski")
            .email('email@email.com')
            .createdAt('2007-12-03T10:15:30')
            .modifiedAt('2007-12-03T10:15:30')
            .customer(CustomerDto.builder()
                    .phoneNumber('123123123')
                    .firstName("Geralt")
                    .lastName("Rivijski")
                    .user(UserDto.builder()
                            .id(1)
                            .type('C' as char)
                            .email("test@email.com")
                            .build()
                    )
                    .build())
            .build()

    GuestDto fakeGuestWithoutId = GuestDto.builder()
            .firstName("Geralt")
            .lastName("Rivijski")
            .email('email@email.com')
            .build()

}