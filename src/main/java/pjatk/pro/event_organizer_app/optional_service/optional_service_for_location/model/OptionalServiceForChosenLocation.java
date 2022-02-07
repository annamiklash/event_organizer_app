package pjatk.pro.event_organizer_app.optional_service.optional_service_for_location.model;

import lombok.*;
import pjatk.pro.event_organizer_app.location.locationforevent.model.LocationForEvent;
import pjatk.pro.event_organizer_app.optional_service.model.OptionalService;

import javax.persistence.*;
import java.time.LocalTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "service_for_event")
@Table(name = "service_for_event")
public class OptionalServiceForChosenLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_chosen_service_for_event")
    private Long id;

    @Column(nullable = false)
    private LocalTime timeFrom;

    @Column(nullable = false)
    private LocalTime timeTo;

    private String comment;

    @Column(nullable = false)
    private String confirmationStatus;

    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_location_for_event")
    private LocationForEvent locationForEvent;

    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_optional_service")
    private OptionalService optionalService;

}
