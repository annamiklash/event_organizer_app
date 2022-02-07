package pjatk.pro.event_organizer_app.catering.service;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pjatk.pro.event_organizer_app.address.model.Address;
import pjatk.pro.event_organizer_app.address.service.AddressService;
import pjatk.pro.event_organizer_app.business.model.Business;
import pjatk.pro.event_organizer_app.business.repository.BusinessRepository;
import pjatk.pro.event_organizer_app.businesshours.DayEnum;
import pjatk.pro.event_organizer_app.businesshours.catering.model.CateringBusinessHours;
import pjatk.pro.event_organizer_app.businesshours.catering.service.CateringBusinessHoursService;
import pjatk.pro.event_organizer_app.catering.mapper.CateringMapper;
import pjatk.pro.event_organizer_app.catering.model.Catering;
import pjatk.pro.event_organizer_app.catering.model.dto.CateringDto;
import pjatk.pro.event_organizer_app.catering.model.dto.FilterCateringsDto;
import pjatk.pro.event_organizer_app.catering.repository.CateringItemRepository;
import pjatk.pro.event_organizer_app.catering.repository.CateringRepository;
import pjatk.pro.event_organizer_app.common.convertors.Converter;
import pjatk.pro.event_organizer_app.common.helper.TimestampHelper;
import pjatk.pro.event_organizer_app.common.mapper.PageableMapper;
import pjatk.pro.event_organizer_app.common.paginator.CustomPage;
import pjatk.pro.event_organizer_app.common.util.CollectionUtil;
import pjatk.pro.event_organizer_app.common.util.DateTimeUtil;
import pjatk.pro.event_organizer_app.cuisine.model.Cuisine;
import pjatk.pro.event_organizer_app.cuisine.model.dto.CuisineDto;
import pjatk.pro.event_organizer_app.cuisine.service.CuisineService;
import pjatk.pro.event_organizer_app.enums.BusinessVerificationStatusEnum;
import pjatk.pro.event_organizer_app.exceptions.ActionNotAllowedException;
import pjatk.pro.event_organizer_app.exceptions.BusinessVerificationException;
import pjatk.pro.event_organizer_app.exceptions.IllegalArgumentException;
import pjatk.pro.event_organizer_app.exceptions.NotFoundException;
import pjatk.pro.event_organizer_app.image.repository.CateringImageRepository;
import pjatk.pro.event_organizer_app.location.model.Location;
import pjatk.pro.event_organizer_app.location.service.LocationService;
import pjatk.pro.event_organizer_app.reviews.catering.repository.CateringReviewRepository;
import pjatk.pro.event_organizer_app.security.model.UserCredentials;
import pjatk.pro.event_organizer_app.security.service.SecurityService;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class CateringService {

    private final CateringRepository cateringRepository;
    private final CateringItemRepository cateringItemRepository;
    private final LocationService locationService;
    private final AddressService addressService;
    private final SecurityService securityService;
    private final BusinessRepository businessRepository;
    private final CateringBusinessHoursService cateringBusinessHoursService;
    private final CuisineService cuisineService;
    private final TimestampHelper timestampHelper;
    private final CateringImageRepository cateringImageRepository;
    private final CateringReviewRepository cateringReviewRepository;

    //    @Cacheable("caterings")
    public ImmutableList<Catering> list(CustomPage customPage, String keyword) {
        keyword = Strings.isNullOrEmpty(keyword) ? "" : keyword.toLowerCase();

        final Pageable paging = PageableMapper.map(customPage);
        final Page<Catering> page = cateringRepository.findAllWithKeyword(paging, keyword);

        return ImmutableList.copyOf(page.get().collect(Collectors.toList()));
    }

    public ImmutableList<Catering> search(FilterCateringsDto dto, Long locationId) {
        String city = dto.getCity();
        city = Strings.isNullOrEmpty(dto.getCity()) ? "" : city.substring(0, city.indexOf(','));

        List<Catering> caterings;
        if (dto.getCuisines() != null) {
            final Set<Long> cuisines = dto.getCuisines().stream()
                    .map(cuisineService::getByName)
                    .map(Cuisine::getId)
                    .collect(Collectors.toSet());
            caterings = cateringRepository.search(cuisines, city, locationId);
        } else {
            caterings = cateringRepository.search(null, city, locationId);
        }

        if (dto.getDate() != null) {
            final LocalDate date = DateTimeUtil.fromStringToFormattedDate(dto.getDate());
            final String dateDayOfTheWeek = date.getDayOfWeek().name();

            caterings = caterings.stream()
                    .filter(catering -> catering.getCateringBusinessHours()
                            .stream()
                            .anyMatch(cateringBusinessHours -> cateringBusinessHours.getDay().equalsIgnoreCase(dateDayOfTheWeek)))
                    .collect(Collectors.toList());
        }

        caterings = filterByPrice(dto.getMinPrice(), dto.getMaxPrice(), caterings);
        return ImmutableList.copyOf(caterings);
    }

    public boolean isOpen(Catering catering, String day) {
        return catering.getCateringBusinessHours().stream()
                .anyMatch(cateringBusinessHours -> cateringBusinessHours.getDay().equals(DayEnum.valueOfLabel(day).name()));
    }

    public Catering get(long id) {
        return cateringRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Catering with ID " + id + " does not exist"));
    }

    //    @Cacheable("catering")
    public Catering getWithDetail(long id) {
        return cateringRepository.findByIdWithDetail(id)
                .orElseThrow(() -> new NotFoundException("Catering with ID " + id + " does not exist"));
    }

    public ImmutableList<Catering> getByBusinessId(long id) {
        return ImmutableList.copyOf(cateringRepository.findAllByBusiness_Id(id));
    }

    @Transactional(rollbackOn = Exception.class)
    public Catering create(CateringDto cateringDto, Long locationId) {
        final UserCredentials userCredentials = securityService.getUserCredentials();

        final Business business = businessRepository.findById(userCredentials.getUserId())
                .orElseThrow(() -> new NotFoundException("Business with id " + userCredentials.getUserId() + " DOES NOT EXIST"));

        if (!BusinessVerificationStatusEnum.VERIFIED.name().equals(business.getVerificationStatus())) {
            throw new BusinessVerificationException(BusinessVerificationException.Enum.BUSINESS_NOT_VERIFIED);
        }
        if (locationId == null) {
            return createStandaloneCatering(cateringDto, business);
        }
        return createCateringWithLocation(cateringDto, locationId, business);

    }

    public Catering getWithBusinessHours(long cateringId) {
        return cateringRepository.getWithBusinessHours(cateringId)
                .orElseThrow(() -> new NotFoundException("Not catering with id " + cateringId));
    }

    public Catering edit(long cateringId, CateringDto dto) {
        final Catering catering = get(cateringId);

        catering.setEmail(dto.getEmail());
        catering.setName(dto.getName());
        catering.setPhoneNumber(Converter.convertPhoneNumberString(dto.getPhoneNumber()));
        catering.setServiceCost(Converter.convertPriceString(dto.getServiceCost()));
        catering.setDescription(dto.getDescription());
        catering.setModifiedAt(timestampHelper.now());

        final Set<Cuisine> cuisines = catering.getCuisines();
        final Set<Cuisine> inputCuisines = dto.getCuisines().stream()
                .map(cuisine -> cuisineService.getByName(cuisine.getName()))
                .collect(Collectors.toSet());

        cuisines.forEach(cuisine -> {
            if (!inputCuisines.contains(cuisine)) {
                catering.removeCuisine(cuisine);
            }
        });

        inputCuisines.forEach(cuisine -> {
            if (!cuisines.contains(cuisine)) {
                catering.addCuisine(cuisine);
            }
        });

        cateringRepository.save(catering);
        log.info("UPDATED");

        return catering;
    }

    @Transactional(rollbackOn = Exception.class)
    public void delete(long id) {
        final Catering cateringToDelete = cateringRepository.findAllCateringInformation(id)
                .orElseThrow(() -> new NotFoundException("No catering with id " + id));

        boolean hasPendingReservations = hasPendingReservations(cateringToDelete);
        if (hasPendingReservations) {
            throw new ActionNotAllowedException("Cannot delete catering with reservations pending");
        }

        final ImmutableList<Location> locations = CollectionUtil.emptyListIfNull(cateringToDelete.getLocations());
        for (Location location : locations) {
            location.removeCatering(cateringToDelete);
            locationService.save(location);
        }

        final ImmutableList<Cuisine> cuisines = CollectionUtil.emptyListIfNull(cateringToDelete.getCuisines());
        for (Cuisine cuisine : cuisines) {
            cateringToDelete.removeCuisine(cuisine);
        }

        CollectionUtil.emptyListIfNull(cateringToDelete.getImages())
                .forEach(cateringImageRepository::delete);

        CollectionUtil.emptyListIfNull(cateringToDelete.getCateringBusinessHours())
                .forEach(cateringBusinessHoursService::delete);

        CollectionUtil.emptyListIfNull(cateringToDelete.getCateringItems())
                .forEach(cateringItemRepository::delete);

        CollectionUtil.emptyListIfNull(cateringToDelete.getReviews())
                .forEach(cateringReviewRepository::delete);

        addressService.delete(cateringToDelete.getCateringAddress());

        cateringRepository.delete(cateringToDelete);
    }

    public boolean cateringWithIdExists(Long id) {
        log.info("CHECKING IF CATERING WITH ID " + id + " EXISTS");
        return cateringRepository.existsById(id);
    }

    public Catering getWithImages(long cateringId) {
        return cateringRepository.findWithImages(cateringId)
                .orElseThrow(() -> new NotFoundException("Catering with id " + cateringId + " DOES NOT EXIST"));
    }


    //    @Cacheable("catering_locations")
    public ImmutableList<Catering> getByLocationId(long id) {
        return ImmutableList.copyOf(cateringRepository.findAllByLocationId(id));
    }

    public Long count(String keyword) {
        keyword = Strings.isNullOrEmpty(keyword) ? "" : keyword.toLowerCase();
        return cateringRepository.countAll(keyword);
    }

    public Catering getAllCateringInformation(long cateringId) {
        return cateringRepository.findAllCateringInformation(cateringId)
                .orElseThrow(() -> new NotFoundException("Catering with id " + cateringId + " DOES NOT EXIST"));
    }

    private boolean hasPendingReservations(Catering cateringToDelete) {
        return CollectionUtil.emptyListIfNull(cateringToDelete.getCateringForChosenEventLocations())
                .stream()
                .map(catering -> catering.getEventLocation().getEvent())
                .anyMatch(organizedEvent -> organizedEvent.getDate().isAfter(LocalDate.now()));
    }


    private Catering createCateringWithLocation(CateringDto dto, Long locationId, Business business) {
        final Location location = locationService.get(locationId);

        final Address address = addressService.create(dto.getAddress());
        final Set<CateringBusinessHours> businessHours = cateringBusinessHoursService.create(dto.getBusinessHours());
        final Catering catering = CateringMapper.fromDto(dto);

        catering.setCateringAddress(address);
        catering.setBusiness(business);
        catering.setCateringBusinessHours(ImmutableSet.copyOf(businessHours));

        final List<CuisineDto> cuisineDtos = dto.getCuisines();
        final Set<Cuisine> cuisines = cuisineDtos.stream()
                .map(CuisineDto::getName)
                .map(cuisineService::getByName)
                .collect(Collectors.toSet());

        catering.setCuisines(ImmutableSet.copyOf(cuisines));
        if (dto.getOffersOutsideCatering()) {
            catering.setLocations(new HashSet<>(getLocationsInSameCity(catering)));
        } else {
            catering.setLocations(ImmutableSet.of(location));
        }

        catering.setCreatedAt(timestampHelper.now());
        catering.setModifiedAt(timestampHelper.now());

        cateringRepository.saveAndFlush(catering);
        return catering;
    }

    private Catering createStandaloneCatering(CateringDto dto, Business business) {
        if (!dto.getOffersOutsideCatering()) {
            throw new IllegalArgumentException("Standalone catering must offer outside catering");
        }
        final Address address = addressService.create(dto.getAddress());
        log.info(dto.getBusinessHours().toString());
        final Set<CateringBusinessHours> businessHours = cateringBusinessHoursService.create(dto.getBusinessHours());

        final Catering catering = CateringMapper.fromDto(dto);

        catering.setCateringAddress(address);
        catering.setRating(0.0);
        catering.setBusiness(business);
        catering.setCateringBusinessHours(ImmutableSet.copyOf(businessHours));
        catering.setCreatedAt(timestampHelper.now());
        catering.setModifiedAt(timestampHelper.now());
        catering.setLocations(new HashSet<>());

        final List<CuisineDto> cuisineDtos = dto.getCuisines();

        final Set<Cuisine> cuisines = cuisineDtos.stream()
                .map(CuisineDto::getName)
                .map(cuisineService::getByName)
                .collect(Collectors.toSet());
        catering.setCuisines(cuisines);

        cateringRepository.saveAndFlush(catering);

        final ImmutableList<Location> locationsWithingSameCity = locationService.findByCity(address.getCity());

        locationsWithingSameCity
                .stream()
                .peek(location -> location.addCatering(catering))
                .forEach(locationService::save);

        return catering;
    }

    private List<Catering> filterByPrice(Integer priceNotLessThen, Integer priceNotMoreThan, List<Catering> caterings) {
        if (priceNotLessThen == null && priceNotMoreThan == null) {
            return caterings;
        }
        final BigDecimal minPrice = priceNotLessThen == null ? null : new BigDecimal(priceNotLessThen);
        final BigDecimal maxPrice = priceNotMoreThan == null ? null : new BigDecimal(priceNotMoreThan);

        if (minPrice != null && maxPrice == null) {
            return caterings.stream()
                    .filter(catering -> minPrice.compareTo(catering.getServiceCost()) < 0 ||
                            minPrice.compareTo(catering.getServiceCost()) == 0)
                    .collect(Collectors.toList());
        } else if (minPrice == null) {
            return caterings.stream()
                    .filter(catering -> maxPrice.compareTo(catering.getServiceCost()) > 0 ||
                            maxPrice.compareTo(catering.getServiceCost()) == 0)
                    .collect(Collectors.toList());
        } else {
            return caterings.stream()
                    .filter(catering -> minPrice.compareTo(catering.getServiceCost()) < 0 ||
                            minPrice.compareTo(catering.getServiceCost()) == 0)
                    .filter(catering -> maxPrice.compareTo(catering.getServiceCost()) > 0 ||
                            maxPrice.compareTo(catering.getServiceCost()) == 0)
                    .collect(Collectors.toList());
        }
    }

    private ImmutableList<Location> getLocationsInSameCity(Catering savedCatering) {
        final String city = savedCatering.getCateringAddress().getCity();
        return locationService.findByCity(city);
    }


}
