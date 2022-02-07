package pjatk.pro.event_organizer_app.reviews.mapper;

import lombok.experimental.UtilityClass;
import pjatk.pro.event_organizer_app.customer.mapper.CustomerMapper;
import pjatk.pro.event_organizer_app.reviews.Review;
import pjatk.pro.event_organizer_app.reviews.ReviewDto;
import pjatk.pro.event_organizer_app.reviews.catering.model.CateringReview;
import pjatk.pro.event_organizer_app.reviews.location.model.LocationReview;
import pjatk.pro.event_organizer_app.reviews.service.model.OptionalServiceReview;

@UtilityClass
public class ReviewMapper {

    public LocationReview fromLocationReviewDto(ReviewDto dto) {
        return LocationReview.builder()
                .title(dto.getTitle())
                .comment(dto.getComment())
                .starRating(dto.getStarRating())
                .build();
    }

    public CateringReview fromCateringReviewDto(ReviewDto dto) {
        return CateringReview.builder()
                .title(dto.getTitle())
                .comment(dto.getComment())
                .starRating(dto.getStarRating())
                .build();
    }

    public OptionalServiceReview fromServiceReviewDto(ReviewDto dto) {
        return OptionalServiceReview.builder()
                .title(dto.getTitle())
                .comment(dto.getComment())
                .starRating(dto.getStarRating())
                .build();
    }

    public ReviewDto toDto(Review locationReview) {
        return ReviewDto.builder()
                .id(locationReview.getId())
                .title(locationReview.getTitle())
                .comment(locationReview.getComment())
                .starRating(locationReview.getStarRating())
                .customer(CustomerMapper.toDto(locationReview.getCustomer()))
                .build();
    }

    public ReviewDto toDtoWithCustomerAvatar(Review locationReview) {
        final ReviewDto dto = ReviewDto.builder()
                .id(locationReview.getId())
                .title(locationReview.getTitle())
                .comment(locationReview.getComment())
                .starRating(locationReview.getStarRating())
                .build();

        if (locationReview.getCustomer().getAvatar() == null) {
            dto.setCustomer(CustomerMapper.toDto(locationReview.getCustomer()));
        } else {
            dto.setCustomer(CustomerMapper.toDtoWIthAvatar(locationReview.getCustomer()));
        }

        return  dto;
    }

}
