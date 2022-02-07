package pjatk.pro.event_organizer_app.trait.address

import pjatk.pro.event_organizer_app.address.model.Address
import pjatk.pro.event_organizer_app.address.model.dto.AddressDto

trait AddressTrait {

    Address fakeAddress = Address.builder()
            .id(1L)
            .country('Poland')
            .city('Warsaw')
            .streetName('Piękna')
            .streetNumber(1)
            .zipCode('01-157')
            .build()

    Address fakeAddressWithId = Address.builder()
            .id(1)
            .country('Poland')
            .city('Warsaw')
            .streetName('Piękna')
            .streetNumber(1)
            .zipCode('01-157')
            .build()

    AddressDto fakeAddressDto = AddressDto.builder()
            .id(1L)
            .country('Poland')
            .city('Warsaw')
            .streetName('Piękna')
            .streetNumber(1)
            .zipCode('01-157')
            .build()

}