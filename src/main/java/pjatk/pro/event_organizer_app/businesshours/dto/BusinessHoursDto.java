package pjatk.pro.event_organizer_app.businesshours.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pjatk.pro.event_organizer_app.businesshours.DayEnum;
import pjatk.pro.event_organizer_app.catering.model.dto.CateringDto;
import pjatk.pro.event_organizer_app.common.constants.RegexConstants;
import pjatk.pro.event_organizer_app.common.util.DateTimeUtil;
import pjatk.pro.event_organizer_app.location.model.dto.LocationDto;
import pjatk.pro.event_organizer_app.optional_service.model.dto.OptionalServiceDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BusinessHoursDto implements Serializable {

    private long id;

    @NotBlank(message = "Day is mandatory")
    private DayEnum day;

    @NotBlank(message = "Time from is mandatory")
    @Pattern(regexp = RegexConstants.TIME_REGEX_WITH_SECONDS)
    @JsonFormat(pattern = DateTimeUtil.TIME_FORMAT)
    private String timeFrom;

    @NotBlank(message = "Time from is mandatory")
    @Pattern(regexp = RegexConstants.TIME_REGEX_WITH_SECONDS)
    private String timeTo;

    private LocationDto location;

    private CateringDto catering;

    private OptionalServiceDto optionalService;
}
