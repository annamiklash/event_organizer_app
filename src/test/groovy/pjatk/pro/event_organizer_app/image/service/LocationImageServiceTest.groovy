package pjatk.pro.event_organizer_app.image.service

import org.springframework.mock.web.MockMultipartFile
import org.springframework.util.StringUtils
import pjatk.pro.event_organizer_app.image.model.LocationImage
import pjatk.pro.event_organizer_app.image.repository.LocationImageRepository
import pjatk.pro.event_organizer_app.location.service.LocationService
import pjatk.pro.event_organizer_app.trait.location.LocationTrait
import spock.lang.Specification
import spock.lang.Subject

class LocationImageServiceTest extends Specification
        implements LocationTrait {

    @Subject
    LocationImageService locationImageService

    LocationImageRepository locationImageRepository
    LocationService locationService

    def file = new MockMultipartFile("data", "filename.jpg", "image", "some xml".getBytes())
    def fileName = StringUtils.cleanPath(file.getOriginalFilename())

    def setup() {
        locationImageRepository = Mock()
        locationService = Mock()

        locationImageService = new LocationImageService(locationImageRepository, locationService)
    }

    def "Upload"() {
        given:
        def locationId = 1l
        def location = fakeFullLocation
        def fileName = StringUtils.cleanPath(file.getOriginalFilename())

        def image = LocationImage.builder()
                .location(location)
                .fileName(fileName)
                .image(file.getBytes())
                .build();

        when:
        locationImageService.upload(locationId, file)

        then:
        1 * locationService.getWithImages(locationId) >> location
        1 * locationImageRepository.save(image)
    }

    def "FindByLocationId"() {
        given:
        def locationId = 1l

        def images = List.of(LocationImage.builder()
                .fileName(fileName)
                .image(file.getBytes())
                .build())

        def target = images
        when:
        def result = locationImageService.findByLocationId(locationId)

        then:
        1 * locationImageRepository.findAllByLocation_Id(locationId) >> images

        result == target
    }

    def "DeleteById"() {
        given:
        def imageId = 1l
        def image = LocationImage.builder()
                .fileName(fileName)
                .image(file.getBytes())
                .build()

        when:
        locationImageService.deleteById(imageId)

        then:
        1 * locationImageRepository.findById(imageId) >> Optional.of(image)
        1 * locationImageRepository.delete(image)

    }
}
