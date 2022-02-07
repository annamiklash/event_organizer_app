package pjatk.pro.event_organizer_app.businesshours;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalTime;

@SuperBuilder(toBuilder = true)
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@MappedSuperclass
public abstract class BusinessHours {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_business_hours")
    private long id;

    @Column(nullable = false)
    private String day;

    @Column(nullable = false)
    private LocalTime timeFrom;

    @Column(nullable = false)
    private LocalTime timeTo;


}
