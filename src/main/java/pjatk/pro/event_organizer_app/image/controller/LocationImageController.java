package pjatk.pro.event_organizer_app.image.controller;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pjatk.pro.event_organizer_app.image.mapper.ImageMapper;
import pjatk.pro.event_organizer_app.image.model.LocationImage;
import pjatk.pro.event_organizer_app.image.model.dto.ImageDto;
import pjatk.pro.event_organizer_app.image.service.LocationImageService;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("api/images/location")
public class LocationImageController {

    private final LocationImageService locationImageService;

    @RequestMapping(
            method = RequestMethod.GET,
            path = "allowed/all",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<ImageDto>> list(@RequestParam long locationId) {
        final List<LocationImage> locationImageList = locationImageService.findByLocationId(locationId);
        final ImmutableList<ImageDto> resultList = locationImageList.stream()
                .map(ImageMapper::toDto)
                .collect(ImmutableList.toImmutableList());

        return ResponseEntity.ok(resultList);
    }

    @PreAuthorize("hasAuthority('BUSINESS')")
    @RequestMapping(
            path = "upload",
            method = RequestMethod.POST,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> upload(@RequestParam long locationId, @RequestParam("file") MultipartFile file) {
        locationImageService.upload(locationId, file);
        return ResponseEntity.ok().build();
    }


    @PreAuthorize("hasAuthority('BUSINESS')")
    @RequestMapping(
            method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete( @RequestParam long id) {
        locationImageService.deleteById( id);
        return ResponseEntity.ok().build();

    }

}
