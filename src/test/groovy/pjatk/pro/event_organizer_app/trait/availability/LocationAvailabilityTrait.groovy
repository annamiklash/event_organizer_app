package pjatk.pro.event_organizer_app.trait.availability

import pjatk.pro.event_organizer_app.availability.AvailabilityEnum
import pjatk.pro.event_organizer_app.availability.location.model.LocationAvailability
import pjatk.pro.event_organizer_app.location.model.Location
import spock.lang.Shared

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month

trait LocationAvailabilityTrait {

    @Shared
    LocationAvailability fakeLocationAvailability = LocationAvailability.builder()
            .id(1)
            .date(LocalDate.of(2021, Month.DECEMBER, 31))
            .timeFrom(LocalDateTime.of(2021, Month.DECEMBER, 31, 10, 0, 0))
            .timeTo(LocalDateTime.of(2021, Month.DECEMBER, 31, 23, 0, 0))
            .status(AvailabilityEnum.AVAILABLE.name())
            .location(buildLocation())
            .build()

    def buildLocation() {
        return Location.builder()
                .id(1)
                .name('Name')
                .build();
    }
}
