package pjatk.pro.event_organizer_app.user.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pjatk.pro.event_organizer_app.user.model.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u from users u " +
            "where u.email = :email")
    Optional<User> findUserByEmail (String email);

    boolean existsByEmail(String email);

    Optional<User> findUserByResetPasswordToken(String resetPasswordToken);

    @Query("SELECT u from users u where u.email = :email " +
            "and u.blockedAt is null")
    Optional<User> active(@Param("email") String email);

    @Query("SELECT u FROM users u " +
            "LEFT JOIN customer c on c.id = u.id " +
            "LEFT JOIN business b on b.id = u.id " +
            "WHERE u.email LIKE %:keyword%")
    Page<User> findAllWithKeyword(Pageable paging, String keyword);

    @Query("SELECT count(u) FROM users u " +
            "LEFT JOIN customer c on c.id = u.id " +
            "LEFT JOIN business b on b.id = u.id ")
    Long countAll(String keyword);
}
