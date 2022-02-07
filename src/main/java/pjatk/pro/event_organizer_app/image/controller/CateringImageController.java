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
import pjatk.pro.event_organizer_app.image.model.CateringImage;
import pjatk.pro.event_organizer_app.image.model.dto.ImageDto;
import pjatk.pro.event_organizer_app.image.service.CateringImageService;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("api/images/catering")
public class CateringImageController {

    private final CateringImageService cateringImageService;

    @RequestMapping(
            method = RequestMethod.GET,
            path = "allowed/all",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<ImageDto>> list(@RequestParam long cateringId) {
        final List<CateringImage> cateringImageList = cateringImageService.findByCateringId(cateringId);
        final ImmutableList<ImageDto> resultList = cateringImageList.stream()
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
    public ResponseEntity<Void> upload(@RequestParam long cateringId, @RequestParam("file") MultipartFile file) {
        cateringImageService.upload(cateringId, file);
        return ResponseEntity.ok().build();
    }


    @PreAuthorize("hasAuthority('BUSINESS')")
    @RequestMapping(
            method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@RequestParam long id) {
        cateringImageService.deleteById(id);
        return ResponseEntity.ok().build();

    }

}
