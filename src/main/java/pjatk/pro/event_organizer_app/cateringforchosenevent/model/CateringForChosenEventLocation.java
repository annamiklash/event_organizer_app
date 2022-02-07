package pjatk.pro.event_organizer_app.cateringforchosenevent.model;

import lombok.*;
import pjatk.pro.event_organizer_app.catering.model.Catering;
import pjatk.pro.event_organizer_app.location.locationforevent.model.LocationForEvent;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Builder
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Entity(name = "catering_for_chosen_location")
public class CateringForChosenEventLocation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_catering_for_chosen_location")
    private Long id;

    @Column(name = "order_time", nullable = false)
    private LocalTime time;

    @Column(name = "order_date", nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private String comment;

    @Column(nullable = false)
    private String confirmationStatus;

    private boolean isCateringOrderConfirmed;

    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_catering")
    private Catering catering;

    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_location_for_event")
    private LocationForEvent eventLocation;

    @EqualsAndHashCode.Exclude
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_catering_for_chosen_location")
    private Set<CateringOrderChoice> cateringOrder;

}
