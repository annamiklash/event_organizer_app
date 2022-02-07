package pjatk.pro.event_organizer_app.reviews.service.service;

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
import pjatk.pro.event_organizer_app.optional_service.model.OptionalService;
import pjatk.pro.event_organizer_app.optional_service.repository.OptionalServiceRepository;
import pjatk.pro.event_organizer_app.reviews.ReviewDto;
import pjatk.pro.event_organizer_app.reviews.mapper.ReviewMapper;
import pjatk.pro.event_organizer_app.reviews.service.model.OptionalServiceReview;
import pjatk.pro.event_organizer_app.reviews.service.repository.OptionalServiceReviewRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class OptionalServiceReviewService {

    private final OptionalServiceReviewRepository serviceReviewRepository;

    private final CustomerRepository customerRepository;

    private final OptionalServiceRepository optionalServiceRepository;

    private final TimestampHelper timestampHelper;

    public OptionalServiceReview leaveServiceReview(long id, long serviceId, ReviewDto dto) {
        final Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer with id " + id + " does not exist"));

        final OptionalService optionalService = optionalServiceRepository.findById(serviceId)
                .orElseThrow(() -> new NotFoundException("Service with id " + serviceId + " does not exist"));

        final OptionalServiceReview optionalServiceReview = ReviewMapper.fromServiceReviewDto(dto);
        optionalServiceReview.setOptionalService(optionalService);
        optionalServiceReview.setCustomer(customer);
        optionalServiceReview.setCreatedAt(timestampHelper.now());

        serviceReviewRepository.save(optionalServiceReview);
        return optionalServiceReview;
    }

    public List<OptionalServiceReview> getByServiceId(CustomPage customPage, long id) {
        if (!exists(id)) {
            throw new NotFoundException("Catering with id " + id + " does not exist");
        }
        final Pageable paging = PageableMapper.map(customPage);
        final Page<OptionalServiceReview> page = serviceReviewRepository.getByServiceId(id, paging);

        return ImmutableList.copyOf(page.get()
                .collect(Collectors.toList()));
    }

    public double getRating(long serviceId) {
        final List<OptionalServiceReview> reviews = serviceReviewRepository.getByServiceId(serviceId);
        if (CollectionUtils.isEmpty(reviews)) {
            return 0;
        }
        final Double rating = reviews.stream()
                .collect(Collectors.averagingDouble(OptionalServiceReview::getStarRating));

        BigDecimal bd = new BigDecimal(Double.toString(rating));
        bd = bd.setScale(1, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public void delete(OptionalServiceReview optionalServiceReview) {
        serviceReviewRepository.delete(optionalServiceReview);
    }

    public boolean exists(long id) {
        return serviceReviewRepository.existsByOptionalService_Id(id);
    }

    public Long count(long id) {
        return serviceReviewRepository.countAllByOptionalService_Id(id);
    }
}