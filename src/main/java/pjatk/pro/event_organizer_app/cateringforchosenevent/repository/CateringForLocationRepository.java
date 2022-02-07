package pjatk.pro.event_organizer_app.cateringforchosenevent.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pjatk.pro.event_organizer_app.cateringforchosenevent.model.CateringForChosenEventLocation;

import java.util.List;
import java.util.Optional;

@Repository
public interface CateringForLocationRepository extends JpaRepository<CateringForChosenEventLocation, Long> {

    @Query("SELECT c FROM catering_for_chosen_location c " +
            "LEFT JOIN FETCH c.eventLocation cl " +
            "LEFT JOIN FETCH cl.event e " +
            "LEFT JOIN FETCH c.catering cat " +
            "WHERE e.id = :eventId AND cat.id = :cateringId")
    Optional<CateringForChosenEventLocation> findByCateringIdAndEventId(@Param("cateringId") long cateringId, @Param("eventId") Long eventId);

    @Query("SELECT c FROM catering_for_chosen_location c " +
            "LEFT JOIN FETCH c.eventLocation cl " +
            "LEFT JOIN FETCH cl.event e " +
            "LEFT JOIN FETCH e.eventType et " +
            "LEFT JOIN FETCH e.customer ec " +
            "LEFT JOIN FETCH c.catering cat " +
            "WHERE cat.id = :cateringId AND c.confirmationStatus = :status")
    List<CateringForChosenEventLocation> findAllByCateringIdAndStatus(@Param("cateringId") long cateringId, @Param("status") String status);

    @Query("SELECT cfle from catering_for_chosen_location cfle " +
            "LEFT JOIN FETCH cfle.catering c " +
            "LEFT JOIN FETCH cfle.eventLocation l " +
            "LEFT JOIN FETCH l.event e " +
            "WHERE cfle.id = :cateringForEventId")
    Optional<CateringForChosenEventLocation> getWithCateringAndEvent(@Param("cateringForEventId")long cateringForEventId);

    @Query("SELECT distinct c FROM catering_for_chosen_location c " +
            "LEFT JOIN FETCH c.cateringOrder co " +
            "LEFT JOIN FETCH co.item i " +
            "LEFT JOIN FETCH c.eventLocation cl " +
            "LEFT JOIN FETCH cl.location l " +
            "LEFT JOIN FETCH l.business b " +
            "LEFT JOIN FETCH cl.event e " +
            "LEFT JOIN FETCH c.catering cat " +
            "WHERE b.id = :businessId AND c.confirmationStatus = :status")
    List<CateringForChosenEventLocation> findAllByBusinessIdAndStatus(@Param("businessId") long businessId, @Param("status") String status);

    @Query("SELECT c FROM catering_for_chosen_location c " +
            "LEFT JOIN c.cateringOrder co " +
            "WHERE c.id = :reservationId")
    Optional<CateringForChosenEventLocation> getWithCateringOrder(@Param("reservationId") long reservationId);
}
