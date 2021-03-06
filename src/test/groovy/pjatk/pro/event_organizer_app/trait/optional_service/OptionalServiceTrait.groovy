package pjatk.pro.event_organizer_app.trait.optional_service

import pjatk.pro.event_organizer_app.address.model.Address
import pjatk.pro.event_organizer_app.address.model.dto.AddressDto
import pjatk.pro.event_organizer_app.availability.optionalservice.model.OptionalServiceAvailability
import pjatk.pro.event_organizer_app.business.model.Business
import pjatk.pro.event_organizer_app.businesshours.dto.BusinessHoursDto
import pjatk.pro.event_organizer_app.businesshours.optionalservice.model.OptionalServiceBusinessHours
import pjatk.pro.event_organizer_app.image.model.OptionalServiceImage
import pjatk.pro.event_organizer_app.optional_service.model.OptionalService
import pjatk.pro.event_organizer_app.optional_service.model.dto.OptionalServiceDto
import pjatk.pro.event_organizer_app.optional_service.model.dto.TranslationLanguageDto
import pjatk.pro.event_organizer_app.optional_service.model.interpreter.Interpreter
import pjatk.pro.event_organizer_app.optional_service.model.other.Host

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month

trait OptionalServiceTrait {

    OptionalService fakeOptionalService = OptionalService.builder()
            .id(1)
            .type("HOST")
            .alias("ALIAS")
            .firstName("GERALT")
            .lastName("RIVIJSKI")
            .description("WIEDZMIN")
            .serviceCost(new BigDecimal("123"))
            .email('email@email.com')
            .optionalServiceBusinessHours(new HashSet<OptionalServiceBusinessHours>())
            .availability(new HashSet<OptionalServiceAvailability>())
            .business(Business.builder()
                    .id(1)
                    .firstName('Name')
                    .lastName('Name')
                    .businessName('Name')
                    .email('business@email.com')
                    .verificationStatus('VERIFIED')
                    .phoneNumber(new BigInteger("123123123"))
                    .build())
            .serviceAddress(Address.builder()
                    .id(1)
                    .country("Coutry")
                    .city("City")
                    .streetName("Street")
                    .streetNumber(1)
                    .zipCode("01-157")
                    .build())
            .images(Set.of(OptionalServiceImage.builder()
                    .id(1l)
                    .fileName("fileName")
                    .image("file.getBytes()".getBytes())
                    .build()))
            .build()

    Host fakeOptionalHost = Host.builder()
            .id(1)
            .type("HOST")
            .alias("ALIAS")
            .firstName("GERALT")
            .lastName("RIVIJSKI")
            .description("WIEDZMIN")
            .serviceCost(new BigDecimal("123"))
            .email('email@email.com')
            .optionalServiceBusinessHours(new HashSet<OptionalServiceBusinessHours>())
            .serviceAddress(Address.builder()
                    .id(1)
                    .country("Coutry")
                    .city("City")
                    .streetName("Street")
                    .streetNumber(1)
                    .zipCode("01-157")
                    .build())
            .build()

    OptionalService fakeOptionalHostWithAvailability = Host.builder()
            .id(1)
            .type("HOST")
            .alias("ALIAS")
            .firstName("GERALT")
            .lastName("RIVIJSKI")
            .description("WIEDZMIN")
            .serviceCost(new BigDecimal("123"))
            .email('email@email.com')
            .optionalServiceBusinessHours(new HashSet<OptionalServiceBusinessHours>())
            .availability(Set.of(OptionalServiceAvailability.builder()
                    .date(LocalDate.of(2022, Month.FEBRUARY, 1))
                    .timeFrom(LocalDateTime.of(2022, Month.FEBRUARY, 1, 9, 0, 0))
                    .timeTo(LocalDateTime.of(2022, Month.FEBRUARY, 1, 23, 0, 0))
                    .status('AVAILABLE')
                    .build()))
            .serviceAddress(Address.builder()
                    .id(1)
                    .country("Coutry")
                    .city("City")
                    .streetName("Street")
                    .streetNumber(1)
                    .zipCode("01-157")
                    .build())
            .business(Business.builder()
                    .id(1)
                    .firstName('Name')
                    .lastName('Name')
                    .businessName('Name')
                    .verificationStatus('VERIFIED')
                    .isActive(true)
                    .email('business@email.com')
                    .phoneNumber(new BigInteger("123123123"))
                    .build())
            .serviceAddress(Address.builder()
                    .id(1)
                    .country('Poland')
                    .city('Warsaw')
                    .streetName('Pi??kna')
                    .streetNumber(1)
                    .zipCode('01-157')
                    .build())
            .images(new HashSet<OptionalServiceImage>())
            .build()

    Interpreter fakeOptionalInterpreterWithAvailability = Interpreter.builder()
            .id(1)
            .type("INTERPRETER")
            .alias("ALIAS")
            .firstName("GERALT")
            .lastName("RIVIJSKI")
            .description("WIEDZMIN")
            .serviceCost(new BigDecimal("123"))
            .email('email@email.com')
            .optionalServiceBusinessHours(new HashSet<OptionalServiceBusinessHours>())
            .availability(Set.of(OptionalServiceAvailability.builder()
                    .date(LocalDate.of(2022, Month.FEBRUARY, 1))
                    .timeFrom(LocalDateTime.of(2022, Month.FEBRUARY, 1, 9, 0, 0))
                    .timeTo(LocalDateTime.of(2022, Month.FEBRUARY, 1, 23, 0, 0))
                    .status('AVAILABLE')
                    .build()))
            .serviceAddress(Address.builder()
                    .id(1)
                    .country("Coutry")
                    .city("City")
                    .streetName("Street")
                    .streetNumber(1)
                    .zipCode("01-157")
                    .build())
            .business(Business.builder()
                    .id(1)
                    .firstName('Name')
                    .lastName('Name')
                    .businessName('Name')
                    .verificationStatus('VERIFIED')
                    .isActive(true)
                    .phoneNumber(new BigInteger("123123123"))
                    .build())
            .serviceAddress(Address.builder()
                    .id(1)
                    .country('Poland')
                    .city('Warsaw')
                    .streetName('Pi??kna')
                    .streetNumber(1)
                    .zipCode('01-157')
                    .build())
            .build()

    OptionalServiceDto fakeOptionalServiceHostDto = OptionalServiceDto.builder()
            .id(1)
            .type("HOST")
            .alias("ALIAS")
            .firstName("GERALT")
            .lastName("RIVIJSKI")
            .description("WIEDZMIN")
            .serviceCost("123")
            .email('email@email.com')
            .businessHours(new ArrayList<BusinessHoursDto>())
            .address(AddressDto.builder()
                    .id(1)
                    .country("Coutry")
                    .city("City")
                    .streetName("Street")
                    .streetNumber(1)
                    .zipCode("01-157")
                    .build())
            .build()

    OptionalServiceDto fakeOptionalServiceInterpreterDto = OptionalServiceDto.builder()
            .id(1)
            .type("INTERPRETER")
            .alias("ALIAS")
            .firstName("GERALT")
            .lastName("RIVIJSKI")
            .description("WIEDZMIN")
            .serviceCost("123")
            .email('email@email.com')
            .businessHours(new ArrayList<BusinessHoursDto>())
            .address(AddressDto.builder().build())
            .translationLanguages(
                    Set.of(TranslationLanguageDto.builder()
                            .name('English')
                            .build()))
            .build()
}