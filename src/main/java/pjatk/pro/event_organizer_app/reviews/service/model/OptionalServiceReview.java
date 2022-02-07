package pjatk.pro.event_organizer_app.reviews.service.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import pjatk.pro.event_organizer_app.optional_service.model.OptionalService;
import pjatk.pro.event_organizer_app.reviews.Review;

import javax.persistence.*;

@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "service_review")
@Entity(name = "service_review")
public class OptionalServiceReview extends Review {

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_optional_service")
    private OptionalService optionalService;
}
