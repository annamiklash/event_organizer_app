package pjatk.pro.event_organizer_app.availability.optionalservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pjatk.pro.event_organizer_app.availability.optionalservice.model.OptionalServiceAvailability;

import java.util.List;
import java.util.Optional;

@Repository
public interface OptionalServiceAvailabilityRepository extends JpaRepository<OptionalServiceAvailability, Long> {

    @Query(value = "select distinct os.* " +
            "from optional_service_availability os " +
            "where os.id_optional_service=:id " +
            "AND os.date = CAST(:date as timestamp)", nativeQuery = true)
    List<OptionalServiceAvailability> findAvailabilitiesByServiceIdAndDate(@Param("id") long id, @Param("date") String date);

    @Query(value = "select distinct sa.* " +
            "from optional_service_availability sa " +
            "where sa.id_optional_service=:id AND sa.status = 'AVAILABLE' " +
            "AND sa.date = CAST(:date as timestamp)", nativeQuery = true)
    List<OptionalServiceAvailability> findByDate(@Param("id") Long id, @Param("date") String date);

    @Query(value = "select distinct la.* from optional_service_availability la " +
            "where la.id_optional_service=:serviceId AND la.status = 'AVAILABLE' " +
            "AND la.time_to = CAST(:timeTo as timestamp)", nativeQuery = true)
    Optional<OptionalServiceAvailability> findByServiceIdAndTimeTo(@Param("serviceId") Long serviceId, @Param("timeTo") String timeTo);

    @Query(value = "select distinct la.* from optional_service_availability la " +
            "where la.id_optional_service=:serviceId AND la.status = 'AVAILABLE' " +
            "AND la.time_from = CAST(:timeFrom as timestamp)", nativeQuery = true)
    Optional<OptionalServiceAvailability> findByServiceIdAndTimeFrom(@Param("serviceId") Long serviceId, @Param("timeFrom") String timeFrom);

    @Query(value = "select distinct la.* from optional_service_availability la " +
            "WHERE la.date = CAST(:date as date) " +
            "AND la.time_from = CAST(:timeFrom as timestamp) " +
            "AND la.time_to= CAST(:timeTo as timestamp)", nativeQuery = true)
    Optional<OptionalServiceAvailability> getByDateAndTime(@Param("date") String date, @Param("timeFrom") String timeFrom, @Param("timeTo") String timeTo);

    @Query(value = "select os.* " +
            "from optional_service_availability os " +
            "where os.id_optional_service=:serviceId " +
            "AND os.date >= CAST(:dateFrom as timestamp) AND os.date <= CAST(:dateTo as timestamp) " +
            "order by os.date, os.time_from", nativeQuery = true)
    List<OptionalServiceAvailability> findByIdAndPeriodDate(@Param("serviceId") long serviceId, @Param("dateFrom") String dateFrom, @Param("dateTo") String dateTo);
}
