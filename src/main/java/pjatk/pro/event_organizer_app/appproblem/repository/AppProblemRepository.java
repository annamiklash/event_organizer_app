package pjatk.pro.event_organizer_app.appproblem.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pjatk.pro.event_organizer_app.appproblem.model.AppProblem;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppProblemRepository extends JpaRepository<AppProblem, Long> {

    @Query("SELECT a FROM app_problem AS a " +
            "left join users u on u.id = a.user.id")
    Page<AppProblem> findAllWithKeyword(Pageable paging);

    @Query(value = "SELECT a.* FROM app_problem AS a " +
            "WHERE a.created_at <= CAST(:dateTo as timestamp) " +
            "AND  a.created_at >= CAST(:dateFrom as timestamp)", nativeQuery = true)
    List<AppProblem> findAll(@Param("dateFrom") String dateFrom, @Param("dateTo") String dateTo);

    @Query("SELECT a FROM app_problem AS a " +
            "left join users u on u.id = a.user.id WHERE a.resolvedAt is null")
    Page<AppProblem> findAllWithKeywordNotResolved(Pageable paging);

    @Query("SELECT a FROM app_problem AS a " +
            "left join users u on u.id = a.user.id WHERE a.resolvedAt is not null")
    Page<AppProblem> findAllWithKeywordResolved(Pageable paging);

    @Query("SELECT a FROM app_problem AS a " +
            "left join fetch a.user ua " +
            "WHERE a.id = :id")
    Optional<AppProblem> findByIdWithDetail(@Param("id") long id);

    List<AppProblem> findByUser_Id(long id);


}
