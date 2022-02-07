package pjatk.pro.event_organizer_app.trait.availability

import pjatk.pro.event_organizer_app.availability.dto.AvailabilityDto
import pjatk.pro.event_organizer_app.availability.location.model.LocationAvailability
import pjatk.pro.event_organizer_app.availability.optionalservice.model.OptionalServiceAvailability

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month

trait AvailabilityTrait {

    AvailabilityDto fakeAvailabilityDto = AvailabilityDto.builder()
            .date('2021-12-31')
            .timeFrom('10:00:00')
            .timeTo('23:00:00')
            .build()

    OptionalServiceAvailability fakeServiceAvailability = OptionalServiceAvailability.builder()
            .date(LocalDate.of(2022, Month.JANUARY, 1))
            .timeFrom(LocalDateTime.of(2022, Month.JANUARY, 1, 10, 0, 0))
            .timeTo(LocalDateTime.of(2022, Month.JANUARY, 1, 20, 0, 0))
            .build()

    OptionalServiceAvailability fakeServiceAvailabilityWithId = OptionalServiceAvailability.builder()
            .id(1l)
            .date(LocalDate.of(2022, Month.JANUARY, 1))
            .timeFrom(LocalDateTime.of(2022, Month.JANUARY, 1, 10, 0, 0))
            .timeTo(LocalDateTime.of(2022, Month.JANUARY, 1, 20, 0, 0))
            .build()

    LocationAvailability fakeLocationAvailabilityWithId = LocationAvailability.builder()
            .id(1l)
            .date(LocalDate.of(2022, Month.JANUARY, 1))
            .timeFrom(LocalDateTime.of(2022, Month.JANUARY, 1, 10, 0, 0))
            .timeTo(LocalDateTime.of(2022, Month.JANUARY, 1, 20, 0, 0))
            .build()

}