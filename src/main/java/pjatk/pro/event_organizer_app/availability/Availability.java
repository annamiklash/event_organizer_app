package pjatk.pro.event_organizer_app.availability;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@SuperBuilder
@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@RequiredArgsConstructor
@MappedSuperclass
public abstract class Availability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_availability")
    private long id;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "time_from", nullable = false)
    private LocalDateTime timeFrom;

    @Column(name = "time_to", nullable = false)
    private LocalDateTime timeTo;

    @Column(nullable = false)
    private String status;
}
