package pjatk.pro.event_organizer_app.availability.location.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import pjatk.pro.event_organizer_app.availability.Availability;
import pjatk.pro.event_organizer_app.location.model.Location;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
@ToString(callSuper = true)
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "location_availability")
@Entity(name = "location_availability")
public class LocationAvailability extends Availability {

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_location")
    private Location location;
}
