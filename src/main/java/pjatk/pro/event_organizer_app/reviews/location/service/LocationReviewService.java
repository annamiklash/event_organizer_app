package pjatk.pro.event_organizer_app.reviews.location.service;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pjatk.pro.event_organizer_app.common.helper.TimestampHelper;
import pjatk.pro.event_organizer_app.common.mapper.PageableMapper;
import pjatk.pro.event_organizer_app.common.paginator.CustomPage;
import pjatk.pro.event_organizer_app.customer.model.Customer;
import pjatk.pro.event_organizer_app.customer.repository.CustomerRepository;
import pjatk.pro.event_organizer_app.exceptions.NotFoundException;
import pjatk.pro.event_organizer_app.location.model.Location;
import pjatk.pro.event_organizer_app.location.repository.LocationRepository;
import pjatk.pro.event_organizer_app.reviews.ReviewDto;
import pjatk.pro.event_organizer_app.reviews.location.model.LocationReview;
import pjatk.pro.event_organizer_app.reviews.location.repository.LocationReviewRepository;
import pjatk.pro.event_organizer_app.reviews.mapper.ReviewMapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class LocationReviewService {

    private final LocationReviewRepository locationReviewRepository;

    private final CustomerRepository customerRepository;

    private final LocationRepository locationRepository;

    private final TimestampHelper timestampHelper;

    public LocationReview leaveLocationReview(long id, long locationId, ReviewDto dto) {
        final Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer with id " + id + " does not exist"));

        final Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new NotFoundException("Location with id " + id + " DOES NOT EXIST"));

        final LocationReview locationReview = ReviewMapper.fromLocationReviewDto(dto);
        locationReview.setLocation(location);
        locationReview.setCustomer(customer);
        locationReview.setCreatedAt(timestampHelper.now());

        locationReviewRepository.save(locationReview);
        return locationReview;
    }

    public double getRating(long locationId) {
        final List<LocationReview> reviews = locationReviewRepository.getByLocationId(locationId);
        if (CollectionUtils.isEmpty(reviews)) {
            return 0;
        }
        final Double rating = reviews.stream()
                .collect(Collectors.averagingDouble(LocationReview::getStarRating));

        BigDecimal bd = new BigDecimal(Double.toString(rating));
        bd = bd.setScale(1, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public List<LocationReview> getByLocationId(CustomPage customPage, long id) {
        if (notExists(id)) {
            throw new NotFoundException("Location with id " + id + " does not exist");
        }
        final Pageable paging = PageableMapper.map(customPage);
        final Page<LocationReview> page = locationReviewRepository.getByLocationId(id, paging);

        return ImmutableList.copyOf(page.get()
                .collect(Collectors.toList()));
    }

    public List<LocationReview> getByLocationId(long id) {
        if (notExists(id)) {
            throw new NotFoundException("Location with id " + id + " does not exist");
        }

        return locationReviewRepository.getByLocationId(id);
    }

    private boolean notExists(long id) {
        return !locationReviewRepository.existsLocationReviewByLocation_Id(id);
    }

    public Long count(long locationId) {
        return locationReviewRepository.countLocationReviewsByLocation_Id(locationId);
    }

    public void delete(LocationReview locationReview) {
        locationReviewRepository.delete(locationReview);
    }
}
