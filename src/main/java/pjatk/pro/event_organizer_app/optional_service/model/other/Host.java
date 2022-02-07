package pjatk.pro.event_organizer_app.optional_service.model.other;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import pjatk.pro.event_organizer_app.optional_service.model.OptionalService;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
@ToString(callSuper = true)
@AllArgsConstructor
@Entity
@DiscriminatorValue("HOST")
public class Host extends OptionalService {
}
