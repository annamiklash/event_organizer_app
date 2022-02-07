package pjatk.pro.event_organizer_app.optional_service.model.interpreter.translation.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pjatk.pro.event_organizer_app.exceptions.NotFoundException;
import pjatk.pro.event_organizer_app.optional_service.model.interpreter.translation.model.TranslationLanguage;
import pjatk.pro.event_organizer_app.optional_service.model.interpreter.translation.repository.TranslationLanguageRepository;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class TranslationLanguageService {

    private final TranslationLanguageRepository translationLanguageRepository;

    public List<TranslationLanguage> getAllByInterpreterId(long id) {
        return translationLanguageRepository.getByInterpreterId(id);
    }

    public TranslationLanguage getByName(String name) {
        return translationLanguageRepository.getByName(name)
                .orElseThrow(() -> new NotFoundException("No translation language with name " + name));

    }


}
