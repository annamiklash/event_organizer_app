package pjatk.pro.event_organizer_app.cateringforchosenevent.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import pjatk.pro.event_organizer_app.businesshours.DayEnum;
import pjatk.pro.event_organizer_app.catering.model.Catering;
import pjatk.pro.event_organizer_app.catering.service.CateringService;
import pjatk.pro.event_organizer_app.cateringforchosenevent.mapper.CateringForChosenLocationMapper;
import pjatk.pro.event_organizer_app.cateringforchosenevent.model.CateringForChosenEventLocation;
import pjatk.pro.event_organizer_app.cateringforchosenevent.model.dto.CateringForChosenEventLocationDto;
import pjatk.pro.event_organizer_app.cateringforchosenevent.repository.CateringForLocationRepository;
import pjatk.pro.event_organizer_app.common.constants.Const;
import pjatk.pro.event_organizer_app.common.helper.TimestampHelper;
import pjatk.pro.event_organizer_app.common.util.CollectionUtil;
import pjatk.pro.event_organizer_app.common.util.DateTimeUtil;
import pjatk.pro.event_organizer_app.customer.repository.CustomerRepository;
import pjatk.pro.event_organizer_app.event.model.OrganizedEvent;
import pjatk.pro.event_organizer_app.event.repository.OrganizedEventRepository;
import pjatk.pro.event_organizer_app.exceptions.ActionNotAllowedException;
import pjatk.pro.event_organizer_app.exceptions.IllegalArgumentException;
import pjatk.pro.event_organizer_app.exceptions.LocationNotBookedException;
import pjatk.pro.event_organizer_app.exceptions.NotFoundException;
import pjatk.pro.event_organizer_app.location.locationforevent.model.LocationForEvent;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static pjatk.pro.event_organizer_app.enums.ConfirmationStatusEnum.CONFIRMED;
import static pjatk.pro.event_organizer_app.enums.ConfirmationStatusEnum.NOT_CONFIRMED;
import static pjatk.pro.event_organizer_app.enums.EventStatusEnum.CANCELLED;

@Service
@AllArgsConstructor
@Slf4j
public class CateringForChosenEventLocationService {

    private final CateringForLocationRepository cateringForLocationRepository;
    private final OrganizedEventRepository organizedEventRepository;
    private final CustomerRepository customerRepository;
    private final CateringService cateringService;
    private final TimestampHelper timestampHelper;

    public CateringForChosenEventLocation confirmReservation(long cateringId, long eventId) {
        final CateringForChosenEventLocation catering =
                cateringForLocationRepository.findByCateringIdAndEventId(cateringId, eventId)
                        .orElseThrow(() -> new NotFoundException("No catering for event " + eventId));

        catering.setConfirmationStatus(CONFIRMED.name());
        cateringForLocationRepository.save(catering);

        final OrganizedEvent organizedEvent = catering.getEventLocation().getEvent();

        organizedEvent.setModifiedAt(timestampHelper.now());
        organizedEventRepository.save(organizedEvent);

        return catering;
    }

    @Transactional(rollbackOn = Exception.class)
    public CateringForChosenEventLocation create(long customerId, long eventId, long cateringId, CateringForChosenEventLocationDto dto) {
        if (!customerRepository.existsById(customerId)) {
            throw new NotFoundException("Customer with id " + customerId + " DOES NOT EXIST");
        }
        final OrganizedEvent organizedEvent = organizedEventRepository.getWithLocation(eventId)
                .orElseThrow(() -> new NotFoundException("Organized event does not exist"));

        final LocationForEvent locationForEvent = organizedEvent.getLocationForEvent()
                .stream()
                .filter(location -> !CANCELLED.name().equals(location.getConfirmationStatus()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("No current reservation"));

        if (NOT_CONFIRMED.name().equals(locationForEvent.getConfirmationStatus())) {
            throw new LocationNotBookedException("You cannot book catering prior to booking location");
        }

        final boolean isOpen = isOpen(cateringId, organizedEvent.getDate().getDayOfWeek().name());
        if (!isOpen) {
            throw new NotFoundException("No catering with id " + cateringId + " is not open on a given date");
        }

        final Catering catering = cateringService.get(cateringId);
        if (!locationForEvent.getLocation().getCaterings().contains(catering)) {
            throw new NotFoundException("Catering cannot deliver to chosen location");
        }

        if (!dateValid(organizedEvent.getStartTime(), organizedEvent.getEndTime(), dto.getTime())) {
            throw new IllegalArgumentException("The time for catering booking should be between event time from and time to");
        }

        final CateringForChosenEventLocation cateringForLocation = CateringForChosenLocationMapper.fromDto(dto);

        cateringForLocation.setDate(organizedEvent.getDate());
        cateringForLocation.setEventLocation(locationForEvent);
        cateringForLocation.setCatering(catering);

        locationForEvent.setEvent(organizedEvent);

        cateringForLocationRepository.save(cateringForLocation);

        return cateringForLocation;
    }

    @Transactional(rollbackOn = Exception.class)
    public CateringForChosenEventLocation cancelReservation(long cateringForEventId) {
        final CateringForChosenEventLocation cateringForLocation = getWithCateringAndEvent(cateringForEventId);
        final OrganizedEvent event = cateringForLocation.getEventLocation().getEvent();

        final LocalTime time = cateringForLocation.getTime();
        final LocalDate date = event.getDate();
        final LocalDateTime dateTime = LocalDateTime.of(date, time);

        if (!isAllowedToCancel(dateTime)) {
            throw new ActionNotAllowedException("Cannot cancel reservation");
        }
        cateringForLocation.setConfirmationStatus(CANCELLED.name());
        event.setModifiedAt(timestampHelper.now());

        organizedEventRepository.save(event);
        cateringForLocationRepository.save(cateringForLocation);

        return cateringForLocation;
    }

    public void confirmOrder(long reservationId) {
        final CateringForChosenEventLocation catering = cateringForLocationRepository.getWithCateringOrder(reservationId)
                .orElseThrow(() -> new NotFoundException("No catering reservation"));

        if (!CollectionUtils.isEmpty(catering.getCateringOrder())) {
            catering.setCateringOrderConfirmed(true);
            cateringForLocationRepository.save(catering);
        }

    }


    public List<CateringForChosenEventLocation> listAllByStatus(long cateringId, String status) {
        return cateringForLocationRepository.findAllByCateringIdAndStatus(cateringId, status);
    }

    public List<CateringForChosenEventLocation> listAllByStatusAndBusinessId(long businessId, String status) {
        return cateringForLocationRepository.findAllByBusinessIdAndStatus(businessId, status);

    }

    public CateringForChosenEventLocation get(long cateringId) {
        return cateringForLocationRepository.findById(cateringId)
                .orElseThrow(() -> new NotFoundException("No booked catering with id " + cateringId + " was found"));
    }

    private boolean isOpen(long cateringId, String day) {
        final String dayName = DayEnum.valueOfLabel(day).name();
        final Catering catering = cateringService.getWithBusinessHours(cateringId);
        return CollectionUtil.emptyListIfNull(catering.getCateringBusinessHours()).stream()
                .anyMatch(cateringBusinessHours -> dayName.equals(cateringBusinessHours.getDay()));
    }

    private CateringForChosenEventLocation getWithCateringAndEvent(long cateringForEventId) {
        return cateringForLocationRepository.getWithCateringAndEvent(cateringForEventId)
                .orElseThrow(() -> new NotFoundException("No location Reservation with id " + cateringForEventId));
    }

    private boolean isAllowedToCancel(LocalDateTime dateTime) {
        return dateTime.minusDays(Const.MAX_CANCELLATION_DAYS_PRIOR).isAfter(timestampHelper.now());
    }

    private boolean dateValid(LocalTime startTime, LocalTime endTime, String bookingTime) {
        return DateTimeUtil.fromTimeStringToLocalTime(bookingTime).isBefore(endTime)
                && DateTimeUtil.fromTimeStringToLocalTime(bookingTime).isAfter(startTime);
    }


}
