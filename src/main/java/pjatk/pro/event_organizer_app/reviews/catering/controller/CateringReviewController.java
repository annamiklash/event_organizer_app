package pjatk.pro.event_organizer_app.reviews.catering.controller;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pjatk.pro.event_organizer_app.common.paginator.CustomPage;
import pjatk.pro.event_organizer_app.reviews.ReviewDto;
import pjatk.pro.event_organizer_app.reviews.catering.model.CateringReview;
import pjatk.pro.event_organizer_app.reviews.catering.service.CateringReviewService;
import pjatk.pro.event_organizer_app.reviews.mapper.ReviewMapper;
import pjatk.pro.event_organizer_app.table.TableDto;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("api/reviews/catering")
public class CateringReviewController {

    private final CateringReviewService cateringReviewService;

    @RequestMapping(
            path = "allowed/all",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TableDto<ReviewDto>> listAllByCateringId(@RequestParam(defaultValue = "0") Integer pageNo,
                                                                   @RequestParam(defaultValue = "50") Integer pageSize,
                                                                   @RequestParam(defaultValue = "id") String sortBy,
                                                                   @RequestParam(defaultValue = "asc") String order,
                                                                   @RequestParam long cateringId) {
        final CustomPage customPage = CustomPage.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .sortBy(sortBy)
                .order(order)
                .build();
        final List<CateringReview> reviewList = cateringReviewService.getByCateringId(customPage, cateringId);

        final Long count = cateringReviewService.count(cateringId);

        final ImmutableList<ReviewDto> result = reviewList.stream()
                .map(ReviewMapper::toDtoWithCustomerAvatar)
                .collect(ImmutableList.toImmutableList());

        return ResponseEntity.ok(new TableDto<>(TableDto.MetaDto.builder().pageNo(pageNo).pageSize(pageSize).sortBy(sortBy).total(count).build(), result));
    }


    @PreAuthorize("hasAuthority('CUSTOMER')")
    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReviewDto> reviewCatering(@RequestParam long id,
                                                    @RequestParam long cateringId,
                                                    @Valid @RequestBody ReviewDto dto) {

        final CateringReview review = cateringReviewService.leaveCateringReview(id, cateringId, dto);
        return ResponseEntity.ok(ReviewMapper.toDto(review));
    }

}
