package pjatk.pro.event_organizer_app.image.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pjatk.pro.event_organizer_app.catering.model.dto.CateringDto;
import pjatk.pro.event_organizer_app.location.model.dto.LocationDto;
import pjatk.pro.event_organizer_app.optional_service.model.dto.OptionalServiceDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageDto {

    private long id;

    @Size(min = 1, max = 300, message = "Path is too long")
    @NotBlank(message = "Image path is mandatory")
    private String path;

    @NotBlank(message = "Name is mandatory")
    @Size(min = 1, max = 50, message
            = "The name should be between 1 and 50 characters")
    private String name;

    private boolean isMain;

    private LocationDto location;

    private CateringDto catering;

    private OptionalServiceDto service;

    private String encodedImage;


}
