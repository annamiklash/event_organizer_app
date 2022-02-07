package pjatk.pro.event_organizer_app.event.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventTypeDto implements Serializable {

    private Long id;

    @NotBlank(message = "Type is mandatory")
    private String type;

    private Set<OrganizedEventDto> events;
}
