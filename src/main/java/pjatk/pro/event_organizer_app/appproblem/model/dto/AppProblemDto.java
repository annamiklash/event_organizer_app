package pjatk.pro.event_organizer_app.appproblem.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pjatk.pro.event_organizer_app.user.model.dto.UserDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppProblemDto implements Serializable {

    private long id;

    @NotBlank(message = "Concern is mandatory")
    private String concern;

    @NotBlank(message = "Concern is mandatory")
    @Size(min = 1, max = 300, message
            = "The name should be between 1 and 300 characters")
    private String description;

    private String createdAt;

    private String resolvedAt;

    private UserDto user;
}
