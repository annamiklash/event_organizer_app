package pjatk.pro.event_organizer_app.optional_service.model.music.musicstyle.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pjatk.pro.event_organizer_app.optional_service.model.music.musicstyle.MusicStyle;

import java.util.List;
import java.util.Optional;


@Repository
public interface MusicStyleRepository extends JpaRepository<MusicStyle, Long> {

    @Query("SELECT ms FROM music_style ms " +
            "LEFT JOIN FETCH ms.optionalServices os " +
            "WHERE os.id = :serviceId")
    List<MusicStyle> findByServiceId(@Param("serviceId") long serviceId);

    Optional<MusicStyle> findByName(String name);
}
