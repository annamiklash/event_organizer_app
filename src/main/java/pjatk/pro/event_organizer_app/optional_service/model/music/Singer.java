package pjatk.pro.event_organizer_app.optional_service.model.music;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import pjatk.pro.event_organizer_app.optional_service.model.OptionalService;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@Entity
@DiscriminatorValue("SINGER")
public class Singer extends OptionalService {


}
