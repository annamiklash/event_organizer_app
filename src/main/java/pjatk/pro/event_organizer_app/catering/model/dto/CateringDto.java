package pjatk.pro.event_organizer_app.catering.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pjatk.pro.event_organizer_app.address.model.dto.AddressDto;
import pjatk.pro.event_organizer_app.availability.dto.AvailabilityDto;
import pjatk.pro.event_organizer_app.business.model.dto.BusinessDto;
import pjatk.pro.event_organizer_app.businesshours.dto.BusinessHoursDto;
import pjatk.pro.event_organizer_app.common.constants.RegexConstants;
import pjatk.pro.event_organizer_app.cuisine.model.dto.CuisineDto;
import pjatk.pro.event_organizer_app.image.model.dto.ImageDto;
import pjatk.pro.event_organizer_app.location.model.dto.LocationDto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CateringDto implements Serializable {

    private Long id;

    @NotBlank(message = "Name is mandatory")
    @Size(min = 1, max = 100, message
            = "The name should be between 1 and 100 characters")
    @Pattern(regexp = RegexConstants.NAME_REGEX)
    private String name;

    private double rating;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    @Size(min = 5, max = 100, message
            = "Email should be between 5 and 100 characters")
    private String email;

    @NotBlank(message = "Phone number is mandatory")
    @Size(min = 9, max = 9, message
            = "phone number should be 9 characters long")
    @Pattern(regexp = RegexConstants.PHONE_NUMBER_REGEX, message = "should contain only digits")
    private String phoneNumber;

    @NotBlank(message = "If there no are service cost, please enter 0")
    @Pattern(regexp = RegexConstants.PRICE_REGEX, message = "should contain only digits or digits separated by a dot sign (1.23)")
    private String serviceCost;

    @NotBlank(message = "Description is mandatory")
    @Size(min = 1, max = 300, message
            = "Description can be no more than 300 symbols")
    private String description;

    @NotNull(message = "Flag is mandatory")
    private Boolean offersOutsideCatering;

    @NotNull(message = "Business hours is mandatory")
    private List<BusinessHoursDto> businessHours;

    @NotNull(message = "Cuisines is mandatory")
    private List<CuisineDto> cuisines;

    private List<AvailabilityDto> availability;

    private String createdAt;

    private String modifiedAt;

    private String deletedAt;

    private AddressDto address;

    private Boolean isOperating;

    private List<CateringItemDto> cateringItems;

    private BusinessDto business;

    private List<LocationDto> locations;

    private List<ImageDto> images;
}