package pjatk.pro.event_organizer_app.optional_service.model.kidperformer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import pjatk.pro.event_organizer_app.optional_service.model.OptionalService;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@DiscriminatorValue("KIDS PERFORMER")
public class KidsPerformer extends OptionalService {

    private String kidsPerformerType;

    private int ageFrom;

    private int ageTo;

}
