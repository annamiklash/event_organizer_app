package pjatk.pro.event_organizer_app.optional_service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pjatk.pro.event_organizer_app.optional_service.model.OptionalService;

import java.util.List;
import java.util.Optional;

@Repository
public interface OptionalServiceRepository extends JpaRepository<OptionalService, Long> {

    @Query("SELECT distinct os from optional_service os " +
            "LEFT JOIN optional_service_image si on si.service.id = os.id " +
            "JOIN os.business ob " +
            "LEFT JOIN users u on ob.id = u.id " +
            "WHERE (:keyword = '' or (os.alias LIKE %:keyword% OR os.type LIKE %:keyword%)) " +
            "AND u.isActive = true")
    Page<OptionalService> findAllWithKeyword(Pageable paging, @Param("keyword") String keyword);

    @Query(value = "SELECT distinct os.* from optional_service os " +
            "LEFT JOIN optional_service_image osi on os.id_optional_service = osi.id_optional_service " +
            "left join optional_service_availability sa on sa.id_optional_service = os.id_optional_service " +
            "LEFT JOIN address a on os.id_service_address = a.id_address " +
            "LEFT JOIN business b on b.id_user = os.id_business " +
            "LEFT JOIN users u on u.id_user = b.id_user " +
            "WHERE sa.date = CAST(:date as timestamp) " +
            "AND (:type like '' or os.type = :type) " +
            "AND (:city like '' or a.city = :city) " +
            "AND u.is_active = true", nativeQuery = true)
    List<OptionalService> search(@Param("date") String date, @Param("type") String type, @Param("city") String city);

    @Query(value = "SELECT distinct os.* from optional_service os " +
            "LEFT JOIN optional_service_image osi on os.id_optional_service = osi.id_optional_service " +
            "left join optional_service_availability sa on sa.id_optional_service = os.id_optional_service " +
            "LEFT JOIN address a on os.id_service_address = a.id_address " +
            "LEFT JOIN business b on b.id_user = os.id_business " +
            "LEFT JOIN users u on u.id_user = b.id_user " +
            "WHERE (:type like '' or os.type = :type) " +
            "AND (:city like '' or a.city = :city) " +
            "AND u.is_active = true", nativeQuery = true)
    List<OptionalService> searchByType(@Param("type") String type, @Param("city") String city);

    @Query(value = "SELECT distinct os.* from optional_service os " +
            "LEFT JOIN optional_service_availability osa ON osa.id_optional_service = os.id_optional_service " +
            "WHERE os.id_optional_service = :serviceId AND osa.date = CAST(:date as timestamp) " +
            "AND (:timeFrom is null or osa.time_from <= CAST(:timeFrom as timestamp)) " +
            "AND (:timeTo is null or osa.time_to >= CAST(:timeTo as timestamp))", nativeQuery = true)
    Optional<OptionalService> available(@Param("serviceId") long serviceId, @Param("date") String date, @Param("timeFrom") String timeFrom, @Param("timeTo") String timeTo);

    @Query("SELECT os from optional_service os " +
            "LEFT JOIN optional_service_image si on si.service.id = os.id " +
            "LEFT JOIN FETCH os.serviceAddress sa " +
            "LEFT JOIN FETCH os.styles ms " +
            "LEFT JOIN FETCH os.reviews r " +
            "LEFT JOIN FETCH os.availability osa " +
            "LEFT JOIN FETCH os.optionalServiceBusinessHours bh " +
            "LEFT JOIN FETCH os.serviceForLocation sfl " +
            "LEFT JOIN FETCH sfl.locationForEvent lfe " +
            "LEFT JOIN FETCH lfe.event " +
            "WHERE os.id = :serviceId")
    Optional<OptionalService> getAllServiceInformation(@Param("serviceId") long serviceId);

    @Query("SELECT distinct concat(a.city, ', ', a.country)  " +
            "FROM address a " +
            "left join optional_service os on os.serviceAddress.id = a.id")
    List<String> findDistinctCities();

    @Query("SELECT os from optional_service os " +
            "LEFT JOIN FETCH os.styles ms " +
            "LEFT JOIN FETCH os.availability osa " +
            "LEFT JOIN FETCH os.business b " +
            "LEFT JOIN optional_service_image si on si.service.id = os.id " +
            "LEFT JOIN FETCH os.optionalServiceBusinessHours bh " +
            "WHERE os.id = :serviceId")
    Optional<OptionalService> findWithDetail(@Param("serviceId") long serviceId);

    @Query("SELECT os from optional_service os " +
            "LEFT JOIN optional_service_image si on si.service.id = os.id " +
            "WHERE os.id = :serviceId")
    Optional<OptionalService> findWithImages(@Param("serviceId") long serviceId);

    @Query("SELECT count(os) from optional_service os " +
            "WHERE os.type LIKE %:keyword% " +
            "OR os.description LIKE %:keyword% " +
            "OR os.alias LIKE %:keyword%")
    Long countAll(@Param("keyword") String keyword);

    List<OptionalService> findAllByBusiness_Id(long id);

    @Query(value = "SELECT distinct os.* from optional_service os " +
            "LEFT JOIN optional_service_availability sa on sa.id_optional_service = os.id_optional_service " +
            "LEFT JOIN address a on os.id_service_address = a.id_address " +
            "LEFT JOIN optional_service_image osi on os.id_optional_service = osi.id_optional_service " +
            "WHERE sa.date = CAST(:date as timestamp) " +
            "AND (:city like '' or a.city like :city)", nativeQuery = true)
    List<OptionalService> searchByDate(@Param("date") String date, @Param("city") String city);

    @Query("SELECT distinct s from optional_service s " +
            "LEFT JOIN optional_service_image si on si.service.id = s.id " +
            "left join fetch s.serviceAddress sa " +
            "left join fetch s.availability " +
            "left join fetch s.optionalServiceBusinessHours bh ")
    List<OptionalService> getAll();

}
