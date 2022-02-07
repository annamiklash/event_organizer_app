package pjatk.pro.event_organizer_app.image.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import pjatk.pro.event_organizer_app.catering.model.Catering;

import javax.persistence.*;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "catering_image")
@Entity(name = "catering_image")
public class CateringImage extends Image implements Serializable {

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_catering")
    private Catering catering;
}