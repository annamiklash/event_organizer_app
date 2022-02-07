package pjatk.pro.event_organizer_app.customer.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pjatk.pro.event_organizer_app.customer.model.Customer;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("SELECT distinct c FROM customer c " +
            "LEFT JOIN customer_avatar a on c.avatar.id = a.id " +
            "WHERE c.id = :customerId")
    Optional<Customer> getByIdWithAvatar(@Param("customerId") long customerId);

    @Query("SELECT c FROM customer c " +
            "LEFT JOIN customer_avatar a on c.avatar.id = a.id " +
            "LEFT JOIN users u on c.id=u.id " +
            "WHERE (:keyword is null or c.firstName LIKE %:keyword%) " +
            "OR (:keyword is null or c.lastName LIKE %:keyword%) " +
            "OR (:keyword is null or u.email LIKE %:keyword%)")
    Page<Customer> findAllWithKeyword(Pageable pageable, @Param("keyword") String keyword);

    @Query("SELECT c FROM customer c " +
            "LEFT JOIN users u on c.id=u.id " +
            "left join fetch c.guests cg " +
            "WHERE c.id = :id")
    Optional<Customer> getByIdWithAllGuests(@Param("id") long id);

    @Query("SELECT c FROM customer c " +
            "LEFT JOIN users u on c.id = u.id " +
            "left join u.appProblems cup " +
            "WHERE c.id = :id")
    Optional<Customer> getByIdWithProblems(@Param("id") long id);

    @Query("SELECT c FROM customer c " +
            "LEFT JOIN customer_avatar a on c.avatar.id = a.id " +
            "LEFT JOIN users u on c.id = u.id " +
            "left join u.appProblems cup " +
            "left join fetch c.guests cg " +
            "left join fetch c.events ce " +
            "left join fetch ce.eventType cee " +
            "WHERE c.id = :id")
    Optional<Customer> getWithDetail(@Param("id") long id);

    @Query("SELECT c from customer c " +
            "LEFT JOIN users u on c.id=u.id " +
            "left join fetch c.events ce " +
            "left join fetch ce.eventType cee " +
            "WHERE c.id = :id ")
    Optional<Customer> getByIdWithEvents(@Param("id") long id);

    @Query("SELECT c FROM customer c " +
            "LEFT JOIN users u on c.id=u.id " +
            "LEFT JOIN FETCH c.guests g " +
            "LEFT JOIN FETCH c.events e " +
            "WHERE c.id = :id")
    Optional<Customer> getAllCustomerInformation(@Param("id") long id);

    @Query("SELECT c FROM customer c " +
            "LEFT JOIN customer_avatar a on c.avatar.id = a.id " +
            "LEFT JOIN users u on c.id=u.id " +
            "WHERE c.id = :id")
    Optional<Customer> getByIdWithUser(@Param("id") long id);


}
