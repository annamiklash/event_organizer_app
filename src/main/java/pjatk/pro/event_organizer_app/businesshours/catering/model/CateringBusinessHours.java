package pjatk.pro.event_organizer_app.businesshours.catering.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import pjatk.pro.event_organizer_app.businesshours.BusinessHours;
import pjatk.pro.event_organizer_app.catering.model.Catering;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
@ToString(callSuper = true)
@AllArgsConstructor
@RequiredArgsConstructor
@Entity(name = "catering_business_hours")
@Table(name = "catering_business_hours")
public class CateringBusinessHours extends BusinessHours {

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_catering")
    private Catering catering;

}