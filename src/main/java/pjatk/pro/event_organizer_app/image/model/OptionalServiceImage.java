package pjatk.pro.event_organizer_app.image.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import pjatk.pro.event_organizer_app.optional_service.model.OptionalService;

import javax.persistence.*;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "optional_service_image")
@Entity(name = "optional_service_image")
public class OptionalServiceImage extends Image implements Serializable {

    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_optional_service")
    private OptionalService service;
}
