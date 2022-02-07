package pjatk.pro.event_organizer_app.business.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pjatk.pro.event_organizer_app.address.model.dto.AddressDto;
import pjatk.pro.event_organizer_app.catering.model.dto.CateringDto;
import pjatk.pro.event_organizer_app.common.constants.RegexConstants;
import pjatk.pro.event_organizer_app.location.model.dto.LocationDto;
import pjatk.pro.event_organizer_app.optional_service.model.dto.OptionalServiceDto;
import pjatk.pro.event_organizer_app.user.model.dto.UserDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BusinessDto implements Serializable {

    public long id;

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

    private UserDto user;

    @NotNull(message = "You need to specify address")
    private AddressDto address;

    private String verificationStatus;

    private Set<LocationDto> locations;

    private Set<CateringDto> caterings;

    private Set<OptionalServiceDto> services;

}
