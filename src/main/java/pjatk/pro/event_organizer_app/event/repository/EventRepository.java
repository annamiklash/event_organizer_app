package pjatk.pro.event_organizer_app.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pjatk.pro.event_organizer_app.event.model.EventType;

import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<EventType, Long> {

    Optional<EventType> findByType(String name);
}
