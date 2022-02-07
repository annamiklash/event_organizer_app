package pjatk.pro.event_organizer_app.businesshours;

import lombok.experimental.UtilityClass;
import pjatk.pro.event_organizer_app.businesshours.dto.BusinessHoursDto;
import pjatk.pro.event_organizer_app.exceptions.IllegalArgumentException;

import java.util.List;

@UtilityClass
public class BusinessHoursValidator {

    public void validate(List<BusinessHoursDto> dtos) {
        final long daysCount = dtos.stream().map(BusinessHoursDto::getDay).distinct().count();

        if (daysCount != dtos.size()) {
            throw new IllegalArgumentException("Invalid business hours");
        }
    }
}
