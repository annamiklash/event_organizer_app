package pjatk.pro.event_organizer_app.businesshours.optionalservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pjatk.pro.event_organizer_app.businesshours.optionalservice.model.OptionalServiceBusinessHours;

@Repository
public interface OptionalServiceBusinessHoursRepository extends JpaRepository<OptionalServiceBusinessHours, Long> {
}
