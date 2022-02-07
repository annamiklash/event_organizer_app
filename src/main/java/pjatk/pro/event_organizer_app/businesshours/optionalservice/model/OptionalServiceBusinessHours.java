package pjatk.pro.event_organizer_app.businesshours.optionalservice.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import pjatk.pro.event_organizer_app.businesshours.BusinessHours;
import pjatk.pro.event_organizer_app.optional_service.model.OptionalService;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Entity(name = "optional_service_business_hours")
@Table(name = "optional_service_business_hours")
public class OptionalServiceBusinessHours extends BusinessHours {

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_optional_service")
    private OptionalService optionalService;
}
