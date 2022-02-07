package pjatk.pro.event_organizer_app.optional_service.model.interpreter.translation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pjatk.pro.event_organizer_app.optional_service.model.interpreter.translation.model.TranslationLanguage;

import java.util.List;
import java.util.Optional;

@Repository
public interface TranslationLanguageRepository extends JpaRepository<TranslationLanguage, Long> {

    @Query("SELECT l from translation_language l " +
            "left join fetch l.interpreters i " +
            "where i.id = :id ")
    List<TranslationLanguage> getByInterpreterId(@Param("id") long id);

    Optional<TranslationLanguage> getByName(String name);
}
