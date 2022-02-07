package pjatk.pro.event_organizer_app.optional_service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pjatk.pro.event_organizer_app.address.model.dto.AddressDto;
import pjatk.pro.event_organizer_app.availability.dto.AvailabilityDto;
import pjatk.pro.event_organizer_app.business.model.dto.BusinessDto;
import pjatk.pro.event_organizer_app.businesshours.dto.BusinessHoursDto;
import pjatk.pro.event_organizer_app.common.constants.RegexConstants;
import pjatk.pro.event_organizer_app.image.model.dto.ImageDto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OptionalServiceDto implements Serializable {

    private long id;

    @Size(min = 1, max = 50, message
            = "The name should be between 1 and 50 characters")
    @NotBlank(message = "Alias from is mandatory")
    private String alias;

    private double rating;

    @NotBlank(message = "Type from is mandatory")
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

    @NotBlank(message = "Type from is mandatory")
    private String type;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    @Size(min = 5, max = 100, message
            = "Email should be between 5 and 100 characters")
    private String email;

    @NotBlank(message = "Description from is mandatory")
    private String description;

    @NotBlank(message = "If there no are service cost, please enter 0")
    @Pattern(regexp = RegexConstants.PRICE_REGEX, message = "should contain only digits or digits separated by a dot sign (1.23)")
    private String serviceCost;

    @NotNull(message = "Business hours is mandatory")
    private List<BusinessHoursDto> businessHours;

    @NotNull
    private AddressDto address;

    private Set<MusicStyleDto> musicStyle;

    private String instrument;

    private Set<TranslationLanguageDto> translationLanguages;

    private List<AvailabilityDto> serviceAvailability;

    private List<ImageDto> images;

    private Integer musicBandPeopleCount;

    private String kidPerformerType;

    private Integer kidAgeFrom;

    private Integer kidAgeTo;

    private String otherType;

    private String createdAt;

    private String modifiedAt;

    private String deletedAt;

    private BusinessDto business;
}
