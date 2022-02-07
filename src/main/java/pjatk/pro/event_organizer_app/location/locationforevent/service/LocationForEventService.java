package pjatk.pro.event_organizer_app.location.locationforevent.service;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import pjatk.pro.event_organizer_app.availability.location.model.LocationAvailability;
import pjatk.pro.event_organizer_app.availability.location.service.LocationAvailabilityService;
import pjatk.pro.event_organizer_app.cateringforchosenevent.model.CateringForChosenEventLocation;
import pjatk.pro.event_organizer_app.common.constants.Const;
import pjatk.pro.event_organizer_app.common.helper.TimestampHelper;
import pjatk.pro.event_organizer_app.common.util.DateTimeUtil;
import pjatk.pro.event_organizer_app.customer.repository.CustomerRepository;
import pjatk.pro.event_organizer_app.event.model.OrganizedEvent;
import pjatk.pro.event_organizer_app.event.repository.OrganizedEventRepository;
import pjatk.pro.event_organizer_app.exceptions.ActionNotAllowedException;
import pjatk.pro.event_organizer_app.exceptions.NotAvailableException;
import pjatk.pro.event_organizer_app.exceptions.NotFoundException;
import pjatk.pro.event_organizer_app.location.locationforevent.mapper.LocationForEventMapper;
import pjatk.pro.event_organizer_app.location.locationforevent.model.LocationForEvent;
import pjatk.pro.event_organizer_app.location.locationforevent.model.dto.LocationForEventDto;
import pjatk.pro.event_organizer_app.location.locationforevent.repository.LocationForEventRepository;
import pjatk.pro.event_organizer_app.location.model.Location;
import pjatk.pro.event_organizer_app.location.service.LocationService;
import pjatk.pro.event_organizer_app.optional_service.optional_service_for_location.model.OptionalServiceForChosenLocation;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

import static pjatk.pro.event_organizer_app.enums.ConfirmationStatusEnum.CANCELLED;
import static pjatk.pro.event_organizer_app.enums.ConfirmationStatusEnum.CONFIRMED;

@Service
@AllArgsConstructor
@Slf4j
public class LocationForEventService {

    private final LocationForEventRepository locationForEventRepository;

    private final OrganizedEventRepository organizedEventRepository;

    private final CustomerRepository customerRepository;

    private final LocationService locationService;

    private final LocationAvailabilityService locationAvailabilityService;

    private final TimestampHelper timestampHelper;

    @Transactional(rollbackOn = Exception.class)
    public LocationForEvent create(long customerId, long eventId, long locationId, LocationForEventDto dto) {
        if (customerRepository.findById(customerId).isEmpty()) {
            throw new NotFoundException("Customer with id " + customerId + " DOES NOT EXIST");
        }
        final OrganizedEvent organizedEvent = organizedEventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event does not exist"));

        final String date = DateTimeUtil.fromLocalDateToDateString(organizedEvent.getDate());

        final boolean isAvailable = locationService.isAvailable(locationId, date, dto.getTimeFrom(), dto.getTimeTo());

        if (!isAvailable) {
            throw new NotAvailableException("Location not available for selected date and time");
        }
        final Location location = locationService.getWithAvailability(locationId, date);

        final String timeFrom = DateTimeUtil.joinDateAndTime(date, dto.getTimeFrom());
        final String timeTo = DateTimeUtil.joinDateAndTime(date, dto.getTimeTo());
        locationService.modifyAvailabilityAfterBooking(location, date, timeFrom, timeTo);

        final LocationForEvent locationForEvent = LocationForEventMapper.fromDto(dto);
        locationForEvent.setLocation(location);
        locationForEvent.setEvent(organizedEvent);

        locationForEventRepository.save(locationForEvent);
        return locationForEvent;
    }

    @Transactional(rollbackOn = Exception.class)
    public LocationForEvent cancelReservation(long locationForEventId) {
        final LocationForEvent locationForEvent = getWithLocationAndEvent(locationForEventId);

        if (!CollectionUtils.isEmpty(locationForEvent.getServices()) && !isServiceCancelled(locationForEvent.getServices())) {
            throw new ActionNotAllowedException("Cannot cancel reservation for venue while there are service reservation for given event");
        }

        if (!CollectionUtils.isEmpty(locationForEvent.getCateringsForEventLocation()) && !isCateringCancelled(locationForEvent.getCateringsForEventLocation())) {
            throw new ActionNotAllowedException("Cannot cancel reservation for venue while there are catering reservation for given event");
        }

        final OrganizedEvent event = locationForEvent.getEvent();

        final LocalTime timeFrom = locationForEvent.getTimeFrom();
        final LocalTime timeTo = locationForEvent.getTimeTo();
        final LocalDate date = event.getDate();

        final LocalDateTime dateTime = LocalDateTime.of(date, timeFrom);

        if (!isAllowedToCancel(dateTime)) {
            throw new ActionNotAllowedException("Cannot cancel reservation");
        }

        locationForEvent.setConfirmationStatus(CANCELLED.name());

        final String stringTimeFrom = DateTimeUtil.joinDateAndTime(DateTimeUtil.fromLocalDateToDateString(date), DateTimeUtil.fromLocalTimeToString(timeFrom));
        final String stringTimeTo = DateTimeUtil.joinDateAndTime(DateTimeUtil.fromLocalDateToDateString(date), DateTimeUtil.fromLocalTimeToString(timeTo));

        LocationAvailability locationAvailability = locationAvailabilityService.getByDateAndTime(DateTimeUtil.fromLocalDateToDateString(date), stringTimeFrom, stringTimeTo);
        locationAvailabilityService.updateToAvailable(locationAvailability, locationForEvent.getLocation());

        event.setModifiedAt(timestampHelper.now());

        locationForEventRepository.save(locationForEvent);
        organizedEventRepository.save(event);

        return locationForEvent;
    }

    public LocationForEvent confirmReservation(long locationId, long eventId) {
        final LocationForEvent locationForEvent = findByLocationIdAndEventId(locationId, eventId);

        locationForEvent.setConfirmationStatus(CONFIRMED.toString());
        locationForEventRepository.save(locationForEvent);
        final OrganizedEvent organizedEvent = locationForEvent.getEvent();

        organizedEvent.setModifiedAt(timestampHelper.now());
        organizedEventRepository.save(organizedEvent);

        return locationForEvent;
    }

    public List<LocationForEvent> listAllByStatus(long locationId, String status) {
        return locationForEventRepository.findAllByLocationIdAndStatus(locationId, status);
    }

    public List<LocationForEvent> listAllByStatusAndBusinessId(long businessId, String status) {
        return locationForEventRepository.findAllBusinessIdAndStatus(businessId, status);
    }

    public LocationForEvent findByEventId(long eventId) {
        return locationForEventRepository.findByEventId(eventId)
                .orElseThrow(() -> new NotFoundException("Location for event does not exist"));
    }

    private LocationForEvent findByLocationIdAndEventId(long id, long eventId) {
        return locationForEventRepository.findByEventIdAndLocationId(eventId, id)
                .orElseThrow(() -> new NotFoundException("Location for event does not exist"));
    }

    private LocationForEvent getWithLocationAndEvent(long locationForEventId) {
        return locationForEventRepository.getWithLocationAndEvent(locationForEventId)
                .orElseThrow(() -> new NotFoundException("No location Reservation with id " + locationForEventId));
    }

    private boolean isAllowedToCancel(LocalDateTime dateTime) {
        return dateTime.minusDays(Const.MAX_CANCELLATION_DAYS_PRIOR).isAfter(timestampHelper.now());
    }

    private boolean isCateringCancelled(Set<CateringForChosenEventLocation> cateringsForEventLocation) {
        if (CollectionUtils.isEmpty(cateringsForEventLocation)) {
            return false;
        }
        return cateringsForEventLocation.stream()
                .allMatch(catering -> CANCELLED.name().equals(catering.getConfirmationStatus()));
    }

    private boolean isServiceCancelled(Set<OptionalServiceForChosenLocation> serviceForEvent) {
        if (CollectionUtils.isEmpty(serviceForEvent)) {
            return false;
        }
        return serviceForEvent.stream()
                .allMatch(service -> CANCELLED.name().equals(service.getConfirmationStatus()));
    }



}
