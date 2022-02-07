package pjatk.pro.event_organizer_app.optional_service.model.interpreter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import pjatk.pro.event_organizer_app.optional_service.model.OptionalService;
import pjatk.pro.event_organizer_app.optional_service.model.interpreter.translation.model.TranslationLanguage;

import javax.persistence.*;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@DiscriminatorValue("INTERPRETER")
public class Interpreter extends OptionalService {

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "service_translator_language",
            joinColumns = @JoinColumn(name = "id_optional_service"),
            inverseJoinColumns = @JoinColumn(name = "id_language"))
    private Set<TranslationLanguage> languages;

    public void addLanguage(TranslationLanguage language) {
        languages.add(language);
    }

    public void removeLanguage(TranslationLanguage language) {
        languages.remove(language);
    }

}
