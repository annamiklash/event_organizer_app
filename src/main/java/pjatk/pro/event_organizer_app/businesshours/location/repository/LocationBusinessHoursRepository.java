package pjatk.pro.event_organizer_app.businesshours.location.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pjatk.pro.event_organizer_app.businesshours.location.model.LocationBusinessHours;

@Repository
public interface LocationBusinessHoursRepository extends JpaRepository<LocationBusinessHours, Long> {
}
