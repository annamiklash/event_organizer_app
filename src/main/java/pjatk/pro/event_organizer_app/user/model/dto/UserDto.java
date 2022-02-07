package pjatk.pro.event_organizer_app.user.model.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pjatk.pro.event_organizer_app.appproblem.model.dto.AppProblemDto;
import pjatk.pro.event_organizer_app.business.model.dto.BusinessDto;
import pjatk.pro.event_organizer_app.common.constants.RegexConstants;
import pjatk.pro.event_organizer_app.customer.model.dto.CustomerDto;

import javax.validation.constraints.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    private long id;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    @Size(min = 5, max = 100, message
            = "Email should be between 5 and 100 characters")
    private String email;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 8, max = 100, message
            = "Password number should be at least 8 characters long")
    @Pattern(regexp = RegexConstants.PASSWORD_REGEX, message = "Password must contain at least one digit, " +
            "one uppercase letter and a special character")
    private String password;

    @NotNull
    private Character type;

    private String createdAt;

    private String modifiedAt;

    private String deletedAt;

    private String blockedAt;

    private boolean isActive;

    private List<AppProblemDto> appProblems;

    private BusinessDto business;

    private CustomerDto customer;

}
