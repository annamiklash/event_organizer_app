package pjatk.pro.event_organizer_app.catering.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pjatk.pro.event_organizer_app.catering.model.Catering;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface CateringRepository extends JpaRepository<Catering, Long> {

    List<Catering> findByCateringAddress_City(String city);

    @Query("SELECT distinct c FROM catering AS c " +
            "LEFT JOIN catering_image ci on ci.catering.id = c.id " +
            "JOIN c.business cb " +
            "left join users u on u.id = cb.id " +
            "WHERE (:keyword = '' or (c.name LIKE %:keyword% OR c.description LIKE %:keyword%)) " +
            "AND u.isActive = true")
    Page<Catering> findAllWithKeyword(Pageable pageable, @Param("keyword") String keyword);

    @Query("SELECT distinct c from catering c " +
            "LEFT JOIN FETCH c.business b " +
            "LEFT JOIN catering_image ci on ci.catering.id = c.id " +
            "left join fetch c.cateringItems it " +
            "left join fetch c.cateringBusinessHours cbh " +
            "left join fetch c.locations cl " +
            "WHERE c.id = :id")
    Optional<Catering> findByIdWithDetail(@Param("id") long id);

    @Query("SELECT distinct c from catering c " +
            "LEFT JOIN catering_image ci on ci.catering.id = c.id " +
            "LEFT JOIN c.locations l " +
            "left join c.cuisines cu " +
            "left join c.cateringBusinessHours bh " +
            "left join c.cateringAddress ca " +
            "WHERE (:city like '' or ca.city = :city) " +
            "AND ((:cuisines) is null or cu.id IN (:cuisines)) " +
            "AND (:locationId is null or l.id = :locationId)")
    List<Catering> search(@Param("cuisines") Set<Long> cuisines, @Param("city") String city, @Param("locationId") Long locationId);

    @Query("SELECT cat FROM catering cat " +
            "LEFT JOIN FETCH cat.cateringBusinessHours bh " +
            "WHERE cat.id = :cateringId")
    Optional<Catering> getWithBusinessHours(@Param("cateringId") long cateringId);

    List<Catering> findAllByBusiness_Id(long id);

    @Query("SELECT distinct c FROM catering c " +
            "LEFT JOIN FETCH c.reviews r " +
            "LEFT JOIN catering_image ci on ci.catering.id = c.id " +
            "LEFT JOIN FETCH c.cateringAddress cad " +
            "LEFT JOIN FETCH c.cateringBusinessHours bh " +
            "LEFT JOIN FETCH c.locations cl " +
            "LEFT JOIN FETCH c.cuisines cc " +
            "LEFT JOIN FETCH c.cateringForChosenEventLocations cfel " +
            "LEFT JOIN FETCH cfel.eventLocation el " +
            "LEFT JOIN FETCH el.event " +
            "LEFT JOIN FETCH c.cateringItems ci " +
            "LEFT JOIN FETCH cfel.cateringOrder co " +
            "WHERE c.id = :cateringId")
    Optional<Catering> findAllCateringInformation(@Param("cateringId") long cateringId);

    @Query("SELECT distinct c FROM catering c " +
            "LEFT JOIN catering_image ci on ci.catering.id = c.id " +
            "LEFT JOIN FETCH c.locations l " +
            "WHERE l.id = :locationId")
    List<Catering> findAllByLocationId(@Param("locationId") long locationId);

    @Query("SELECT count(c) FROM catering AS c " +
            "JOIN c.business cb " +
            "left join users u on u.id = cb.id " +
            "WHERE c.name LIKE %:keyword% " +
            "OR c.description LIKE %:keyword% " +
            "AND u.isActive = true")
    Long countAll(@Param("keyword") String keyword);

    @Query("SELECT distinct c FROM catering c " +
            "LEFT JOIN catering_image ci on ci.catering.id = c.id " +
            "WHERE c.id = :cateringId")
    Optional<Catering> findWithImages(@Param("cateringId") long cateringId);

}
