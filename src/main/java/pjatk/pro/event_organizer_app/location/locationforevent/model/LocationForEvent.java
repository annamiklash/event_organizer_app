package pjatk.pro.event_organizer_app.location.locationforevent.model;

import lombok.*;
import pjatk.pro.event_organizer_app.cateringforchosenevent.model.CateringForChosenEventLocation;
import pjatk.pro.event_organizer_app.event.model.OrganizedEvent;
import pjatk.pro.event_organizer_app.location.model.Location;
import pjatk.pro.event_organizer_app.optional_service.optional_service_for_location.model.OptionalServiceForChosenLocation;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.time.LocalTime;
import java.util.Set;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@With
@Entity(name = "location_for_event")
public class LocationForEvent implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_location_for_event")
    private Long id;

    @Column(name = "time_from")
    private LocalTime timeFrom;

    @Column(name = "time_to")
    private LocalTime timeTo;

    @Min(1)
    @Column(name = "guests")
    private int guestCount;

    @Column(nullable = false)
    private String confirmationStatus;

    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_location")
    private Location location;

    @ToString.Exclude
    @ManyToOne(
            fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "id_organized_event")
    private OrganizedEvent event;

    @EqualsAndHashCode.Exclude
    @OneToMany(
            cascade = CascadeType.REMOVE,
            fetch = FetchType.LAZY,
            orphanRemoval = true)
    @JoinColumn(name = "id_location_for_event")
    private Set<CateringForChosenEventLocation> cateringsForEventLocation;

    @EqualsAndHashCode.Exclude
    @OneToMany(
            cascade = CascadeType.REMOVE,
            fetch = FetchType.LAZY,
            orphanRemoval = true)
    @JoinColumn(name = "id_location_for_event")
    private Set<OptionalServiceForChosenLocation> services;


}
