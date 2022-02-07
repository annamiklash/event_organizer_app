package pjatk.pro.event_organizer_app.event.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


@AllArgsConstructor
@RequiredArgsConstructor
@Data
@Builder
@Entity(name = "event_type")
public class EventType implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_event_type")
    private Long id;

    @Column(unique = true)
    private String type;

    @EqualsAndHashCode.Exclude
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_event_type")
    @JsonIgnore
    Set<OrganizedEvent> events = new HashSet<>();


}
