package pjatk.pro.event_organizer_app.optional_service.model.music.musicstyle;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import pjatk.pro.event_organizer_app.optional_service.model.OptionalService;

import javax.persistence.*;
import java.util.Set;

@Builder
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Entity(name = "music_style")
public class MusicStyle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_music_style")
    private Long id;

    private String name;

    @EqualsAndHashCode.Exclude
    @ManyToMany(mappedBy = "styles", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<OptionalService> optionalServices;

}
