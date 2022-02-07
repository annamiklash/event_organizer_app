package pjatk.pro.event_organizer_app.cuisine.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CuisineDto implements Serializable {

    private Long id;

    @Size(min = 1, max = 50, message
            = "Cannot be more than 50 symbols")
    private String name;
}
