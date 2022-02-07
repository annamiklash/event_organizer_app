package pjatk.pro.event_organizer_app.trait.location.locationforevent

import com.google.common.collect.ImmutableSet
import pjatk.pro.event_organizer_app.availability.location.model.LocationAvailability
import pjatk.pro.event_organizer_app.cateringforchosenevent.model.CateringForChosenEventLocation
import pjatk.pro.event_organizer_app.customer.model.Customer
import pjatk.pro.event_organizer_app.event.model.OrganizedEvent
import pjatk.pro.event_organizer_app.location.locationforevent.model.LocationForEvent
import pjatk.pro.event_organizer_app.location.locationforevent.model.dto.LocationForEventDto
import pjatk.pro.event_organizer_app.location.model.Location
import pjatk.pro.event_organizer_app.optional_service.optional_service_for_location.model.OptionalServiceForChosenLocation

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.Month

import static pjatk.pro.event_organizer_app.enums.ConfirmationStatusEnum.CONFIRMED

trait LocationForEventTrait {

    LocationForEventDto fakeLocationForEvent = LocationForEventDto.builder()
            .timeFrom("10:00:00")
            .timeTo("12:00:00")
            .guestCount(10)
            .build()

    LocationForEvent fakeLocationForEventDto = LocationForEvent.builder()
            .id(1L)
            .timeFrom(LocalTime.parse("10:00:00"))
            .timeTo(LocalTime.parse("12:00:00"))
            .guestCount(10)
            .confirmationStatus(CONFIRMED.name())
            .cateringsForEventLocation(new HashSet<CateringForChosenEventLocation>())
            .services(new HashSet<OptionalServiceForChosenLocation>())
            .build()

    LocationForEvent fakeFullLocationForEvent = LocationForEvent.builder()
            .id(1L)
            .timeFrom(LocalTime.parse("10:00:00"))
            .timeTo(LocalTime.parse("12:00:00"))
            .guestCount(10)
            .confirmationStatus(CONFIRMED.name())
            .location(Location.builder()
                    .id(1L)
                    .availability(Set.of(
                            LocationAvailability.builder()
                                    .id(1l)
                                    .date(LocalDate.of(2022, Month.FEBRUARY, 1))
                                    .timeFrom(LocalDateTime.of(2022, Month.FEBRUARY, 1, 9, 0, 0))
                                    .timeTo(LocalDateTime.of(2022, Month.FEBRUARY, 1, 23, 0, 0))
                                    .build()))
                    .build())
            .event(OrganizedEvent.builder()
                    .date(LocalDate.parse('2007-12-03'))
                    .startTime(LocalTime.parse("10:00:00"))
                    .endTime(LocalTime.parse("12:00:00"))
                    .guestCount(10)
                    .customer(Customer.builder().build())
                    .build())
            .build()

    LocationForEvent fakeFullLocationForEventWithService = LocationForEvent.builder()
            .id(1L)
            .timeFrom(LocalTime.parse("10:00:00"))
            .timeTo(LocalTime.parse("12:00:00"))
            .guestCount(10)
            .confirmationStatus(CONFIRMED.name())
            .location(Location.builder()
                    .id(1L)
                    .availability(Set.of(
                            LocationAvailability.builder()
                                    .id(1l)
                                    .date(LocalDate.of(2022, Month.FEBRUARY, 1))
                                    .timeFrom(LocalDateTime.of(2022, Month.FEBRUARY, 1, 9, 0, 0))
                                    .timeTo(LocalDateTime.of(2022, Month.FEBRUARY, 1, 23, 0, 0))
                                    .build()))
                    .build())
            .services(ImmutableSet.of(OptionalServiceForChosenLocation.builder()
                    .id(1L)
                    .confirmationStatus("CONFIRMED")
                    .build()))
            .event(OrganizedEvent.builder()
                    .date(LocalDate.parse('2007-12-03'))
                    .startTime(LocalTime.parse("10:00:00"))
                    .endTime(LocalTime.parse("12:00:00"))
                    .guestCount(10)
                    .customer(Customer.builder().build())
                    .build())
            .build()

    LocationForEvent fakeFullLocationForEventWithCatering = LocationForEvent.builder()
            .id(1L)
            .timeFrom(LocalTime.parse("10:00:00"))
            .timeTo(LocalTime.parse("12:00:00"))
            .guestCount(10)
            .confirmationStatus(CONFIRMED.name())
            .location(Location.builder()
                    .id(1L)
                    .availability(Set.of(
                            LocationAvailability.builder()
                                    .id(1l)
                                    .date(LocalDate.of(2022, Month.FEBRUARY, 1))
                                    .timeFrom(LocalDateTime.of(2022, Month.FEBRUARY, 1, 9, 0, 0))
                                    .timeTo(LocalDateTime.of(2022, Month.FEBRUARY, 1, 23, 0, 0))
                                    .build()))
                    .build())
            .cateringsForEventLocation(ImmutableSet.of(CateringForChosenEventLocation.builder()
                    .id(1L)
                    .confirmationStatus("CONFIRMED")
                    .build()))
            .event(OrganizedEvent.builder()
                    .date(LocalDate.parse('2007-12-03'))
                    .startTime(LocalTime.parse("10:00:00"))
                    .endTime(LocalTime.parse("12:00:00"))
                    .guestCount(10)
                    .customer(Customer.builder().build())
                    .build())
            .build()

    LocationForEventDto createLocationReservationDto = LocationForEventDto.builder()
            .timeFrom('13:00')
            .timeTo('18:00')
            .guestCount(10)
            .build()

    LocationForEvent fakeLocationForEventCreate = LocationForEvent.builder()
            .timeFrom(LocalTime.parse("13:00:00"))
            .timeTo(LocalTime.parse("18:00:00"))
            .guestCount(10)
            .confirmationStatus('NOT_CONFIRMED')
            .build()
}