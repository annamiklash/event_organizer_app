package pjatk.pro.event_organizer_app.availability.location.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pjatk.pro.event_organizer_app.availability.location.model.LocationAvailability;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationAvailabilityRepository extends JpaRepository<LocationAvailability, Long> {

    @Query(value = "select distinct la.* from location_availability la " +
            "where la.id_location=:locationId " +
            "AND la.date = CAST(:date as timestamp)", nativeQuery = true)
    List<LocationAvailability> find(@Param("locationId") Long locationId, @Param("date") String date);

    @Query(value = "select distinct la.* from location_availability la " +
            "where la.id_location=:locationId AND la.status = 'AVAILABLE' " +
            "AND la.date = CAST(:date as timestamp)", nativeQuery = true)
    List<LocationAvailability> findWithStatusAvailable(@Param("locationId") Long locationId, @Param("date") String date);

    @Query(value = "select distinct la.* from location_availability la " +
            "where la.id_location=:id AND la.status = 'AVAILABLE' " +
            "AND la.time_to = CAST(:timeTo as timestamp)", nativeQuery = true)
    Optional<LocationAvailability> findByLocationIdAndTimeTo(@Param("id") Long id, @Param("timeTo") String timeTo);

    @Query(value = "select distinct la.* from location_availability la " +
            "where la.id_location=:id AND la.status = 'AVAILABLE' " +
            "AND la.time_from = CAST(:timeFrom as timestamp)", nativeQuery = true)
    Optional<LocationAvailability> findByLocationIdAndTimeFrom(@Param("id") Long id, @Param("timeFrom") String timeFrom);


    @Query(value = "select distinct la.* from location_availability la " +
            "WHERE la.date = CAST(:date as date) " +
            "AND la.time_from = CAST(:timeFrom as timestamp) " +
            "AND la.time_to= CAST(:timeTo as timestamp)", nativeQuery = true)
    Optional<LocationAvailability> getByDateAndTime(@Param("date") String date, @Param("timeFrom") String timeFrom, @Param("timeTo") String timeTo);

    @Query(value = "select  la.* from location_availability la " +
            "where la.id_location=:locationId " +
            "AND la.date >= CAST(:dateFrom as timestamp) AND la.date <= CAST(:dateTo as timestamp) " +
            "order by la.date, la.time_from", nativeQuery = true)
    List<LocationAvailability> findByIdAndPeriodDate(@Param("locationId") long locationId, @Param("dateFrom") String dateFrom,@Param("dateTo") String dateTo);
}
