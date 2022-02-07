package pjatk.pro.event_organizer_app.reviews.catering.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import pjatk.pro.event_organizer_app.catering.model.Catering;
import pjatk.pro.event_organizer_app.reviews.Review;

import javax.persistence.*;

@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "catering_review")
@Entity(name = "catering_review")
public class CateringReview extends Review {

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_catering")
    private Catering catering;

}
