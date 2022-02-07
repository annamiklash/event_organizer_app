package pjatk.pro.event_organizer_app.reviews;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pjatk.pro.event_organizer_app.catering.model.dto.CateringDto;
import pjatk.pro.event_organizer_app.customer.model.dto.CustomerDto;
import pjatk.pro.event_organizer_app.location.model.dto.LocationDto;
import pjatk.pro.event_organizer_app.optional_service.model.dto.OptionalServiceDto;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewDto {

    private long id;

    @NotBlank(message = "Title is mandatory")
    private String title;

    @NotBlank(message = "Comment is mandatory")
    @Size(min = 5, max = 100, message
            = "comment should be between 5 and 100 characters")
    private String comment;

    @Min(1)
    @Max(5)
    @NotNull(message = "Rating is mandatory")
    private int starRating;

    private CustomerDto customer;

    private LocationDto location;

    private CateringDto catering;

    private OptionalServiceDto optionalServiceDto;
}
