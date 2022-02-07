package pjatk.pro.event_organizer_app.cateringforchosenevent.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pjatk.pro.event_organizer_app.catering.model.dto.CateringDto;
import pjatk.pro.event_organizer_app.location.locationforevent.model.dto.LocationForEventDto;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CateringForChosenEventLocationDto {

    private long id;

    @NotNull
    private String time;

    @NotNull
    private String comment;

    private String confirmationStatus;

    private Boolean isOrderConfirmed;

    private CateringDto catering;

    private LocationForEventDto eventLocation;

    private List<CateringOrderChoiceDto> order;
}
