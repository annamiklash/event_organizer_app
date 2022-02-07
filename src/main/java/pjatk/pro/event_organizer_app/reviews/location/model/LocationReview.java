package pjatk.pro.event_organizer_app.reviews.location.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import pjatk.pro.event_organizer_app.location.model.Location;
import pjatk.pro.event_organizer_app.reviews.Review;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
@ToString(callSuper = true)
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "location_review")
@Entity(name = "location_review")
public class LocationReview extends Review {

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_location")
    private Location location;

}
