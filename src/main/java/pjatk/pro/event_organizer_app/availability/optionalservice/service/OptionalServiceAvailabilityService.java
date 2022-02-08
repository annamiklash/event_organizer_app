package pjatk.pro.event_organizer_app.availability.optionalservice.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pjatk.pro.event_organizer_app.availability.dto.AvailabilityDto;
import pjatk.pro.event_organizer_app.availability.mapper.AvailabilityMapper;
import pjatk.pro.event_organizer_app.availability.optionalservice.model.OptionalServiceAvailability;
import pjatk.pro.event_organizer_app.availability.optionalservice.repository.OptionalServiceAvailabilityRepository;
import pjatk.pro.event_organizer_app.common.util.DateTimeUtil;
import pjatk.pro.event_organizer_app.exceptions.IllegalArgumentException;
import pjatk.pro.event_organizer_app.exceptions.NotFoundException;
import pjatk.pro.event_organizer_app.optional_service.model.OptionalService;
import pjatk.pro.event_organizer_app.optional_service.service.OptionalServiceService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static pjatk.pro.event_organizer_app.availability.AvailabilityEnum.AVAILABLE;
import static pjatk.pro.event_organizer_app.availability.AvailabilityEnum.NOT_AVAILABLE;

@Service
@AllArgsConstructor
@Slf4j
public class OptionalServiceAvailabilityService {

    private final OptionalServiceAvailabilityRepository optionalServiceAvailabilityRepository;

    private final OptionalServiceService optionalServiceService;


    public List<OptionalServiceAvailability> update(List<AvailabilityDto> dtos, long serviceId, boolean deleteAll) {
        final OptionalService optionalService = optionalServiceService.get(serviceId);
        final List<OptionalServiceAvailability> result = new ArrayList<>();

        final Map<String, List<AvailabilityDto>> dateMap = dtos.stream()
                .collect(Collectors.groupingBy(AvailabilityDto::getDate));

        dateMap.keySet()
                .forEach(date -> {
                    final List<OptionalServiceAvailability> allByServiceIdAndDate =
                            findAllByServiceIdAndDate(optionalService.getId(), date)
                                    .stream().filter(serviceAvailability -> "AVAILABLE".equals(serviceAvailability.getStatus()))
                                    .collect(Collectors.toList());

                    if (deleteAll) {
                        allByServiceIdAndDate.forEach(optionalServiceAvailabilityRepository::delete);
                    }

                    dateMap.get(date).forEach(availabilityDto -> {
                        final OptionalServiceAvailability availability =
                                resolveAvailabilitiesForDay(availabilityDto, optionalService, false);

                        availability.setOptionalService(optionalService);
                        availability.setStatus(AVAILABLE.name());

                        optionalServiceAvailabilityRepository.save(availability);
                        result.add(availability);
                    });

                });

        return result;
    }

    public List<OptionalServiceAvailability> findAllByServiceIdAndDate(long id, String date) {
        return optionalServiceAvailabilityRepository.findAvailabilitiesByServiceIdAndDate(id, date);
    }

    public List<OptionalServiceAvailability> findAllByServiceIdAndDatePeriod(long id, String dateFrom, String dateTo) {
        if (DateTimeUtil.fromStringToFormattedDate(dateFrom).isAfter(DateTimeUtil.fromStringToFormattedDate(dateTo))) {
            throw new IllegalArgumentException("Date from after date to");
        }
        return optionalServiceAvailabilityRepository.findByIdAndPeriodDate(id, dateFrom, dateTo);
    }

    public void delete(long locationId, String date) {
        optionalServiceAvailabilityRepository.findAvailabilitiesByServiceIdAndDate(locationId, date).stream()
                .filter(cateringAvailability -> AVAILABLE.name().equals(cateringAvailability.getStatus()))
                .forEach(optionalServiceAvailabilityRepository::delete);
    }

    public void updateToAvailable(OptionalServiceAvailability serviceAvailability, OptionalService service) {
        final AvailabilityDto availabilityDto = AvailabilityMapper.toDto(serviceAvailability);

        final OptionalServiceAvailability availability = resolveAvailabilitiesForDay(availabilityDto, service, false);
        availability.setStatus(AVAILABLE.name());
        optionalServiceAvailabilityRepository.save(availability);
        optionalServiceAvailabilityRepository.deleteById(serviceAvailability.getId());

    }

    public void deleteById(long id) {
        optionalServiceAvailabilityRepository.deleteById(id);
        optionalServiceAvailabilityRepository.flush();
    }

    public OptionalServiceAvailability getByDateAndTime(String date, String timeFrom, String timeTo) {
        return optionalServiceAvailabilityRepository.getByDateAndTime(date, timeFrom, timeTo)
                .orElseThrow(() -> new NotFoundException("Nothing for given date and time"));
    }

    private OptionalServiceAvailability resolveAvailabilitiesForDay(AvailabilityDto dto, OptionalService service, boolean deleteAll) {
        final List<OptionalServiceAvailability> available = findAllByServiceIdAndDate(service.getId(), dto.getDate()).stream()
                .filter(serviceAvailability -> "AVAILABLE".equals(serviceAvailability.getStatus()))
                .collect(Collectors.toList());

        if (deleteAll) {
            available.forEach(optionalServiceAvailabilityRepository::delete);
        }

        final List<OptionalServiceAvailability> notAvailable = findAllByServiceIdAndDate(service.getId(), dto.getDate()).stream()
                .filter(locationAvailability -> "NOT_AVAILABLE".equals(locationAvailability.getStatus()))
                .collect(Collectors.toList());

        if (available.size() == 0 && notAvailable.size() == 0) {
            return AvailabilityMapper.fromDtoToOptionalServiceAvailability(dto);
        }
        if (available.size() > 0 || isToCancel(notAvailable, dto)) {
            final Optional<OptionalServiceAvailability> upperBordering = optionalServiceAvailabilityRepository.findByServiceIdAndTimeTo(service.getId(), DateTimeUtil.joinDateAndTime(dto.getDate(), dto.getTimeFrom())); //upper
            final Optional<OptionalServiceAvailability> lowerBordering = optionalServiceAvailabilityRepository.findByServiceIdAndTimeFrom(service.getId(), DateTimeUtil.joinDateAndTime(dto.getDate(), dto.getTimeTo()));//lower

            if (upperBordering.isEmpty() && lowerBordering.isEmpty()) { // -> save as it is
                return AvailabilityMapper.fromDtoToOptionalServiceAvailability(dto);
            }

            if (upperBordering.isPresent() && lowerBordering.isEmpty()) { // -> extend upper (upper.setTimeTo(dto.getTimeTo))
                final OptionalServiceAvailability availability = upperBordering.get();
                availability.setTimeTo(DateTimeUtil.fromStringToFormattedDateTime(DateTimeUtil.joinDateAndTime(dto.getDate(), dto.getTimeTo())));
                return availability;
            }

            if (upperBordering.isEmpty()) { // -> extend lower (lower.setTimeFrom(dto.getTimeFrom))
                final OptionalServiceAvailability availability = lowerBordering.get();
                availability.setTimeFrom(DateTimeUtil.fromStringToFormattedDateTime(DateTimeUtil.joinDateAndTime(dto.getDate(), dto.getTimeFrom())));
                return availability;
            } else {                        // -> change one of them, delete the other,
                final OptionalServiceAvailability lower = lowerBordering.get();
                final OptionalServiceAvailability upper = upperBordering.get();

                final OptionalServiceAvailability newAvailability = OptionalServiceAvailability.builder()
                        .date(upper.getDate())
                        .timeFrom(upper.getTimeFrom())
                        .timeTo(lower.getTimeTo())
                        .status(AVAILABLE.name())
                        .optionalService(lower.getOptionalService())
                        .build();

                optionalServiceAvailabilityRepository.deleteById(lower.getId());
                optionalServiceAvailabilityRepository.deleteById(upper.getId());
                return newAvailability;
            }
        } else {
            if (isNewWithinNotAvailable(dto, notAvailable)) {
                throw new IllegalArgumentException("Time from and time to for availability is withing reserved time frame");
            } else {
                return AvailabilityMapper.fromDtoToOptionalServiceAvailability(dto);
            }
        }
    }

    private boolean isToCancel(List<OptionalServiceAvailability> notAvailable, AvailabilityDto dto) {
        final Optional<OptionalServiceAvailability> or = notAvailable.stream().filter(locationAvailability -> locationAvailability.getStatus().equals(NOT_AVAILABLE.name())
                && locationAvailability.getDate().equals(DateTimeUtil.fromStringToFormattedDate(dto.getDate()))
                && locationAvailability.getTimeFrom().equals(DateTimeUtil.fromStringToFormattedDateTime(DateTimeUtil.joinDateAndTime(dto.getDate(), dto.getTimeFrom())))
                && locationAvailability.getTimeTo().equals(DateTimeUtil.fromStringToFormattedDateTime(DateTimeUtil.joinDateAndTime(dto.getDate(), dto.getTimeFrom()))))
                .findAny();
        return or.isPresent();
    }

    private boolean isNewWithinNotAvailable(AvailabilityDto dto, List<OptionalServiceAvailability> notAvailable) {
        final LocalDateTime from = DateTimeUtil.fromStringToFormattedDateTime(DateTimeUtil.joinDateAndTime(dto.getDate(), dto.getTimeFrom()));
        final LocalDateTime to = DateTimeUtil.fromStringToFormattedDateTime(DateTimeUtil.joinDateAndTime(dto.getDate(), dto.getTimeTo()));

        for (OptionalServiceAvailability availability : notAvailable) {
            if (from.isBefore(availability.getTimeTo()) && from.isAfter(availability.getTimeFrom())) {
                return true;
            }
            if (to.isBefore(availability.getTimeTo()) && to.isAfter(availability.getTimeFrom())) {
                return true;
            }
        }
        return false;

    }
}
