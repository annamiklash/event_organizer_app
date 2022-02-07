package pjatk.pro.event_organizer_app.optional_service.model.music;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import pjatk.pro.event_organizer_app.optional_service.model.OptionalService;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
@AllArgsConstructor
@Entity
@DiscriminatorValue("DJ")
public class DJ extends OptionalService {


}
