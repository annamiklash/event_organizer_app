package pjatk.pro.event_organizer_app.business.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pjatk.pro.event_organizer_app.business.model.Business;

import java.util.Optional;

@Repository
public interface BusinessRepository extends JpaRepository<Business, Long> {

    @Query("SELECT b FROM business b " +
            "LEFT JOIN users u on b.id = u.id " +
            "left join fetch b.address ba " +
            "left join fetch b.caterings bc " +
            "left join fetch b.locations bl " +
            "WHERE b.id = :id")
    Optional<Business> getWithDetail(@Param("id") long id);

    @Query("SELECT b FROM business b " +
            "LEFT JOIN users u on b.id = u.id " +
            "WHERE b.id = :id")
    Optional<Business> findById(@Param("id") long id);

    @Query("SELECT b FROM business b " +
            "LEFT JOIN users u on b.id = u.id " +
            "LEFT JOIN FETCH b.caterings c " +
            "LEFT JOIN FETCH b.locations l " +
            "LEFT JOIN FETCH b.services s " +
            "LEFT JOIN FETCH b.address a " +
            "WHERE b.id = :id")
    Optional<Business> findAllBusinessInformation(@Param("id") long id);

    @Query("SELECT b FROM business b " +
            "LEFT JOIN users u on b.id=u.id " +
            "LEFT JOIN FETCH b.address a " +
            "WHERE b.id = :id")
    Optional<Business> findByIdWithAddress(@Param("id") long id);

    @Query("SELECT b FROM business b " +
            "LEFT JOIN users u on b.id = u.id " +
            "LEFT JOIN FETCH b.caterings c " +
            "LEFT JOIN FETCH b.locations l " +
            "LEFT JOIN FETCH b.services s " +
            "LEFT JOIN FETCH b.address a " +
            "WHERE b.id = :id")
    Optional<Business> getWithAddress(@Param("id") long id);


}
