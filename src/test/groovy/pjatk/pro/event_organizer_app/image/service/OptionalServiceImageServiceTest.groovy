package pjatk.pro.event_organizer_app.image.service

import org.springframework.mock.web.MockMultipartFile
import org.springframework.util.StringUtils
import pjatk.pro.event_organizer_app.image.model.OptionalServiceImage
import pjatk.pro.event_organizer_app.image.repository.OptionalServiceImageRepository
import pjatk.pro.event_organizer_app.optional_service.service.OptionalServiceService
import pjatk.pro.event_organizer_app.trait.optional_service.OptionalServiceTrait
import spock.lang.Specification
import spock.lang.Subject

class OptionalServiceImageServiceTest extends Specification implements OptionalServiceTrait {

    @Subject
    OptionalServiceImageService optionalServiceImageService

    OptionalServiceImageRepository optionalServiceImageRepository
    OptionalServiceService optionalServiceService

    def file = new MockMultipartFile("data", "filename.jpg", "image", "some xml".getBytes())
    def fileName = StringUtils.cleanPath(file.getOriginalFilename())

    def setup() {
        optionalServiceImageRepository = Mock()
        optionalServiceService = Mock()

        optionalServiceImageService = new OptionalServiceImageService(optionalServiceImageRepository,
                optionalServiceService)
    }

    def "Upload"() {
        given:
        def serviceId = 1l
        def service = fakeOptionalService
        def fileName = StringUtils.cleanPath(file.getOriginalFilename())

        def image = OptionalServiceImage.builder()
                .service(service)
                .fileName(fileName)
                .image(file.getBytes())
                .build();

        when:
        optionalServiceImageService.upload(serviceId, file)

        then:
        1 * optionalServiceService.getWithImages(serviceId) >> service
        1 * optionalServiceImageRepository.save(image)
    }

    def "FindByServiceId"() {
        given:
        def serviceId = 1l

        def images = List.of(OptionalServiceImage.builder()
                .fileName(fileName)
                .image(file.getBytes())
                .build())

        def target = images
        when:
        def result = optionalServiceImageService.findByServiceId(serviceId)

        then:
        1 * optionalServiceImageRepository.findAllByService_Id(serviceId) >> images

        result == target
    }

    def "DeleteById"() {
        given:
        def imageId = 1l
        def image = OptionalServiceImage.builder()
                .fileName(fileName)
                .image(file.getBytes())
                .build()

        when:
        optionalServiceImageService.deleteById(imageId)

        then:
        1 * optionalServiceImageRepository.findById(imageId) >> Optional.of(image)
        1 * optionalServiceImageRepository.delete(image)

    }
}
