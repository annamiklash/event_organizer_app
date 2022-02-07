package pjatk.pro.event_organizer_app.customer.guest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import pjatk.pro.event_organizer_app.customer.model.Customer;
import pjatk.pro.event_organizer_app.event.model.OrganizedEvent;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Builder
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "guest")
@Entity(name = "guest")
public class Guest implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_guest")
    Long id;

    @Column( nullable = false)
    String firstName;

    @Column(name = "last_name")
    String lastName;

    @Column
    String email;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "modified_at", nullable = false)
    private LocalDateTime modifiedAt;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_customer")
    private Customer customer;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToMany(mappedBy = "guests", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<OrganizedEvent> organizedEvents;
}
