package pjatk.pro.event_organizer_app.businesshours.catering.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pjatk.pro.event_organizer_app.businesshours.catering.model.CateringBusinessHours;

@Repository
public interface CateringBusinessHoursRepository extends JpaRepository<CateringBusinessHours, Long> {
}
