package pjatk.pro.event_organizer_app.event.helper;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;
import pjatk.pro.event_organizer_app.cateringforchosenevent.model.CateringForChosenEventLocation;
import pjatk.pro.event_organizer_app.enums.ConfirmationStatusEnum;
import pjatk.pro.event_organizer_app.event.model.OrganizedEvent;
import pjatk.pro.event_organizer_app.exceptions.NotFoundException;
import pjatk.pro.event_organizer_app.location.locationforevent.model.LocationForEvent;
import pjatk.pro.event_organizer_app.optional_service.optional_service_for_location.model.OptionalServiceForChosenLocation;

import java.util.Set;

import static pjatk.pro.event_organizer_app.enums.EventStatusEnum.CANCELLED;
import static pjatk.pro.event_organizer_app.enums.EventStatusEnum.IN_PROGRESS;

@Component
public class StatusChangeHelper {

    public boolean possibleToChangeStatusFromInProgressToConfirmed(OrganizedEvent organizedEvent) {
        final String eventStatus = organizedEvent.getEventStatus();
        final LocationForEvent locationForEvent = organizedEvent.getLocationForEvent()
                .stream()
                .filter(location -> !CANCELLED.name().equals(location.getConfirmationStatus()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("No current reservation"));

        if (!IN_PROGRESS.name().equals(eventStatus)
                || locationForEvent == null
                || ConfirmationStatusEnum.NOT_CONFIRMED.name().equals(locationForEvent.getConfirmationStatus())) {
            return false;
        }

        return areAllCateringForEventHaveConfirmedStatus(locationForEvent.getCateringsForEventLocation())
                && areAllServicesHaveConfirmedStatus(locationForEvent.getServices());

    }

    private boolean areAllServicesHaveConfirmedStatus(Set<OptionalServiceForChosenLocation> services) {
        if (CollectionUtils.isEmpty(services)) {
            return true;
        }
        return services
                .stream()
                .allMatch(service -> ConfirmationStatusEnum.CONFIRMED.name().equals(service.getConfirmationStatus()));
    }

    private boolean areAllCateringForEventHaveConfirmedStatus(Set<CateringForChosenEventLocation> cateringsForEventLocation) {
        if (CollectionUtils.isEmpty(cateringsForEventLocation)) {
            return true;
        }
        return cateringsForEventLocation
                .stream()
                .allMatch(catering -> ConfirmationStatusEnum.CONFIRMED.name().equals(catering.getConfirmationStatus()));
    }
}
