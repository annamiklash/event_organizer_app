package pjatk.pro.event_organizer_app.customer.guest.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pjatk.pro.event_organizer_app.customer.guest.model.Guest;

import java.util.List;

@Repository
public interface GuestRepository extends JpaRepository<Guest, Long> {

    @Query("SELECT g FROM guest g " +
            "WHERE (:keyword is null or g.firstName LIKE %:keyword%) " +
            "OR (:keyword is null or g.lastName LIKE %:keyword%) " +
            "OR (:keyword is null or g.email LIKE %:keyword%)")
    Page<Guest> findAllWithKeyword(Pageable pageable, @Param("keyword") String keyword);

    List<Guest> getAllByCustomer_Id(long id);
}
