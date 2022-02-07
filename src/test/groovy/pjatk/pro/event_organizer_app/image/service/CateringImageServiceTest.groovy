package pjatk.pro.event_organizer_app.image.service

import org.springframework.mock.web.MockMultipartFile
import org.springframework.util.StringUtils
import pjatk.pro.event_organizer_app.catering.service.CateringService
import pjatk.pro.event_organizer_app.image.model.CateringImage
import pjatk.pro.event_organizer_app.image.repository.CateringImageRepository
import pjatk.pro.event_organizer_app.trait.catering.CateringTrait
import spock.lang.Specification
import spock.lang.Subject

class CateringImageServiceTest extends Specification implements CateringTrait {

    @Subject
    CateringImageService cateringImageService

    CateringImageRepository cateringImageRepository
    CateringService cateringService

    def file = new MockMultipartFile("data", "filename.jpg", "image", "some xml".getBytes())
    def fileName = StringUtils.cleanPath(file.getOriginalFilename())


    def setup() {
        cateringImageRepository = Mock()
        cateringService = Mock()

        cateringImageService = new CateringImageService(cateringImageRepository, cateringService)
    }

    def "Upload"() {
        given:
        def cateringId = 1l
        def catering = fakeCatering
        def fileName = StringUtils.cleanPath(file.getOriginalFilename())

        def image = CateringImage.builder()
                .catering(catering)
                .fileName(fileName)
                .image(file.getBytes())
                .build();

        when:
        cateringImageService.upload(cateringId, file)

        then:
        1 * cateringService.getWithImages(cateringId) >> catering
        1 * cateringImageRepository.save(image)
    }

    def "FindByCateringId"() {
        given:
        def cateringId = 1l
        def images = List.of(CateringImage.builder()
                .fileName(fileName)
                .image(file.getBytes())
                .build())

        def target = images
        when:
        def result = cateringImageService.findByCateringId(cateringId)

        then:
        1 * cateringImageRepository.findAllByCatering_Id(cateringId) >> images

        result == target
    }

    def "DeleteById"() {
        given:
        def imageId = 1l
        def image = CateringImage.builder()
                .fileName(fileName)
                .image(file.getBytes())
                .build()

        when:
        cateringImageService.deleteById(imageId)

        then:
        1 * cateringImageRepository.findById(imageId) >> Optional.of(image)
        1 * cateringImageRepository.delete(image)

    }
}
