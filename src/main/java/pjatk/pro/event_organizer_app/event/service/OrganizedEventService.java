package pjatk.pro.event_organizer_app.event.service;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pjatk.pro.event_organizer_app.cateringforchosenevent.model.CateringForChosenEventLocation;
import pjatk.pro.event_organizer_app.cateringforchosenevent.service.CateringForChosenEventLocationService;
import pjatk.pro.event_organizer_app.common.helper.TimestampHelper;
import pjatk.pro.event_organizer_app.common.mapper.PageableMapper;
import pjatk.pro.event_organizer_app.common.paginator.CustomPage;
import pjatk.pro.event_organizer_app.customer.model.Customer;
import pjatk.pro.event_organizer_app.customer.repository.CustomerRepository;
import pjatk.pro.event_organizer_app.enums.CustomerReservationTabEnum;
import pjatk.pro.event_organizer_app.event.helper.StatusChangeHelper;
import pjatk.pro.event_organizer_app.event.mapper.OrganizedEventMapper;
import pjatk.pro.event_organizer_app.event.model.OrganizedEvent;
import pjatk.pro.event_organizer_app.event.model.dto.OrganizedEventDto;
import pjatk.pro.event_organizer_app.event.repository.OrganizedEventRepository;
import pjatk.pro.event_organizer_app.exceptions.IllegalArgumentException;
import pjatk.pro.event_organizer_app.exceptions.NotFoundException;
import pjatk.pro.event_organizer_app.location.locationforevent.model.LocationForEvent;
import pjatk.pro.event_organizer_app.location.locationforevent.service.LocationForEventService;
import pjatk.pro.event_organizer_app.optional_service.model.interpreter.Interpreter;
import pjatk.pro.event_organizer_app.optional_service.model.interpreter.translation.service.TranslationLanguageService;
import pjatk.pro.event_organizer_app.optional_service.optional_service_for_location.model.OptionalServiceForChosenLocation;
import pjatk.pro.event_organizer_app.optional_service.optional_service_for_location.service.OptionalServiceForLocationService;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static pjatk.pro.event_organizer_app.enums.EventStatusEnum.CANCELLED;
import static pjatk.pro.event_organizer_app.optional_service.enums.OptionalServiceTypeEnum.INTERPRETER;

@Service
@AllArgsConstructor
@Slf4j
public class OrganizedEventService {

    private final OrganizedEventRepository organizedEventRepository;
    private final EventTypeService eventTypeService;
    private final CustomerRepository customerRepository;
    private final CateringForChosenEventLocationService cateringForChosenEventLocationService;
    private final LocationForEventService locationForEventService;
    private final OptionalServiceForLocationService optionalServiceForLocationService;
    private final StatusChangeHelper statusChangeHelper;
    private final TimestampHelper timestampHelper;
    private final TranslationLanguageService translationLanguageService;

    public ImmutableList<OrganizedEventDto> list(CustomPage customPage, String keyword) {
        keyword = Strings.isNullOrEmpty(keyword) ? "" : keyword.toLowerCase();

        final Pageable paging = PageableMapper.map(customPage);
        final Page<OrganizedEvent> page = organizedEventRepository.findAll(paging);

        return page.get()
                .map(OrganizedEventMapper::toDtoWithCustomer)
                .collect(ImmutableList.toImmutableList());
    }

    public OrganizedEvent get(long orgEventId) {
        return organizedEventRepository.findById(orgEventId)
                .orElseThrow(() -> new NotFoundException("No organized event with id " + orgEventId));
    }

    public OrganizedEvent getWithDetail(long orgEventId) {
        return organizedEventRepository.getWithDetail(orgEventId)
                .orElseThrow(() -> new NotFoundException("No organized event with id " + orgEventId));

    }

    public OrganizedEvent getWithDetail(long orgEventId, long customerId) {
        final OrganizedEvent organizedEvent = organizedEventRepository.getWithDetail(orgEventId, customerId)
                .orElseThrow(() -> new NotFoundException("No organized event with id " + orgEventId));

        final Set<LocationForEvent> locationForEventSet = organizedEvent.getLocationForEvent();
        if (CollectionUtils.isEmpty(locationForEventSet)) {
            return organizedEvent;
        }

        locationForEventSet.stream()
                .filter(location -> !"CANCELLED".equals(location.getConfirmationStatus()))
                .findFirst()
                .map(location -> location.getServices().stream()
                        .map(OptionalServiceForChosenLocation::getOptionalService)
                        .peek(optionalService -> {
                            if (optionalService.getType().equals(INTERPRETER.getValue())) {
                                ((Interpreter) optionalService).setLanguages(
                                        new HashSet<>(translationLanguageService.getAllByInterpreterId(optionalService.getId())));
                            }
                        })
                        .collect(Collectors.toSet()));

        return organizedEvent;

    }

    public void save(OrganizedEvent organizedEvent) {
        organizedEventRepository.save(organizedEvent);
    }

    public void delete(OrganizedEvent organizedEvent) {
        organizedEventRepository.delete(organizedEvent);
    }

    public OrganizedEvent getWithAllInformationForSendingInvitations(long eventId, long customerId) {
        return organizedEventRepository.getWithAllInformationForSendingInvitations(eventId, customerId)
                .orElseThrow(() -> new NotFoundException("No organized event with eventId " + eventId));
    }

    public OrganizedEvent cancel(OrganizedEvent organizedEvent) {
        final Set<LocationForEvent> locationForEventSet = organizedEvent.getLocationForEvent();

        final boolean areOnlyCancelled = locationForEventSet.stream()
                .allMatch(location -> "CANCELLED".equals(location.getConfirmationStatus()));

        if (areOnlyCancelled) {
            organizedEvent.setEventStatus(CANCELLED.name());
            organizedEventRepository.save(organizedEvent);

            return organizedEvent;
        }

        final LocationForEvent locationForEvent = locationForEventSet.stream()
                .filter(location -> !CANCELLED.name().equals(location.getConfirmationStatus()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("No current reservation"));

        final Set<OptionalServiceForChosenLocation> services = locationForEvent.getServices();
        final Set<CateringForChosenEventLocation> caterings = locationForEvent.getCateringsForEventLocation();

        services.stream()
                .filter(Objects::nonNull)
                .forEach(optionalServiceForChosenLocation ->
                        optionalServiceForLocationService.cancelReservation(optionalServiceForChosenLocation.getId()));

        caterings.stream()
                .filter(Objects::nonNull)
                .forEach(catering -> cateringForChosenEventLocationService.cancelReservation(catering.getId()));

        locationForEventService.cancelReservation(locationForEvent.getId());

        organizedEvent.setEventStatus(CANCELLED.name());
        organizedEventRepository.save(organizedEvent);

        return organizedEvent;

    }

    public List<OrganizedEvent> getAllByCustomerIdAndTab(long customerId, CustomerReservationTabEnum tabEnum) {
        if (!customerRepository.existsById(customerId)) {
            throw new NotFoundException("No customer with " + customerId);
        }

        switch (tabEnum) {
            case ALL:
                return organizedEventRepository.findAllByCustomer_Id(customerId);
            case PAST:
                return organizedEventRepository.findAllFinished(customerId);
            case CURRENT:
                return organizedEventRepository.findAllCurrent(customerId);
            default:
                throw new IllegalArgumentException("Incorrect customer reservation type");
        }
    }

    public OrganizedEvent getWithLocation(long eventId) {
        return organizedEventRepository.getWithLocation(eventId)
                .orElseThrow(() -> new NotFoundException("No event with id " + eventId));
    }

    public OrganizedEvent create(long customerId, OrganizedEventDto dto) {
        final Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new NotFoundException("Customer not found"));

        final OrganizedEvent organizedEvent = OrganizedEventMapper.fromDto(dto);

        organizedEvent.setEventType(eventTypeService.getByType(dto.getEventType()));
        organizedEvent.setCustomer(customer);
        organizedEvent.setCreatedAt(timestampHelper.now());
        organizedEvent.setModifiedAt(timestampHelper.now());

        save(organizedEvent);

        return organizedEvent;
    }

    public void performUpdate() {
        final List<OrganizedEvent> all = organizedEventRepository.findAll();
        CollectionUtils.emptyIfNull(all).stream()
                .filter(organizedEvent -> Objects.equals(organizedEvent.getEventStatus(), "READY")
                        && organizedEvent.getDate().isAfter(timestampHelper.now().toLocalDate()))
                .peek(organizedEvent -> organizedEvent.setEventStatus("FINISHED"))
                .forEach(organizedEventRepository::save);
    }
}
