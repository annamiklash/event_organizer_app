package pjatk.pro.event_organizer_app.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pjatk.pro.event_organizer_app.event.model.OrganizedEvent;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrganizedEventRepository extends JpaRepository<OrganizedEvent, Long> {

    @Query("SELECT oe from organized_event oe " +
            "left join fetch oe.eventType et " +
            "left join fetch oe.customer c " +
            "left join users u on u.id = c.id " +
            "left join fetch oe.locationForEvent lfe " +
            "left join fetch lfe.cateringsForEventLocation cfl " +
            "left join fetch cfl.catering cflc " +
            "left join fetch lfe.location l " +
            "left join fetch l.locationAddress la " +
            "left join fetch oe.guests g " +
            "WHERE oe.id = :id " +
            "AND oe.customer.id = :customerId " +
            "AND lfe.confirmationStatus not like 'CANCELLED'")
    Optional<OrganizedEvent> getWithAllInformationForSendingInvitations(@Param("id") long id, @Param("customerId") long customerId);

    boolean existsOrganizedEventByIdAndCustomer_Id(long eventId, long customerId);

    @Query("SELECT distinct oe FROM organized_event oe " +
            "LEFT JOIN FETCH oe.customer c " +
            "LEFT JOIN FETCH oe.eventType et " +
            "LEFT JOIN FETCH oe.locationForEvent lfe " +
            "LEFT JOIN FETCH lfe.location l " +
            "LEFT JOIN location_image li on li.location.id = l.id " +
            "WHERE c.id = :customerId")
    List<OrganizedEvent> findAllByCustomer_Id(@Param("customerId") long customerId);

    @Query("SELECT distinct oe FROM organized_event oe " +
            "LEFT JOIN FETCH oe.customer c " +
            "LEFT JOIN FETCH oe.eventType et " +
            "LEFT JOIN FETCH oe.locationForEvent lfe " +
            "LEFT JOIN FETCH lfe.location l " +
            "LEFT JOIN location_image li on li.location.id = l.id " +
            "WHERE c.id = :customerId " +
            "AND oe.eventStatus LIKE 'FINISHED'")
    List<OrganizedEvent> findAllFinished(@Param("customerId") long customerId);

    @Query("SELECT distinct oe FROM organized_event oe " +
            "LEFT JOIN FETCH oe.customer c " +
            "LEFT JOIN FETCH oe.eventType et " +
            "LEFT JOIN FETCH oe.locationForEvent lfe " +
            "LEFT JOIN FETCH lfe.location l " +
            "LEFT JOIN location_image li on li.location.id = l.id " +
            "WHERE c.id = :customerId " +
            "AND oe.eventStatus NOT LIKE 'FINISHED' AND oe.eventStatus NOT LIKE 'CANCELLED'")
    List<OrganizedEvent> findAllCurrent(@Param("customerId") long customerId);

    @Query("SELECT oe from organized_event oe " +
            "LEFT JOIN FETCH oe.locationForEvent lfe " +
            "LEFT JOIN FETCH lfe.location l " +
            "LEFT JOIN FETCH l.caterings cat " +
            "WHERE oe.id = :eventId")
    Optional<OrganizedEvent> getWithLocation(@Param("eventId") long eventId);

    @Query("SELECT distinct oe FROM organized_event oe " +
            "LEFT JOIN FETCH oe.guests g " +
            "LEFT JOIN FETCH oe.customer cust " +
            "LEFT JOIN FETCH oe.locationForEvent oel " +
            "LEFT JOIN FETCH oel.location l " +
            "LEFT JOIN location_image li on li.location.id = l.id " +
            "LEFT JOIN FETCH oe.eventType oet " +
            "LEFT JOIN FETCH oel.cateringsForEventLocation cat " +
            "LEFT JOIN FETCH cat.catering c " +
            "LEFT JOIN catering_image ci on ci.catering.id = c.id " +
            "LEFT JOIN FETCH cat.cateringOrder co " +
            "LEFT JOIN FETCH co.item i " +
            "LEFT JOIN FETCH oel.services serv " +
            "LEFT JOIN FETCH serv.optionalService os " +
            "LEFT JOIN FETCH os.styles s " +
            "LEFT JOIN optional_service_image osi on osi.service.id = serv.id " +
            "WHERE oe.id = :orgEventId AND cust.id = :customerId")
    Optional<OrganizedEvent> getWithDetail(@Param("orgEventId") long orgEventId,
                                           @Param("customerId") long customerId);

    @Query("SELECT distinct oe FROM organized_event oe " +
            "LEFT JOIN FETCH oe.guests g " +
            "LEFT JOIN FETCH oe.customer cust " +
            "LEFT JOIN FETCH oe.locationForEvent oel " +
            "LEFT JOIN FETCH oel.location l " +
            "LEFT JOIN location_image li on li.location.id = l.id " +
            "LEFT JOIN FETCH oe.eventType oet " +
            "LEFT JOIN FETCH oel.cateringsForEventLocation cat " +
            "LEFT JOIN FETCH cat.catering c " +
            "LEFT JOIN catering_image ci on ci.catering.id = c.id " +
            "LEFT JOIN FETCH cat.cateringOrder co " +
            "LEFT JOIN FETCH co.item i " +
            "LEFT JOIN FETCH oel.services serv " +
            "LEFT JOIN optional_service_image osi on osi.service.id = serv.id " +
            "WHERE oe.id = :orgEventId ")
    Optional<OrganizedEvent> getWithDetail(@Param("orgEventId") long orgEventId);

    @Query("SELECT distinct oe FROM organized_event oe " +
            "LEFT JOIN FETCH oe.customer cust " +
            "LEFT JOIN FETCH oe.locationForEvent oel " +
            "LEFT JOIN FETCH oe.eventType oet " +
            "LEFT JOIN FETCH oel.cateringsForEventLocation cat " +
            "LEFT JOIN FETCH cat.catering c " +
            "LEFT JOIN FETCH cat.cateringOrder co " +
            "LEFT JOIN FETCH co.item i " +
            "LEFT JOIN FETCH oel.services serv ")
    Optional<OrganizedEvent> getEventSummary(long orgEventId);

}
