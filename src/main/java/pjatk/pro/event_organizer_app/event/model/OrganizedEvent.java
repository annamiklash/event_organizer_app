package pjatk.pro.event_organizer_app.event.model;

import lombok.*;
import pjatk.pro.event_organizer_app.customer.guest.model.Guest;
import pjatk.pro.event_organizer_app.customer.model.Customer;
import pjatk.pro.event_organizer_app.location.locationforevent.model.LocationForEvent;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@With
@Entity(name = "organized_event")
public class OrganizedEvent implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_organized_event")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "event_date", nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @Column(nullable = false)
    private String eventStatus;

    @Min(1)
    @Column(nullable = false)
    private int guestCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_event_type", nullable = false)
    private EventType eventType;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "modified_at", nullable = false)
    private LocalDateTime modifiedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_customer")
    private Customer customer;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_organized_event")
    private Set<LocationForEvent> locationForEvent;

    @EqualsAndHashCode.Exclude
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "event_guest",
            joinColumns = @JoinColumn(name = "id_organized_event"),
            inverseJoinColumns = @JoinColumn(name = "id_guest"))
    private Set<Guest> guests;

}
