package pjatk.pro.event_organizer_app.user.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pjatk.pro.event_organizer_app.address.model.dto.AddressDto;
import pjatk.pro.event_organizer_app.common.constants.RegexConstants;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BusinessUserRegistrationDto {

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

    @NotBlank(message = "Name is mandatory")
    @Size(min = 1, max = 100, message
            = "The name should be between 1 and 100 characters")
    @Pattern(regexp = RegexConstants.NAME_REGEX)
    private String businessName;

    @NotBlank(message = "Phone number is mandatory")
    @Size(min = 9, max = 9, message
            = "Phone number should be 9 characters long")
    @Pattern(regexp = RegexConstants.PHONE_NUMBER_REGEX, message = "should contain only digits")
    private String phoneNumber;

    @NotNull
    private AddressDto address;

    private UserDto user;

    private String verificationStatus;

    private final Character type = 'B';


}
