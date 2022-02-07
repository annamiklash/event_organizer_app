package pjatk.pro.event_organizer_app.customer.model.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pjatk.pro.event_organizer_app.common.constants.RegexConstants;
import pjatk.pro.event_organizer_app.common.util.DateTimeUtil;
import pjatk.pro.event_organizer_app.customer.guest.model.dto.GuestDto;
import pjatk.pro.event_organizer_app.event.model.dto.OrganizedEventDto;
import pjatk.pro.event_organizer_app.image.model.dto.ImageDto;
import pjatk.pro.event_organizer_app.user.model.dto.UserDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerDto {

    private long id;

    private UserDto user;

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

    @NotBlank(message = "Birthdate is mandatory")
    @Pattern(regexp = RegexConstants.DATE_REGEX)
    @JsonFormat(pattern = DateTimeUtil.DATE_FORMAT)
    private String birthdate;

    @NotBlank(message = "Phone number is mandatory")
    @Size(min = 9, max = 9, message
            = "Phone number should be 9 characters long")
    @Pattern(regexp = RegexConstants.PHONE_NUMBER_REGEX, message = "should contain only digits")
    private String phoneNumber;

    private ImageDto avatar;

//    private CustomerAvatarDto avatar;

    private Set<GuestDto> guests;

    private Set<OrganizedEventDto> events;

}
