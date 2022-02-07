package pjatk.pro.event_organizer_app.reviews;

import lombok.*;
import lombok.experimental.SuperBuilder;
import pjatk.pro.event_organizer_app.customer.model.Customer;

import javax.persistence.*;
import java.time.LocalDateTime;

@SuperBuilder
@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@RequiredArgsConstructor
@MappedSuperclass
public abstract class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_review")
    private long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String comment;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private int starRating;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_customer", nullable = false)
    private Customer customer;
}
