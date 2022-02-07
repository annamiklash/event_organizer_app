package pjatk.pro.event_organizer_app.catering.model;

import lombok.*;
import pjatk.pro.event_organizer_app.cateringforchosenevent.model.CateringOrderChoice;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Builder
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Entity(name = "catering_item")
@Table(name = "catering_item")
public class CateringItem implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_catering_item")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private BigDecimal servingPrice;

    @Column(nullable = false)
    private boolean isVegan;

    @Column(nullable = false)
    private boolean isVegetarian;

    @Column(nullable = false)
    private boolean isGlutenFree;

    @Column(name = "type", nullable = false)
    private String itemType;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "modified_at", nullable = false)
    private LocalDateTime modifiedAt;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_catering", nullable = false)
    private Catering catering;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "item")
    private Set<CateringOrderChoice> cateringOrderChoices;

}


