package pjatk.pro.event_organizer_app.trait.availability.optionalservice

import pjatk.pro.event_organizer_app.availability.optionalservice.model.OptionalServiceAvailability
import pjatk.pro.event_organizer_app.optional_service.model.OptionalService

import java.time.LocalDate
import java.time.LocalDateTime

import static pjatk.pro.event_organizer_app.availability.AvailabilityEnum.AVAILABLE

trait OptionalServiceAvailabilityTrait {

    OptionalServiceAvailability fakeOptionalServiceAvailability = OptionalServiceAvailability.builder()
            .optionalService(
                    OptionalService.builder()
                            .id(1)
                            .type("HOST")
                            .alias("ALIAS")
                            .firstName("GERALT")
                            .lastName("RIVIJSKI")
                            .description("WIEDZMIN")
                            .serviceCost(new BigDecimal("123"))
                            .email("Test@test.com")
                            .build()
            )
            .date(LocalDate.parse('2007-12-03'))
            .timeFrom(LocalDateTime.parse('2007-12-03T10:15:30'))
            .timeTo(LocalDateTime.parse('2007-12-03T10:15:30'))
            .status(AVAILABLE.name())
            .build()
}