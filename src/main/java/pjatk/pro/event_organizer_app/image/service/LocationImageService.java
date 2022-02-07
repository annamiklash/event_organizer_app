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
import pjatk.pro.event_organizer_app.image.model.LocationImage;
import pjatk.pro.event_organizer_app.image.repository.LocationImageRepository;
import pjatk.pro.event_organizer_app.location.model.Location;
import pjatk.pro.event_organizer_app.location.service.LocationService;

import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class LocationImageService {

    private static final int MAX_IMAGE_COUNT = 8;

    private final LocationImageRepository locationImageRepository;

    private final LocationService locationService;


    public List<LocationImage> findByLocationId(long locationId) {
        return locationImageRepository.findAllByLocation_Id(locationId);
    }


    public void deleteById(long imageId) {
        final LocationImage imageToDelete = locationImageRepository.findById(imageId)
                .orElseThrow(() -> new NotFoundException("Location image with id " + imageId + " does not exits"));

        locationImageRepository.delete(imageToDelete);
    }

    @SneakyThrows(IOException.class)
    public void upload(long locationId, MultipartFile file) {
        if (file.getOriginalFilename() == null) {
            throw new ActionNotAllowedException("Cannot upload from empty path");
        }
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        ImageValidator.validateFileExtension(fileName);

        final Location location = locationService.getWithImages(locationId);
        if (location.getImages().size() >= MAX_IMAGE_COUNT) {
            throw new IllegalArgumentException("Can only have no more than " + MAX_IMAGE_COUNT + " images");
        }

        final LocationImage locationImage = LocationImage.builder()
                .location(location)
                .fileName(fileName)
                .image(file.getBytes())
                .build();

        locationImageRepository.save(locationImage);
    }

}
