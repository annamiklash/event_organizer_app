package pjatk.pro.event_organizer_app.trait.customer


import pjatk.pro.event_organizer_app.customer.avatar.model.CustomerAvatar
import pjatk.pro.event_organizer_app.customer.model.Customer
import pjatk.pro.event_organizer_app.customer.model.dto.CustomerDto
import pjatk.pro.event_organizer_app.image.model.dto.ImageDto
import pjatk.pro.event_organizer_app.user.model.dto.UserDto

import java.time.LocalDate

trait CustomerTrait {

    Customer fakeCustomer = Customer.builder()
            .id(1L)
            .firstName('Geralt')
            .lastName('Rivijski')
            .type('C' as char)
            .birthdate(LocalDate.parse('2007-12-03'))
            .phoneNumber(new BigInteger("123123123"))
            .email('email@email.com')
            .guests(new HashSet<>())
            .events(new HashSet<>())
            .appProblems(new HashSet<>())
            .avatar(CustomerAvatar.builder().id(1L).image("image".getBytes()).fileName("name").build())
            .build()

    CustomerDto fakeCustomerDTO = CustomerDto.builder()
            .firstName('Geralt')
            .lastName('Rivijski')
            .birthdate('2007-12-03')
            .phoneNumber("123123123")
            .guests(new HashSet<>())
            .events(new HashSet<>())
            .user(UserDto.builder()
                    .id(1)
                    .type('C' as char)
                    .email('email@email.com')
                    .build())
            .avatar(ImageDto.builder().id(1L).build())
            .build()


}