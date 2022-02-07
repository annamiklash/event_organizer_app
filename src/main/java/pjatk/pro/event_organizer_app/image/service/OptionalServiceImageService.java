package pjatk.pro.event_organizer_app.image.service;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import pjatk.pro.event_organizer_app.customer.avatar.validator.ImageValidator;
import pjatk.pro.event_organizer_app.exceptions.ActionNotAllowedException;
import pjatk.pro.event_organizer_app.exceptions.IllegalArgumentException;
import pjatk.pro.event_organizer_app.exceptions.NotFoundException;
import pjatk.pro.event_organizer_app.image.model.OptionalServiceImage;
import pjatk.pro.event_organizer_app.image.repository.OptionalServiceImageRepository;
import pjatk.pro.event_organizer_app.optional_service.model.OptionalService;
import pjatk.pro.event_organizer_app.optional_service.service.OptionalServiceService;

import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class OptionalServiceImageService {

    private static final int MAX_IMAGE_COUNT = 8;

    private final OptionalServiceImageRepository optionalServiceImageRepository;

    private final OptionalServiceService optionalServiceService;

    @SneakyThrows(IOException.class)
    public void upload(long serviceId, MultipartFile file) {
        if (file.getOriginalFilename() == null) {
            throw new ActionNotAllowedException("Cannot upload from empty path");
        }
        final OptionalService service = optionalServiceService.getWithImages(serviceId);

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        ImageValidator.validateFileExtension(fileName);

        if (service.getImages().size() >= MAX_IMAGE_COUNT) {
            throw new IllegalArgumentException("Can only have no more than " + MAX_IMAGE_COUNT + " images");
        }

        final OptionalServiceImage locationImage = OptionalServiceImage.builder()
                .service(service)
                .fileName(fileName)
                .image(file.getBytes())
                .build();

        optionalServiceImageRepository.save(locationImage);
    }

    public List<OptionalServiceImage> findByServiceId(long serviceId) {
        return optionalServiceImageRepository.findAllByService_Id(serviceId);
    }

    public void deleteById( Long imageId) {
        final OptionalServiceImage imageToDelete = optionalServiceImageRepository.findById(imageId)
                .orElseThrow(() -> new NotFoundException("Service image with id " + imageId + " does not exist"));
        optionalServiceImageRepository.delete(imageToDelete);
    }

}
