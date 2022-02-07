package pjatk.pro.event_organizer_app.location.service;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pjatk.pro.event_organizer_app.address.model.Address;
import pjatk.pro.event_organizer_app.address.service.AddressService;
import pjatk.pro.event_organizer_app.availability.location.model.LocationAvailability;
import pjatk.pro.event_organizer_app.availability.location.repository.LocationAvailabilityRepository;
import pjatk.pro.event_organizer_app.business.model.Business;
import pjatk.pro.event_organizer_app.business.repository.BusinessRepository;
import pjatk.pro.event_organizer_app.businesshours.location.model.LocationBusinessHours;
import pjatk.pro.event_organizer_app.businesshours.location.service.LocationBusinessHoursService;
import pjatk.pro.event_organizer_app.catering.model.Catering;
import pjatk.pro.event_organizer_app.catering.repository.CateringRepository;
import pjatk.pro.event_organizer_app.common.convertors.Converter;
import pjatk.pro.event_organizer_app.common.helper.TimestampHelper;
import pjatk.pro.event_organizer_app.common.mapper.PageableMapper;
import pjatk.pro.event_organizer_app.common.paginator.CustomPage;
import pjatk.pro.event_organizer_app.common.util.CollectionUtil;
import pjatk.pro.event_organizer_app.common.util.DateTimeUtil;
import pjatk.pro.event_organizer_app.enums.BusinessVerificationStatusEnum;
import pjatk.pro.event_organizer_app.enums.LocationDescriptionItemEnum;
import pjatk.pro.event_organizer_app.exceptions.ActionNotAllowedException;
import pjatk.pro.event_organizer_app.exceptions.BusinessVerificationException;
import pjatk.pro.event_organizer_app.exceptions.NotFoundException;
import pjatk.pro.event_organizer_app.image.repository.LocationImageRepository;
import pjatk.pro.event_organizer_app.location.locationforevent.model.LocationForEvent;
import pjatk.pro.event_organizer_app.location.mapper.LocationMapper;
import pjatk.pro.event_organizer_app.location.model.Location;
import pjatk.pro.event_organizer_app.location.model.LocationDescriptionItem;
import pjatk.pro.event_organizer_app.location.model.dto.FilterLocationsDto;
import pjatk.pro.event_organizer_app.location.model.dto.LocationDto;
import pjatk.pro.event_organizer_app.location.repository.LocationRepository;
import pjatk.pro.event_organizer_app.reviews.location.service.LocationReviewService;
import pjatk.pro.event_organizer_app.security.model.UserCredentials;
import pjatk.pro.event_organizer_app.security.service.SecurityService;

import javax.transaction.Transactional;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

import static pjatk.pro.event_organizer_app.availability.AvailabilityEnum.AVAILABLE;
import static pjatk.pro.event_organizer_app.availability.AvailabilityEnum.NOT_AVAILABLE;

@Service
@AllArgsConstructor
@Slf4j
public class LocationService {

    private final LocationRepository locationRepository;

    private final LocationDescriptionItemService locationDescriptionItemService;

    private final CateringRepository cateringRepository;

    private final AddressService addressService;

    private final BusinessRepository businessRepository;

    private final LocationAvailabilityRepository locationAvailabilityRepository;

    private final LocationBusinessHoursService locationBusinessHoursService;

    private final SecurityService securityService;

    private final LocationReviewService locationReviewService;

    private final LocationImageRepository locationImageRepository;

    private final TimestampHelper timestampHelper;

    public ImmutableList<Location> list(CustomPage customPage, String keyword) {
        keyword = Strings.isNullOrEmpty(keyword) ? "" : keyword.toLowerCase();

        final Pageable paging = PageableMapper.map(customPage);
        final Page<Location> page = locationRepository.findAllWithKeyword(paging, keyword);

        return ImmutableList.copyOf(page.get()
                .peek(location -> location.setRating(locationReviewService.getRating(location.getId())))
                .collect(Collectors.toList()));
    }

    public Long count(String keyword) {
        keyword = Strings.isNullOrEmpty(keyword) ? "" : keyword.toLowerCase();
        return locationRepository.countAll(keyword);
    }

    public Location get(long id) {
        return locationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Location with id " + id + " DOES NOT EXIST"));
    }

    public Location getWithDetail(long id) {
        final Location location = locationRepository.getByIdWithDetail(id)
                .orElseThrow(() -> new NotFoundException("Location with id " + id + " DOES NOT EXIST"));

        location.setRating(locationReviewService.getRating(id));
        return location;
    }

    public boolean isAvailable(long locationId, String date, String timeFrom, String timeTo) {
        timeFrom = DateTimeUtil.joinDateAndTime(date, timeFrom);
        timeTo = DateTimeUtil.joinDateAndTime(date, timeTo);
        return locationRepository.available(locationId, date, timeFrom, timeTo).isPresent();

    }

    public List<String> getCities() {
        return locationRepository.findDistinctCities();
    }

    public boolean exists(long id) {
        return locationRepository.existsById(id);
    }

    public ImmutableList<Location> search(FilterLocationsDto dto) {
        List<Location> locations = new ArrayList<>();

        String city = dto.getCity();
        city = Strings.isNullOrEmpty(dto.getCity()) ? "" : city.substring(0, city.indexOf(','));

        List<LocationDescriptionItem> filters;

        filters = CollectionUtils.isEmpty(dto.getDescriptionItems()) ? new ArrayList<>() :
                dto.getDescriptionItems().stream()
                        .map(locationDescriptionItemService::getById)
                        .collect(Collectors.toList());


        if (dto.getDate() == null) {
            locations = locationRepository.searchWithoutDate(city);

        } else {
            locations = locationRepository.searchWithDate(city, dto.getDate());
        }

        locations = locations.stream()
                .filter(location -> location.getDescriptions().containsAll(filters))
                .collect(Collectors.toList());

        if (dto.getIsSeated() == null && dto.getGuestCount() != null) {
            return ImmutableList.copyOf(
                    locations.stream()
                            .filter(location -> dto.getGuestCount() <= location.getSeatingCapacity() + location.getStandingCapacity())
                            .collect(Collectors.toList()));
        } else if (dto.getIsSeated() != null && dto.getIsSeated() != null) {

            if (dto.getIsSeated()) {
                return ImmutableList.copyOf(
                        locations.stream()
                                .filter(location -> dto.getGuestCount() <= location.getSeatingCapacity())
                                .collect(Collectors.toList()));
            }

            if (!dto.getIsSeated()) {
                return ImmutableList.copyOf(
                        locations.stream()
                                .filter(location -> dto.getGuestCount() <= location.getStandingCapacity())
                                .collect(Collectors.toList()));
            }
        }
        locations = filterByPrice(dto.getMinPrice(), dto.getMaxPrice(), locations);

        return ImmutableList.copyOf(locations.stream()
                .peek(location -> locationReviewService.getRating(location.getId()))
                .collect(Collectors.toList()));
    }

    public ImmutableList<Location> findByCity(String city) {
        return ImmutableList.copyOf(locationRepository.findByLocationAddress_City(city));
    }

    @Transactional(rollbackOn = Exception.class)
    public Location create(LocationDto dto) {
        final UserCredentials userCredentials = securityService.getUserCredentials();

        final Business business = businessRepository.findById(userCredentials.getUserId())
                .orElseThrow(() -> new NotFoundException("Business with id " + userCredentials.getUserId() + " DOES NOT EXIST"));

        if (!business.getVerificationStatus().equals(String.valueOf(BusinessVerificationStatusEnum.VERIFIED))) {
            throw new BusinessVerificationException(BusinessVerificationException.Enum.BUSINESS_NOT_VERIFIED);
        }

        final Address address = addressService.create(dto.getAddress());

        final List<LocationBusinessHours> businessHours = locationBusinessHoursService.create(dto.getBusinessHours());

        final Set<String> locationDescriptionEnumSet = dto.getDescriptions();

        final Location location = LocationMapper.fromDto(dto);
        final Set<LocationDescriptionItem> descriptions = locationDescriptionEnumSet.stream()
                .map(locationDescriptionItemService::getById)
                .collect(Collectors.toSet());

        location.setLocationAddress(address);
        location.setBusiness(business);
        location.setDescriptions(descriptions);
        location.setLocationBusinessHours(new HashSet<>(businessHours));
        location.setImages(new HashSet<>());
        location.setRating(0.0);
        location.setCreatedAt(timestampHelper.now());
        location.setModifiedAt(timestampHelper.now());

        //!SERVES_FOOD && OUTSIDE_CATERING_AVAILABLE
        if (locationDescriptionEnumSet.contains(LocationDescriptionItemEnum.OUTSIDE_CATERING_AVAILABLE.getValue())) {
            final List<Catering> caterings = cateringRepository.findByCateringAddress_City(address.getCity());
            location.setCaterings(new HashSet<>(caterings));

        }
        save(location);

        createAvailabilitiesForNewLocation(businessHours, location);

        return location;
    }


    public void save(Location location) {
        log.info("TRYING TO SAVE LOCATION " + location.toString());
        locationRepository.save(location);
    }

    public Location getWithAvailability(long locationId, String date) {
        final Location location = locationRepository.getByIdWithAvailability(locationId, date)
                .orElseThrow(() -> new NotFoundException("Location with id " + locationId + " DOES NOT EXIST"));

        location.setRating(locationReviewService.getRating(locationId));
        return location;
    }

    @Transactional(rollbackOn = Exception.class)
    public Location edit(LocationDto dto, long id) {
        final Location location = getWithDetail(id);

        location.setEmail(dto.getEmail());
        location.setName(dto.getName());
        location.setPhoneNumber(Converter.convertPhoneNumberString(dto.getPhoneNumber()));
        location.setDescription(dto.getDescription());
        location.setSeatingCapacity(dto.getSeatingCapacity());
        location.setStandingCapacity(dto.getStandingCapacity());
        location.setDailyRentCost(Converter.convertPriceString(dto.getDailyRentCost()));

        final Set<String> inputDescriptionEnums = dto.getDescriptions();
        final Set<LocationDescriptionItem> inputDescriptions = inputDescriptionEnums.stream()
                .map(locationDescriptionItemService::getById)
                .collect(Collectors.toSet());

        final Set<LocationDescriptionItem> locationDescriptions =
                location.getDescriptions();

        locationDescriptions.forEach(locationDescriptionItem -> {
            if (!inputDescriptions.contains(locationDescriptionItem)) {
                location.removeDescriptionItem(locationDescriptionItem);
            }
        });

        inputDescriptions.forEach(locationDescriptionItem -> {
            if (!locationDescriptions.contains(locationDescriptionItem)) {
                location.addDescriptionItem(locationDescriptionItem);
            }
        });

        location.setModifiedAt(timestampHelper.now());

        save(location);

        return location;

    }

    public ImmutableList<Location> getByBusinessId(long id) {
        return ImmutableList.copyOf(locationRepository.findAllByBusiness_Id(id));
    }

    public Location getWithImages(long locationId) {
        return locationRepository.findWithImages(locationId)
                .orElseThrow(() -> new NotFoundException("Location with id " + locationId + " DOES NOT EXIST"));
    }

    //    @Cacheable("location_caterings")
    public ImmutableList<Location> getByCateringId(long cateringId) {
        return ImmutableList.copyOf(locationRepository.findAllByCateringId(cateringId));
    }

    @Transactional(rollbackOn = Exception.class)
    public void delete(long id) {
        final Location locationToDelete = locationRepository.getAllLocationInformation(id)
                .orElseThrow(() -> new NotFoundException("Location with id " + id + " DOES NOT EXIST"));

        final ImmutableList<LocationForEvent> reservations = CollectionUtil.emptyListIfNull(locationToDelete.getLocationForEvent());
        final boolean canDelete = reservations.stream()
                .allMatch(location -> "FINISHED".equals(location.getEvent().getEventStatus()));

        if (!canDelete) {
            throw new ActionNotAllowedException("Cannot delete location. There are reservations pending");
        }

        boolean hasPendingReservations = hasPendingReservations(locationToDelete);
        if (hasPendingReservations) {
            throw new ActionNotAllowedException("Cannot delete location with reservations pending");
        }
        CollectionUtil.emptyListIfNull(locationToDelete.getLocationBusinessHours())
                .forEach(locationBusinessHoursService::delete);

        CollectionUtil.emptyListIfNull(locationToDelete.getAvailability())
                .forEach(locationAvailabilityRepository::delete);

        CollectionUtil.emptyListIfNull(locationToDelete.getImages())
                .forEach(locationImageRepository::delete);

        CollectionUtil.emptyListIfNull(locationToDelete.getReviews())
                .forEach(locationReviewService::delete);

        final ImmutableList<LocationDescriptionItem> descriptions = CollectionUtil.emptyListIfNull(locationToDelete.getDescriptions());
        for (LocationDescriptionItem description : descriptions) {
            locationToDelete.removeDescriptionItem(description);
        }

        final ImmutableList<Catering> caterings = CollectionUtil.emptyListIfNull(locationToDelete.getCaterings());
        for (Catering catering : caterings) {
            locationToDelete.removeCatering(catering);
        }
        addressService.delete(locationToDelete.getLocationAddress());

        locationRepository.delete(locationToDelete);
    }

    @Transactional(rollbackOn = Exception.class)
    public void modifyAvailabilityAfterBooking(Location location, String eventDate, String dateTimeFrom, String dateTimeTo) {

        final Set<LocationAvailability> locationAvailability = location.getAvailability();

        final LocalDate date = DateTimeUtil.fromStringToFormattedDate(eventDate);
        final LocalDateTime timeFrom = DateTimeUtil.fromStringToFormattedDateTime(dateTimeFrom);
        final LocalDateTime timeTo = DateTimeUtil.fromStringToFormattedDateTime(dateTimeTo);

        final List<LocationAvailability> availabilityForDate = locationAvailability.stream()
                .filter(availability -> availability.getDate().equals(date))
                .filter(availability -> (timeFrom.isEqual(availability.getTimeFrom()) || timeFrom.isAfter(availability.getTimeFrom()))
                        && (timeTo.isEqual(availability.getTimeTo()) || timeTo.isBefore(availability.getTimeTo()))
                        && (availability.getStatus().equals(AVAILABLE.toString())))
                .collect(Collectors.toList());

        final LocationAvailability availability = availabilityForDate.get(0);

        final List<LocationAvailability> modified = modify(availability, date, timeFrom, timeTo);

        locationAvailabilityRepository.delete(availability);

        modified.forEach(locationAvailabilityRepository::saveAndFlush);

    }

    private List<Location> filterByPrice(String minPrice, String maxPrice, List<Location> locations) {
        if (minPrice == null && maxPrice == null) {
            return locations;
        } else if (minPrice != null && maxPrice == null) {
            return locations.stream()
                    .filter(location -> Converter.convertPriceString(minPrice).compareTo(location.getDailyRentCost()) < 0 ||
                            Converter.convertPriceString(minPrice).compareTo(location.getDailyRentCost()) == 0)
                    .collect(Collectors.toList());
        } else if (minPrice == null) {
            return locations.stream()
                    .filter(location -> Converter.convertPriceString(maxPrice).compareTo(location.getDailyRentCost()) > 0 ||
                            Converter.convertPriceString(maxPrice).compareTo(location.getDailyRentCost()) == 0)
                    .collect(Collectors.toList());
        } else {
            return locations.stream()
                    .filter(location -> Converter.convertPriceString(minPrice).compareTo(location.getDailyRentCost()) < 0 ||
                            Converter.convertPriceString(minPrice).compareTo(location.getDailyRentCost()) == 0)
                    .filter(location -> Converter.convertPriceString(maxPrice).compareTo(location.getDailyRentCost()) > 0 ||
                            Converter.convertPriceString(maxPrice).compareTo(location.getDailyRentCost()) == 0)
                    .collect(Collectors.toList());
        }
    }

    private void createAvailabilitiesForNewLocation(List<LocationBusinessHours> businessHours, Location location) {
        for (LocationBusinessHours businessHour : businessHours) {
            final String day = businessHour.getDay();
            final DayOfWeek dayOfWeek = DayOfWeek.valueOf(day);
            LocalDate startDate = LocalDate.now();
            final LocalDate endDate = startDate.plusDays(30);

            while (startDate.isBefore(endDate) || startDate.equals(endDate)) {
                final LocalDate nextWeekDay = startDate.with(TemporalAdjusters.next(dayOfWeek));
                final LocationAvailability availability = LocationAvailability.builder()
                        .date(nextWeekDay)
                        .timeFrom(LocalDateTime.of(nextWeekDay, businessHour.getTimeFrom()))
                        .timeTo(LocalDateTime.of(nextWeekDay, businessHour.getTimeTo()))
                        .status(AVAILABLE.name())
                        .build();

                availability.setLocation(location);
                locationAvailabilityRepository.save(availability);
                startDate = nextWeekDay;
            }
        }
    }


    private List<LocationAvailability> modify(LocationAvailability availability, LocalDate bookingDate, LocalDateTime bookingTimeFrom, LocalDateTime bookingTimeTo) {
        final List<LocationAvailability> modified = new ArrayList<>();

        modified.add(LocationAvailability.builder()
                .status(NOT_AVAILABLE.toString())
                .date(bookingDate)
                .timeFrom(bookingTimeFrom)
                .timeTo(bookingTimeTo)
                .location(availability.getLocation())
                .build());

        if (availability.getTimeFrom().isBefore(bookingTimeFrom)) {
            modified.add(LocationAvailability.builder()
                    .status(AVAILABLE.toString())
                    .date(bookingDate)
                    .timeFrom(availability.getTimeFrom())
                    .timeTo(bookingTimeFrom)
                    .location(availability.getLocation())
                    .build());
        }

        if (availability.getTimeTo().isAfter(bookingTimeTo)) {
            modified.add(LocationAvailability.builder()
                    .status(AVAILABLE.toString())
                    .date(bookingDate)
                    .timeFrom(bookingTimeTo)
                    .timeTo(availability.getTimeTo())
                    .location(availability.getLocation())
                    .build());
        }

        return modified;
    }

    private boolean hasPendingReservations(Location locationToDelete) {
        return locationToDelete.getLocationForEvent().stream()
                .map(LocationForEvent::getEvent)
                .anyMatch(organizedEvent -> Objects.equals(organizedEvent.getEventStatus(), "IN_PROGRESS") ||
                        Objects.equals(organizedEvent.getEventStatus(), "READY"));

    }

}
