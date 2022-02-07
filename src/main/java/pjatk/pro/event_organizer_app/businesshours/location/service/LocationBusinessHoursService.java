package pjatk.pro.event_organizer_app.businesshours.location.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pjatk.pro.event_organizer_app.businesshours.BusinessHoursValidator;
import pjatk.pro.event_organizer_app.businesshours.dto.BusinessHoursDto;
import pjatk.pro.event_organizer_app.businesshours.location.model.LocationBusinessHours;
import pjatk.pro.event_organizer_app.businesshours.location.repository.LocationBusinessHoursRepository;
import pjatk.pro.event_organizer_app.businesshours.mapper.BusinessHoursMapper;
import pjatk.pro.event_organizer_app.common.util.DateTimeUtil;
import pjatk.pro.event_organizer_app.exceptions.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class LocationBusinessHoursService {

    private final LocationBusinessHoursRepository locationBusinessHoursRepository;

    public List<LocationBusinessHours> create(List<BusinessHoursDto> dtos) {

        BusinessHoursValidator.validate(dtos);

        return dtos.stream()
                .map(BusinessHoursMapper::fromDtoToLocation)
                .peek(locationBusinessHoursRepository::save)
                .collect(Collectors.toList());
    }

    public void delete(LocationBusinessHours locationBusinessHours) {
        locationBusinessHoursRepository.delete(locationBusinessHours);
    }

    public LocationBusinessHours edit(long id, BusinessHoursDto dto) {
        final LocationBusinessHours locationBusinessHours = get(id);

        locationBusinessHours.setDay(dto.getDay().name());
        locationBusinessHours.setDay(dto.getDay().name());
        locationBusinessHours.setTimeTo(DateTimeUtil.fromTimeStringToLocalTime(dto.getTimeTo()));

        locationBusinessHoursRepository.save(locationBusinessHours);
        return locationBusinessHours;

    }

    private LocationBusinessHours get(long id) {
        return locationBusinessHoursRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No business hours with id " + id));
    }

}
