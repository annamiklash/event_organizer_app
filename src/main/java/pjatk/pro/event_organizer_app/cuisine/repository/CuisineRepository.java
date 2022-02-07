package pjatk.pro.event_organizer_app.cuisine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pjatk.pro.event_organizer_app.cuisine.model.Cuisine;

@Repository
public interface CuisineRepository extends JpaRepository<Cuisine, Long> {

    boolean existsByName(String name);

    Cuisine findByName(String name);
}
