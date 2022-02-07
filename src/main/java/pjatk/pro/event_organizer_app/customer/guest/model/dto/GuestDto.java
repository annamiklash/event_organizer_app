package pjatk.pro.event_organizer_app.customer.guest.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pjatk.pro.event_organizer_app.common.constants.RegexConstants;
import pjatk.pro.event_organizer_app.customer.model.dto.CustomerDto;
import pjatk.pro.event_organizer_app.event.model.dto.OrganizedEventDto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GuestDto implements Serializable {

    private long id;

    @NotBlank(message = "First name is mandatory")
    @Size(min = 1, max = 30, message
            = "The name should be between 1 and 30 characters")
    @Pattern(regexp = RegexConstants.FIRST_NAME_REGEX)
    private String firstName;

    @NotBlank(message = "Last name is mandatory")
    @Size(min = 1, max = 40, message
            = "The name should be between 1 and 40 characters")
    @Pattern(regexp = RegexConstants.LAST_NAME_REGEX)
    private String lastName;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    @Size(min = 5, max = 100, message
            = "Email should be between 5 and 100 characters")
    private String email;

    private String createdAt;

    private String modifiedAt;

    private CustomerDto customer;

    private OrganizedEventDto organizedEvent;

}
