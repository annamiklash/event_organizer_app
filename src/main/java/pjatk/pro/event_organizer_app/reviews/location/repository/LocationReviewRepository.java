package pjatk.pro.event_organizer_app.reviews.location.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pjatk.pro.event_organizer_app.reviews.location.model.LocationReview;

import java.util.List;

@Repository
public interface LocationReviewRepository extends JpaRepository<LocationReview, Long> {

    boolean existsLocationReviewByLocation_Id(long id);

    @Query("SELECT lr from location_review lr " +
            "left join customer c on c.id = lr.customer.id " +
            "left join customer_avatar ca on ca.id = c.avatar.id " +
            "left join location l on l.id = lr.location.id WHERE l.id = :id")
    Page<LocationReview> getByLocationId(@Param("id") long id, Pageable paging);

    @Query("SELECT lr from location_review lr " +
            "left join customer c on c.id = lr.customer.id " +
            "left join customer_avatar ca on ca.id = c.avatar.id " +
            "left join location l on l.id = lr.location.id WHERE l.id = :id")
    List<LocationReview> getByLocationId(@Param("id") long id);

    Long countLocationReviewsByLocation_Id(long id);


}
