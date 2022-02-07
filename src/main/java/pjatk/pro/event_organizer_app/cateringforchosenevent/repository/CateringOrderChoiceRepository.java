package pjatk.pro.event_organizer_app.cateringforchosenevent.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pjatk.pro.event_organizer_app.cateringforchosenevent.model.CateringOrderChoice;

import java.util.List;
import java.util.Optional;

@Repository
public interface CateringOrderChoiceRepository extends JpaRepository<CateringOrderChoice, Long> {

    @Query("SELECT ch FROM catering_order_choice ch " +
            "LEFT JOIN FETCH ch.item it " +
            "LEFT JOIN FETCH ch.eventLocationCatering lcat " +
            "LEFT JOIN FETCH lcat.catering cat " +
            "WHERE cat.id = :cateringId and lcat.id = :reservationId")
    List<CateringOrderChoice> getAll(@Param("cateringId") long cateringId, @Param("reservationId") long reservationId);

    @Query("SELECT ch FROM catering_order_choice ch " +
            "LEFT JOIN FETCH ch.item it " +
            "LEFT JOIN FETCH ch.eventLocationCatering lcat " +
            "LEFT JOIN FETCH lcat.catering cat " +
            "WHERE cat.id = :cateringId")
    List<CateringOrderChoice> getAll(@Param("cateringId") long cateringId);

    @Query("SELECT ch FROM catering_order_choice ch " +
            "LEFT JOIN FETCH ch.item it " +
            "LEFT JOIN FETCH ch.eventLocationCatering lcat " +
            "LEFT JOIN FETCH lcat.catering cat " +
            "WHERE ch.id = :orderChoiceId")
    Optional<CateringOrderChoice> findWithDetail(@Param("orderChoiceId") long orderChoiceId);

}
