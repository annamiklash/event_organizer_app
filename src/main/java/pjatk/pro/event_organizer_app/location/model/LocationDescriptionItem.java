package pjatk.pro.event_organizer_app.location.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Builder
@Getter
@ToString
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "description_item")
@Entity(name = "description_item")
public class LocationDescriptionItem implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "name")
    String id;

    @Column
    String description;

    @ToString.Exclude
    @ManyToMany(mappedBy = "descriptions")
    @JsonIgnore
    Set<Location> locations = new HashSet<>();


}
