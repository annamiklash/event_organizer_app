package pjatk.pro.event_organizer_app.address.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pjatk.pro.event_organizer_app.address.model.Address;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    @Query("SELECT a FROM address a " +
            "LEFT JOIN users u on u.id = :userId")
    Optional<Address> findByUserId(@Param("userId") long userId);

    @Query("SELECT a FROM address a " +
            "WHERE a.id = (SELECT b.address.id FROM business b " +
            "LEFT JOIN address ba on ba.id = a.id " +
            "LEFT JOIN location l on l.business.id = b.id " +
            "WHERE l.id = :locationId)")
    Optional<Address> findByLocationId(@Param("locationId") long locationId);

    @Query("SELECT a FROM address a " +
            "WHERE a.id = (SELECT b.address.id FROM business b " +
            "LEFT JOIN address ba on ba.id = a.id " +
            "LEFT JOIN catering c on c.business.id = b.id " +
            "WHERE c.id = :cateringId)")
    Optional<Address> findByCateringId(@Param("cateringId") long cateringId);

    @Query("SELECT a FROM address a " +
            "WHERE a.id = (SELECT b.address.id FROM business b " +
            "LEFT JOIN address ba on ba.id = a.id " +
            "LEFT JOIN optional_service s on s.business.id = b.id " +
            "WHERE s.id = :serviceId)")
    Optional<Address> findByServiceId(@Param("serviceId") long serviceId);
}
