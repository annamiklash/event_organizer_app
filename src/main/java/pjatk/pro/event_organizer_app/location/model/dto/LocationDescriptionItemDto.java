package pjatk.pro.event_organizer_app.location.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LocationDescriptionItemDto {

    @NotBlank(message = "Name is mandatory")
    private String id;

    @NotBlank(message = "Description is mandatory")
    private String description;

    private Set<LocationDto> locations;
}
