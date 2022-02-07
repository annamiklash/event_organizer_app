package pjatk.pro.event_organizer_app.image.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pjatk.pro.event_organizer_app.image.model.LocationImage;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationImageRepository extends JpaRepository<LocationImage, Long> {

    List<LocationImage> findAllByLocation_Id(long locationId);

    Optional<LocationImage> findByIdAndLocation_Id(long id, long locationId);

    @Query("SELECT i from location_image i " +
            "LEFT JOIN FETCH i.location l " +
            "WHERE l.id = :locationId")
    Optional<LocationImage> getMain(@Param("locationId") long locationId);

    @Query("SELECT count(i) from location_image i " +
            "LEFT JOIN location l on l.id = i.location.id " +
            "WHERE l.id = :locationId")
    int countAll(@Param("locationId") long locationId);

}
