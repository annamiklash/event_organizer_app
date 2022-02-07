package pjatk.pro.event_organizer_app.image.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import pjatk.pro.event_organizer_app.location.model.Location;

import javax.persistence.*;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "location_image")
@Entity(name = "location_image")
public class LocationImage extends Image implements Serializable {

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_location")
    private Location location;

}
