package pjatk.pro.event_organizer_app.businesshours.catering.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pjatk.pro.event_organizer_app.businesshours.BusinessHoursValidator;
import pjatk.pro.event_organizer_app.businesshours.catering.model.CateringBusinessHours;
import pjatk.pro.event_organizer_app.businesshours.catering.repository.CateringBusinessHoursRepository;
import pjatk.pro.event_organizer_app.businesshours.dto.BusinessHoursDto;
import pjatk.pro.event_organizer_app.businesshours.mapper.BusinessHoursMapper;
import pjatk.pro.event_organizer_app.common.util.DateTimeUtil;
import pjatk.pro.event_organizer_app.exceptions.NotFoundException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class CateringBusinessHoursService {

    private final CateringBusinessHoursRepository cateringBusinessHoursRepository;

    public Set<CateringBusinessHours> create(List<BusinessHoursDto> dtos) {

        BusinessHoursValidator.validate(dtos);

        return dtos.stream()
                .map(BusinessHoursMapper::fromDtoToCatering)
                .peek(this::save)
                .collect(Collectors.toSet());
    }

    private void save(CateringBusinessHours cateringBusinessHours) {
        cateringBusinessHoursRepository.save(cateringBusinessHours);
    }

    public void delete(CateringBusinessHours businessHour) {
        cateringBusinessHoursRepository.delete(businessHour);
    }


    public CateringBusinessHours edit(long id, BusinessHoursDto dto) {
        final CateringBusinessHours businessHours = get(id);

        businessHours.setDay(dto.getDay().name());
        businessHours.setDay(dto.getDay().name());
        businessHours.setTimeTo(DateTimeUtil.fromTimeStringToLocalTime(dto.getTimeTo()));

        save(businessHours);
        return businessHours;

    }

    public CateringBusinessHours get(long id) {
        return cateringBusinessHoursRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No business hours with id " + id));
    }


}
