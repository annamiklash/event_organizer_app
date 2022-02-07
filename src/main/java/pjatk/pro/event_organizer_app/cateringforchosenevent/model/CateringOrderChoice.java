package pjatk.pro.event_organizer_app.cateringforchosenevent.model;

import lombok.*;
import pjatk.pro.event_organizer_app.catering.model.CateringItem;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.io.Serializable;

@Builder
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Entity(name = "catering_order_choice")
public class CateringOrderChoice implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_catering_order_choice")
    private Long id;

    @Min(1)
    @Column(name = "count", nullable = false)
    private int amount;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn(name = "id_catering_item")
    private CateringItem item;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_catering_for_chosen_location")
    private CateringForChosenEventLocation eventLocationCatering;

}
