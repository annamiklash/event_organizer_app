package pjatk.pro.event_organizer_app.image.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pjatk.pro.event_organizer_app.image.model.CateringImage;

import java.util.List;
import java.util.Optional;

@Repository
public interface CateringImageRepository extends JpaRepository<CateringImage, Long> {

    List<CateringImage> findAllByCatering_Id(long locationId);

    Optional<CateringImage> getCateringImageByIdAndCatering_Id(long id, long cateringId);

    @Query("SELECT i from catering_image i " +
            "LEFT JOIN FETCH i.catering c " +
            "WHERE c.id = :cateringId")
    Optional<CateringImage> getMain(@Param("cateringId") long cateringId);

    @Query("SELECT count(i) from catering_image i " +
            "LEFT JOIN catering c on c.id = i.catering.id " +
            "WHERE c.id = :cateringId")
    int countAll(@Param("cateringId") long cateringId);
}
