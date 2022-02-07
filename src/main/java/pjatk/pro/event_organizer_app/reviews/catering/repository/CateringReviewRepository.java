package pjatk.pro.event_organizer_app.reviews.catering.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pjatk.pro.event_organizer_app.reviews.catering.model.CateringReview;

import java.util.List;

@Repository
public interface CateringReviewRepository extends JpaRepository<CateringReview, Long> {

    boolean existsCateringReviewByCatering_Id(long id);

    @Query("SELECT cr from catering_review cr " +
            "left join customer c on c.id = cr.customer.id " +
            "left join catering cat on cat.id = cr.catering.id WHERE cat.id = :id")
    List<CateringReview> getByCateringId(@Param("id") long id);

    Long countAllByCatering_Id(long id);

    @Query("SELECT cr from catering_review cr " +
            "left join customer c on c.id = cr.customer.id " +
            "left join customer_avatar ca on ca.id = c.avatar.id " +
            "left join catering cat on cat.id = cr.catering.id WHERE cat.id = :id")
    Page<CateringReview> getByCateringId(@Param("id")long id, Pageable pageable);
}
