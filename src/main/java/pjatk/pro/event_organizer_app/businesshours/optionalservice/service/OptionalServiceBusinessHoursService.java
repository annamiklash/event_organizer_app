package pjatk.pro.event_organizer_app.businesshours.optionalservice.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pjatk.pro.event_organizer_app.businesshours.BusinessHoursValidator;
import pjatk.pro.event_organizer_app.businesshours.dto.BusinessHoursDto;
import pjatk.pro.event_organizer_app.businesshours.mapper.BusinessHoursMapper;
import pjatk.pro.event_organizer_app.businesshours.optionalservice.model.OptionalServiceBusinessHours;
import pjatk.pro.event_organizer_app.businesshours.optionalservice.repository.OptionalServiceBusinessHoursRepository;
import pjatk.pro.event_organizer_app.common.util.DateTimeUtil;
import pjatk.pro.event_organizer_app.exceptions.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class OptionalServiceBusinessHoursService {

    private final OptionalServiceBusinessHoursRepository optionalServiceBusinessHoursRepository;

    public List<OptionalServiceBusinessHours> create(List<BusinessHoursDto> dtos) {

        BusinessHoursValidator.validate(dtos);

        return dtos.stream()
                .map(BusinessHoursMapper::fromDtoToOptionalService)
                .peek(optionalServiceBusinessHoursRepository::save)
                .collect(Collectors.toList());
    }

    public OptionalServiceBusinessHours edit(long id, BusinessHoursDto dto) {
        final OptionalServiceBusinessHours businessHours = get(id);

        businessHours.setDay(dto.getDay().name());
        businessHours.setDay(dto.getDay().name());
        businessHours.setTimeTo(DateTimeUtil.fromTimeStringToLocalTime(dto.getTimeTo()));

        optionalServiceBusinessHoursRepository.save(businessHours);
        return businessHours;

    }

    public void delete(OptionalServiceBusinessHours businessHours) {
        optionalServiceBusinessHoursRepository.delete(businessHours);
    }

    private OptionalServiceBusinessHours get(long id) {
        return optionalServiceBusinessHoursRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No business hours with id " + id));
    }

}
