package pjatk.pro.event_organizer_app.trait.image

import pjatk.pro.event_organizer_app.address.model.Address
import pjatk.pro.event_organizer_app.catering.model.Catering
import pjatk.pro.event_organizer_app.image.model.LocationImage
import pjatk.pro.event_organizer_app.location.model.Location

trait LocationImageTrait {

    LocationImage fakeLocationImage = LocationImage.builder()
            .id(1L)
            .image("SAMPLE IMAGE".getBytes())
            .fileName("SAMPLE FILE NAME")
            .location(Location.builder().id(1L)
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
            .build()

}