package pjatk.pro.event_organizer_app.cateringforchosenevent.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pjatk.pro.event_organizer_app.catering.model.dto.CateringItemDto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CateringOrderChoiceDto implements Serializable {

    private long id;

    @Min(1)
    @NotNull(message = "Amount is mandatory")
    private Integer amount;

    @NotNull(message = "Amount is mandatory")
    private long itemId;

    private CateringItemDto item;

    private CateringForChosenEventLocationDto catering;
}
