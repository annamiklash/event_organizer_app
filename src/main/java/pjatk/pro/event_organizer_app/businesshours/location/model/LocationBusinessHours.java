package pjatk.pro.event_organizer_app.businesshours.location.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import pjatk.pro.event_organizer_app.businesshours.BusinessHours;
import pjatk.pro.event_organizer_app.location.model.Location;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@ToString(callSuper = true)
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Entity(name = "location_business_hours")
@Table(name = "location_business_hours")
public class LocationBusinessHours extends BusinessHours {

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_location")
    private Location location;

}
