package pjatk.pro.event_organizer_app.reviews.catering.service;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pjatk.pro.event_organizer_app.catering.model.Catering;
import pjatk.pro.event_organizer_app.catering.service.CateringService;
import pjatk.pro.event_organizer_app.common.helper.TimestampHelper;
import pjatk.pro.event_organizer_app.common.mapper.PageableMapper;
import pjatk.pro.event_organizer_app.common.paginator.CustomPage;
import pjatk.pro.event_organizer_app.customer.model.Customer;
import pjatk.pro.event_organizer_app.customer.service.CustomerService;
import pjatk.pro.event_organizer_app.exceptions.NotFoundException;
import pjatk.pro.event_organizer_app.reviews.ReviewDto;
import pjatk.pro.event_organizer_app.reviews.catering.model.CateringReview;
import pjatk.pro.event_organizer_app.reviews.catering.repository.CateringReviewRepository;
import pjatk.pro.event_organizer_app.reviews.mapper.ReviewMapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class CateringReviewService {

    private final CateringReviewRepository cateringReviewRepository;

    private final CustomerService customerService;

    private final CateringService cateringService;

    private final TimestampHelper timestampHelper;

    public CateringReview leaveCateringReview(long id, long cateringId, ReviewDto dto) {
        final Customer customer = customerService.get(id);

        final Catering catering = cateringService.get(cateringId);

        final CateringReview cateringReview = ReviewMapper.fromCateringReviewDto(dto);
        cateringReview.setCatering(catering);
        cateringReview.setCustomer(customer);
        cateringReview.setCreatedAt(timestampHelper.now());

        cateringReviewRepository.save(cateringReview);
        return cateringReview;
    }


    public List<CateringReview> getByCateringId(CustomPage customPage, long id) {
        if (notExists(id)) {
            throw new NotFoundException("Catering with id " + id + " does not exist");
        }
        final Pageable paging = PageableMapper.map(customPage);
        final Page<CateringReview> page = cateringReviewRepository.getByCateringId(id, paging);

        return ImmutableList.copyOf(page.get()
                .collect(Collectors.toList()));
    }

    public List<CateringReview> getByCateringId(long id) {
        if (notExists(id)) {
            throw new NotFoundException("Catering with id " + id + " does not exist");
        }
        return cateringReviewRepository.getByCateringId(id);
    }


    public double getRating(long cateringId) {
        final List<CateringReview> reviews = cateringReviewRepository.getByCateringId(cateringId);
        if (CollectionUtils.isEmpty(reviews)) {
            return 0;
        }
        final Double rating = reviews.stream()
                .collect(Collectors.averagingDouble(CateringReview::getStarRating));

        BigDecimal bd = new BigDecimal(Double.toString(rating));
        bd = bd.setScale(1, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public Long count(long cateringId) {
        return cateringReviewRepository.countAllByCatering_Id(cateringId);
    }

    private boolean notExists(long id) {
        return !cateringReviewRepository.existsCateringReviewByCatering_Id(id);
    }

}
