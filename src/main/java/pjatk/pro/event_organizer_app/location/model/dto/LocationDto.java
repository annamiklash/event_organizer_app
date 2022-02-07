package pjatk.pro.event_organizer_app.location.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.NumberFormat;
import pjatk.pro.event_organizer_app.address.model.dto.AddressDto;
import pjatk.pro.event_organizer_app.availability.dto.AvailabilityDto;
import pjatk.pro.event_organizer_app.business.model.dto.BusinessDto;
import pjatk.pro.event_organizer_app.businesshours.dto.BusinessHoursDto;
import pjatk.pro.event_organizer_app.catering.model.dto.CateringDto;
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
public class LocationDto implements Serializable {

    private long id;

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

    @NotNull
    @NumberFormat(style = NumberFormat.Style.NUMBER)
    private Integer seatingCapacity;

    @NotNull
    @NumberFormat(style = NumberFormat.Style.NUMBER)
    private Integer standingCapacity;

    private String description;

    @NotBlank(message = "If there no are service cost, please enter 0")
    @Pattern(regexp = RegexConstants.PRICE_REGEX, message = "should contain only digits or digits separated by a dot sign (1.23)")
    private String dailyRentCost;

    @NotNull
    @NumberFormat(style = NumberFormat.Style.NUMBER)
    private Integer sizeInSqMeters;

    @NotNull(message = "Location descriptions is mandatory")
    private Set<String> descriptions;

    @NotNull(message = "Address is mandatory")
    private AddressDto address;

    @NotNull(message = "Business hours is mandatory")
    private List<BusinessHoursDto> businessHours;

    private String createdAt;

    private String modifiedAt;

    private String deletedAt;

    private BusinessDto business;

    private Set<CateringDto> caterings;

    private List<AvailabilityDto> locationAvailability;

    private List<ImageDto> images;

}
