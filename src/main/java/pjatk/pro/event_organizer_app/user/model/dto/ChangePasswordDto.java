package pjatk.pro.event_organizer_app.user.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pjatk.pro.event_organizer_app.common.constants.RegexConstants;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChangePasswordDto {

    @NotNull
    private String oldPassword;

    @NotBlank
    @Size(min = 8, max = 100, message
            = "Password number should be at least 8 characters long")
    @Pattern(regexp = RegexConstants.PASSWORD_REGEX, message = "Password must contain at least one digit, " +
            "one uppercase letter and a special character")
    private String newPassword;
}
