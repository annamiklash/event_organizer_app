package pjatk.pro.event_organizer_app.trait.image

import pjatk.pro.event_organizer_app.image.model.OptionalServiceImage
import pjatk.pro.event_organizer_app.optional_service.model.OptionalService

trait OptionalServiceImageTrait {

    OptionalServiceImage fakeOptionalServiceImage = OptionalServiceImage.builder()
            .id(1L)
            .image("SAMPLE IMAGE".getBytes())
            .fileName("SAMPLE FILE NAME")
            .service(OptionalService.builder()
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
}