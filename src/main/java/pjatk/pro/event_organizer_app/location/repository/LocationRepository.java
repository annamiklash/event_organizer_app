package pjatk.pro.event_organizer_app.location.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pjatk.pro.event_organizer_app.location.model.Location;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends PagingAndSortingRepository<Location, Long> {

    List<Location> findByLocationAddress_City(String city);


    @Query(value = "SELECT distinct l.* from location l " +
            "LEFT JOIN location_image li on li.id_location = l.id_location " +
            "left join address a on l.id_location_address = a.id_address " +
            "left join location_description ld on l.id_location = ld.id_location " +
            "left join description_item di on ld.name = di.name " +
            "LEFT JOIN location_availability la on l.id_location = la.id_location " +
            "WHERE (:city like '' or a.city = :city) " +
            "AND la.date = CAST(:date as timestamp)", nativeQuery = true)
    List<Location> searchWithDate(@Param("city") String city,
                                  @Param("date") String date);

    @Query(value = "SELECT distinct l.* from location l " +
            "LEFT JOIN location_image li on li.id_location = l.id_location " +
            "left join address a on l.id_location_address = a.id_address " +
            "left join location_description ld on l.id_location = ld.id_location " +
            "left join description_item di on ld.name = di.name " +
            "WHERE (:city like '' or a.city = :city)", nativeQuery = true)
    List<Location> searchWithoutDate(@Param("city") String city);

    @Query(value = "SELECT distinct l.* from location l " +
            "LEFT JOIN location_image li on li.id_location = l.id_location " +
            "left join location_availability la on la.id_location = l.id_location " +
            "WHERE l.id_location = :locationId " +
            "AND la.date = CAST(:date as timestamp) " +
            "AND (:timeFrom is null or la.time_from <= CAST(:timeFrom as timestamp)) " +
            "AND (:timeTo is null or la.time_to >= CAST(:timeTo as timestamp))", nativeQuery = true)
    Optional<Location> available(@Param("locationId") long locationId, @Param("date") String date, @Param("timeFrom") String timeFrom, @Param("timeTo") String timeTo);

    @Query("SELECT l FROM location l " +
            "LEFT JOIN location_image li on li.location.id = l.id " +
            "left join fetch l.business lb " +
            "left join fetch l.caterings lc " +
            "left join fetch l.descriptions ld " +
            "left join fetch l.locationBusinessHours lbh " +
            "left join fetch l.availability la where l.id = :id")
    Optional<Location> getByIdWithDetail(@Param("id") Long id);

    @Query("SELECT distinct l FROM location AS l " +
            "LEFT JOIN location_image li on li.location.id = l.id " +
            "WHERE (:keyword = '' or (l.name LIKE %:keyword% OR l.description LIKE %:keyword%))")
    Page<Location> findAllWithKeyword(Pageable paging, @Param("keyword") String keyword);

    @Query("SELECT count(l) FROM location AS l " +
            "WHERE (:keyword like '' or (l.name LIKE %:keyword% OR l.description LIKE %:keyword%)) " +
            "AND l.deletedAt IS NULL")
    long countAll(@Param("keyword") String keyword);

    @Query(value = "SELECT distinct l.* from location l " +
            "left join location_availability la on la.id_location = l.id_location " +
            "WHERE l.id_location = :id AND la.date = CAST(:date as timestamp)", nativeQuery = true)
    Optional<Location> getByIdWithAvailability(@Param("id") long id, @Param("date") String date);

    @Query("SELECT l from location l " +
            "left join fetch l.locationAddress a " +
            "WHERE a.city = :city")
    List<Location> findAllByCity(@Param("city") String city);

    @Query("SELECT distinct l from location l " +
            "LEFT JOIN location_image li on li.location.id = l.id " +
            "left join fetch l.caterings c " +
            "WHERE c.id = :cateringId")
    List<Location> findAllByCateringId(@Param("cateringId") long cateringId);

    @Query("SELECT distinct l from location l " +
            "LEFT JOIN location_image li on li.location.id = l.id " +
            "left join fetch l.locationAddress " +
            "left join fetch l.availability " +
            "left join fetch l.descriptions ")
    List<Location> getAll();


    @Query("SELECT l from location l " +
            "LEFT JOIN FETCH l.reviews r " +
            "LEFT JOIN location_image li on li.location.id = l.id " +
            "LEFT JOIN FETCH l.locationAddress lad " +
            "LEFT JOIN FETCH l.descriptions d " +
            "LEFT JOIN FETCH l.caterings cat " +
            "LEFT JOIN FETCH l.availability la " +
            "LEFT JOIN FETCH l.locationBusinessHours bh " +
            "LEFT JOIN FETCH l.locationForEvent lfe " +
            "LEFT JOIN FETCH lfe.event " +
            "WHERE l.id = :locationId ")
    Optional<Location> getAllLocationInformation(@Param("locationId") long locationId);

    List<Location> findAllByBusiness_Id(long id);

    @Query("SELECT distinct concat(a.city, ', ', a.country)  " +
            "FROM address a " +
            "left join location l on l.locationAddress.id = a.id " +
            "order by 1 asc")
    List<String> findDistinctCities();


    @Query("SELECT l from location l " +
            "LEFT JOIN location_image li on li.location.id = l.id " +
            "WHERE l.id = :locationId")
    Optional<Location> findWithImages(@Param("locationId") long locationId);

    @Query("SELECT l from location l " +
            "LEFT JOIN location_image li on li.location.id = l.id " +
            "WHERE l.id = :locationId")
    Optional<Location> getByIdWithImages(@Param("locationId") long locationId);

}
