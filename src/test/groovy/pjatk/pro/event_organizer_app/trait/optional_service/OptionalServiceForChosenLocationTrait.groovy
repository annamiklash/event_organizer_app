package pjatk.pro.event_organizer_app.trait.optional_service

import com.google.common.collect.ImmutableList
import com.google.common.collect.ImmutableSet
import com.google.common.collect.Sets
import pjatk.pro.event_organizer_app.address.model.Address
import pjatk.pro.event_organizer_app.address.model.dto.AddressDto
import pjatk.pro.event_organizer_app.businesshours.dto.BusinessHoursDto
import pjatk.pro.event_organizer_app.catering.model.Catering
import pjatk.pro.event_organizer_app.cateringforchosenevent.model.CateringForChosenEventLocation
import pjatk.pro.event_organizer_app.cateringforchosenevent.model.dto.CateringForChosenEventLocationDto
import pjatk.pro.event_organizer_app.cuisine.model.Cuisine
import pjatk.pro.event_organizer_app.customer.model.dto.CustomerDto
import pjatk.pro.event_organizer_app.event.model.OrganizedEvent
import pjatk.pro.event_organizer_app.event.model.dto.OrganizedEventDto
import pjatk.pro.event_organizer_app.location.locationforevent.model.LocationForEvent
import pjatk.pro.event_organizer_app.location.locationforevent.model.dto.LocationForEventDto
import pjatk.pro.event_organizer_app.location.model.Location
import pjatk.pro.event_organizer_app.location.model.dto.LocationDto
import pjatk.pro.event_organizer_app.optional_service.model.OptionalService
import pjatk.pro.event_organizer_app.optional_service.model.dto.OptionalServiceDto
import pjatk.pro.event_organizer_app.optional_service.optional_service_for_location.model.OptionalServiceForChosenLocation
import pjatk.pro.event_organizer_app.optional_service.optional_service_for_location.model.dto.OptionalServiceForChosenLocationDto
import pjatk.pro.event_organizer_app.user.model.dto.UserDto

import java.time.LocalDate
import java.time.LocalTime

import static pjatk.pro.event_organizer_app.enums.ConfirmationStatusEnum.NOT_CONFIRMED
import static pjatk.pro.event_organizer_app.enums.EventStatusEnum.IN_PROGRESS

trait OptionalServiceForChosenLocationTrait {

    OptionalServiceForChosenLocation fakeOptionalServiceForChosenLocation = OptionalServiceForChosenLocation.builder()
            .id(1L)
            .timeFrom(LocalTime.parse("10:00:00"))
            .timeTo(LocalTime.parse("12:00:00"))
            .comment("SAMPLE COMMENT")
            .confirmationStatus("CONFIRMED")
            .locationForEvent(LocationForEvent.builder()
                    .id(1L)
                    .confirmationStatus("CONFIRMED")
                    .timeFrom(LocalTime.parse("10:00:00"))
                    .timeTo(LocalTime.parse("12:00:00"))
                    .event(OrganizedEvent.builder()
                            .date(LocalDate.parse('2007-12-03'))
                            .startTime(LocalTime.parse("10:00:00"))
                            .endTime(LocalTime.parse("12:00:00"))
                            .guestCount(10)
                            .build())
                    .location(Location.builder().id(2L)
                            .caterings(new HashSet<Catering>())
                            .locationAddress(Address.builder()
                                    .id(1)
                                    .country('Poland')
                                    .city('Warsaw')
                                    .streetName('Piękna')
                                    .streetNumber(1)
                                    .zipCode('01-157')
                                    .build())
                            .build())
                    .services(ImmutableSet.of(OptionalServiceForChosenLocation.builder()
                            .id(1L)
                            .confirmationStatus("CONFIRMED")
                            .build()))
                    .cateringsForEventLocation(ImmutableSet.of(CateringForChosenEventLocation.builder()
                            .id(1L)
                            .time(LocalTime.parse('10:15'))
                            .comment("SAMPLE COMMENT")
                            .confirmationStatus(NOT_CONFIRMED.name())
                            .catering(Catering.builder()
                                    .id(1L)
                                    .name('Name')
                                    .email('email@email.com')
                                    .phoneNumber(new BigInteger('123456789'))
                                    .description('description')
                                    .cateringAddress(Address.builder()
                                            .id(1L)
                                            .country('Poland')
                                            .city('Warsaw')
                                            .streetName('Piękna')
                                            .streetNumber(1)
                                            .zipCode('01-157')
                                            .build())
                                    .cuisines(Sets.newHashSet(Cuisine.builder()
                                            .id(1)
                                            .name('Greek')
                                            .build()))
                                    .build())
                            .build()))
                    .build())
            .optionalService(OptionalService.builder()
                    .id(1)
                    .type("HOST")
                    .alias("ALIAS")
                    .firstName("GERALT")
                    .lastName("RIVIJSKI")
                    .description("WIEDZMIN")
                    .serviceCost(new BigDecimal("123"))
                    .email("Test@test.com")
                    .build())
            .build()

    OptionalServiceForChosenLocation fakeOptionalServiceForChosenLocationSimpleNoId = OptionalServiceForChosenLocation.builder()
            .timeFrom(LocalTime.parse("13:00:00"))
            .timeTo(LocalTime.parse("18:00:00"))
            .comment("SAMPLE COMMENT")
            .confirmationStatus("NOT_CONFIRMED")
            .build()

    OptionalServiceForChosenLocationDto fakeOptionalServiceForChosenLocationDtoBasic = OptionalServiceForChosenLocationDto.builder()
            .timeFrom('13:00')
            .timeTo('18:00')
            .comment('SAMPLE COMMENT')
            .build()

    OptionalServiceForChosenLocationDto fakeOptionalServiceForChosenLocationDto = OptionalServiceForChosenLocationDto.builder()
            .timeFrom('10:00')
            .timeTo('12:00')
            .comment('SAMPLE COMMENT')
            .confirmationStatus('CONFIRMED')
            .optionalService(OptionalServiceDto.builder()
                    .id(1)
                    .type("HOST")
                    .alias("ALIAS")
                    .firstName("GERALT")
                    .lastName("RIVIJSKI")
                    .description("WIEDZMIN")
                    .serviceCost("123")
                    .email('email@email.com')
                    .businessHours(new ArrayList<BusinessHoursDto>())
                    .address(AddressDto.builder().build())
                    .build())
            .locationForEvent(LocationForEventDto.builder()
                    .id(1L)
                    .timeFrom('10:00')
                    .timeTo('12:00')
                    .guestCount(1)
                    .date('2012-01-01')
                    .confirmationStatus('CONFIRMED')
                    .caterings(new ArrayList<CateringForChosenEventLocationDto>())
                    .optionalServices(new ArrayList<OptionalServiceForChosenLocationDto>())
                    .location(LocationDto.builder()
                            .id(1L)
                            .name("SAMPLE LOCATION NAME")
                            .rating(12.10D)
                            .email("test@email.com")
                            .phoneNumber('123123123')
                            .seatingCapacity(10)
                            .standingCapacity(20)
                            .description("SAMPLE DESCRIPTION")
                            .dailyRentCost("123")
                            .sizeInSqMeters(100)
                            .descriptions(ImmutableSet.of())
                            .address(AddressDto.builder()
                                    .id(1L)
                                    .country('Poland')
                                    .city('Warsaw')
                                    .streetName('Piękna')
                                    .streetNumber(1)
                                    .zipCode('01-157')
                                    .build())
                            .businessHours(ImmutableList.of())
                            .build())
                    .event(OrganizedEventDto.builder()
                            .id(1)
                            .name("SAMPLE NAME")
                            .date('2007-12-03')
                            .startTime("10:00")
                            .endTime("12:00")
                            .guestCount(10)
                            .eventStatus(IN_PROGRESS.name())
                            .eventType("Party")
                            .customer(CustomerDto.builder()
                                    .firstName('Geralt')
                                    .lastName('Rivijski')
                                    .birthdate('2007-12-03')
                                    .phoneNumber("123123123")
                                    .user(UserDto.builder()
                                            .id(1)
                                            .type('C' as char)
                                            .email('email@email.com')
                                            .build())
                                    .build())
                            .build())
                    .build())
            .build();

}