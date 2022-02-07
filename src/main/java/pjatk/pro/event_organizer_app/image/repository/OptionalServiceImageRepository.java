package pjatk.pro.event_organizer_app.image.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pjatk.pro.event_organizer_app.image.model.OptionalServiceImage;

import java.util.List;
import java.util.Optional;

@Repository
public interface OptionalServiceImageRepository extends JpaRepository<OptionalServiceImage, Long> {

    List<OptionalServiceImage> findAllByService_Id(long locationId);

    Optional<OptionalServiceImage> findByIdAndService_Id(long id, long serviceId);

    @Query("SELECT i from optional_service_image i " +
            "LEFT JOIN FETCH i.service s " +
            "WHERE s.id = :serviceId")
    Optional<OptionalServiceImage> getMain(@Param("serviceId") long serviceId);

    @Query(value = "SELECT count(1) from optional_service_image i " +
            "LEFT JOIN optional_service os on i.id_optional_service = os.id_optional_service " +
            "WHERE os.id_optional_service = :serviceId", nativeQuery = true)
    int countAll(@Param("serviceId") long serviceId);
}
