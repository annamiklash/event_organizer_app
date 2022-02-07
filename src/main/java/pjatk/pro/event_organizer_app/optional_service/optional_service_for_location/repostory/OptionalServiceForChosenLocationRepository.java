package pjatk.pro.event_organizer_app.optional_service.optional_service_for_location.repostory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pjatk.pro.event_organizer_app.optional_service.optional_service_for_location.model.OptionalServiceForChosenLocation;

import java.util.List;
import java.util.Optional;

@Repository
public interface OptionalServiceForChosenLocationRepository extends JpaRepository<OptionalServiceForChosenLocation, Long> {

    @Query("SELECT s FROM service_for_event s " +
            "LEFT JOIN FETCH s.locationForEvent sl " +
            "LEFT JOIN FETCH sl.event e " +
            "LEFT JOIN FETCH s.optionalService so " +
            "WHERE so.id = :serviceId AND e.id = :eventId")
    Optional<OptionalServiceForChosenLocation> findByServiceIdAndEventId(@Param("serviceId") long serviceId, @Param("eventId") long eventId);

    @Query("SELECT s FROM service_for_event s " +
            "LEFT JOIN FETCH s.locationForEvent sl " +
            "LEFT JOIN FETCH sl.event e " +
            "LEFT JOIN FETCH e.eventType et " +
            "LEFT JOIN FETCH e.customer ec " +
            "LEFT JOIN FETCH s.optionalService so " +
            "WHERE so.id = :serviceId AND s.confirmationStatus = :status")
    List<OptionalServiceForChosenLocation> findAllByServiceIdAndStatus(@Param("serviceId") long serviceId, @Param("status") String status);

    @Query("SELECT s FROM service_for_event s " +
            "LEFT JOIN FETCH s.locationForEvent sl " +
            "LEFT JOIN FETCH sl.event e " +
            "LEFT JOIN FETCH e.eventType et " +
            "LEFT JOIN FETCH e.customer ec " +
            "LEFT JOIN FETCH s.optionalService so " +
            "LEFT JOIN FETCH so.business b " +
            "WHERE b.id = :businessId AND s.confirmationStatus = :status")
    List<OptionalServiceForChosenLocation> findAllByBusinessIdAndStatus(@Param("businessId") long businessId, @Param("status") String status);

    @Query("SELECT s FROM service_for_event s " +
            "LEFT JOIN FETCH s.locationForEvent sl " +
            "LEFT JOIN FETCH sl.event e " +
            "LEFT JOIN FETCH e.eventType et " +
            "LEFT JOIN FETCH e.customer ec " +
            "LEFT JOIN FETCH s.optionalService so " +
            "LEFT JOIN FETCH so.availability " +
            "WHERE s.id = :serviceForLocationId")
    Optional<OptionalServiceForChosenLocation> getWithServiceAndEvent(@Param("serviceForLocationId")long serviceForLocationId);
}
