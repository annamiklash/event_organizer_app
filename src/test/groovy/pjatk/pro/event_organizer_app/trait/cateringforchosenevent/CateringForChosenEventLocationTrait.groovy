package pjatk.pro.event_organizer_app.trait.cateringforchosenevent

import com.google.common.collect.ImmutableSet
import com.google.common.collect.Sets
import pjatk.pro.event_organizer_app.address.model.Address
import pjatk.pro.event_organizer_app.availability.location.model.LocationAvailability
import pjatk.pro.event_organizer_app.businesshours.location.model.LocationBusinessHours
import pjatk.pro.event_organizer_app.catering.model.Catering
import pjatk.pro.event_organizer_app.catering.model.CateringItem
import pjatk.pro.event_organizer_app.cateringforchosenevent.model.CateringForChosenEventLocation
import pjatk.pro.event_organizer_app.cateringforchosenevent.model.CateringOrderChoice
import pjatk.pro.event_organizer_app.cateringforchosenevent.model.dto.CateringForChosenEventLocationDto
import pjatk.pro.event_organizer_app.cuisine.model.Cuisine
import pjatk.pro.event_organizer_app.event.model.EventType
import pjatk.pro.event_organizer_app.event.model.OrganizedEvent
import pjatk.pro.event_organizer_app.location.locationforevent.model.LocationForEvent
import pjatk.pro.event_organizer_app.location.model.Location
import pjatk.pro.event_organizer_app.location.model.LocationDescriptionItem
import pjatk.pro.event_organizer_app.optional_service.optional_service_for_location.model.OptionalServiceForChosenLocation

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.Month

import static pjatk.pro.event_organizer_app.enums.ConfirmationStatusEnum.CONFIRMED
import static pjatk.pro.event_organizer_app.enums.ConfirmationStatusEnum.NOT_CONFIRMED
import static pjatk.pro.event_organizer_app.enums.EventStatusEnum.IN_PROGRESS

trait CateringForChosenEventLocationTrait {

    CateringForChosenEventLocation fakeCateringForChosenEventLocation = CateringForChosenEventLocation.builder()
            .time(LocalTime.parse('10:15'))
            .comment("SAMPLE COMMENT")
            .confirmationStatus(NOT_CONFIRMED.name())
            .isCateringOrderConfirmed(true)
            .eventLocation(LocationForEvent.builder()
                    .id(1L)
                    .event(OrganizedEvent.builder()
                            .date(LocalDate.parse('2022-12-03'))
                            .build())
                    .build())
            .build()


    CateringForChosenEventLocation fakeFullCateringForChosenEventLocation = CateringForChosenEventLocation.builder()
            .id(1L)
            .time(LocalTime.parse('10:15'))
            .comment("SAMPLE COMMENT")
            .confirmationStatus(NOT_CONFIRMED.name())
            .isCateringOrderConfirmed(true)
            .cateringOrder(Set.of(
                    CateringOrderChoice.builder()
                            .id(1l)
                            .amount(10)
                            .item(CateringItem.builder()
                                    .id(1l)
                                    .name('Name')
                                    .build())
                            .build()))
            .eventLocation(LocationForEvent.builder()
                    .id(1L)
                    .timeFrom(LocalTime.parse("10:00:00"))
                    .timeTo(LocalTime.parse("12:00:00"))
                    .guestCount(10)
                    .confirmationStatus('CONFIRMED')
                    .location(Location.builder()
                            .id(1L)
                            .name('Name')
                            .description('Description')
                            .email('email@email')
                            .phoneNumber(new BigInteger(123456789))
                            .seatingCapacity(30)
                            .standingCapacity(100)
                            .dailyRentCost(new BigDecimal(100.0))
                            .sizeInSqMeters(300)
                            .createdAt(LocalDateTime.of(2020, Month.FEBRUARY, 1, 9, 0, 0))
                            .modifiedAt(LocalDateTime.of(2020, Month.FEBRUARY, 1, 9, 0, 0))
                            .deletedAt(null)
                            .caterings(new HashSet<Catering>())
                            .descriptions(new HashSet<LocationDescriptionItem>())
                            .availability(new HashSet<LocationAvailability>())
                            .locationBusinessHours(new HashSet<LocationBusinessHours>())
                            .locationAddress(Address.builder()
                                    .id(1)
                                    .country('Poland')
                                    .city('Warsaw')
                                    .streetName('Piękna')
                                    .streetNumber(1)
                                    .zipCode('01-157')
                                    .build())
                            .build())
                    .event(OrganizedEvent.builder()
                            .id(1L)
                            .name("SAMPLE NAME")
                            .date(LocalDate.parse('2007-12-03'))
                            .startTime(LocalTime.parse("10:00:00"))
                            .endTime(LocalTime.parse("12:00:00"))
                            .guestCount(10)
                            .eventStatus(IN_PROGRESS.name())
                            .eventType(EventType.builder()
                                    .id(1L)
                                    .type("Party")
                                    .build())
                            .locationForEvent(Set.of(LocationForEvent.builder()
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
                                    .build()))
                            .build())
                    .build())
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

            .build()

    CateringForChosenEventLocationDto fakeCateringForChosenEventLocationDto = CateringForChosenEventLocationDto.builder()
            .id(1L)
            .time('10:15')
            .comment("SAMPLE COMMENT")
            .isOrderConfirmed(false)
            .confirmationStatus(CONFIRMED.name())
            .build()
}