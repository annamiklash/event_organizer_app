package pjatk.pro.event_organizer_app.location.locationforevent.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pjatk.pro.event_organizer_app.cateringforchosenevent.model.dto.CateringForChosenEventLocationDto;
import pjatk.pro.event_organizer_app.event.model.dto.OrganizedEventDto;
import pjatk.pro.event_organizer_app.location.model.dto.LocationDto;
import pjatk.pro.event_organizer_app.optional_service.optional_service_for_location.model.dto.OptionalServiceForChosenLocationDto;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LocationForEventDto {

    private Long id;

    @NotNull
    private String timeFrom;

    @NotNull
    private String timeTo;

    @NotNull
    private int guestCount;

    private String date;

    private String confirmationStatus;

    private LocationDto location;

    private OrganizedEventDto event;

    private List<CateringForChosenEventLocationDto> caterings;

    private List<OptionalServiceForChosenLocationDto> optionalServices;
}
