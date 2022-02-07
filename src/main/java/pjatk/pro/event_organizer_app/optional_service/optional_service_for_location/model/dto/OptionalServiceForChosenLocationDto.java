package pjatk.pro.event_organizer_app.optional_service.optional_service_for_location.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pjatk.pro.event_organizer_app.location.locationforevent.model.dto.LocationForEventDto;
import pjatk.pro.event_organizer_app.optional_service.model.dto.OptionalServiceDto;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OptionalServiceForChosenLocationDto {

    private Long id;

    @NotNull
    private String timeFrom;

    @NotNull
    private String timeTo;

    @NotNull
    private String comment;

    private String confirmationStatus;

    private LocationForEventDto locationForEvent;

    private OptionalServiceDto optionalService;
}
