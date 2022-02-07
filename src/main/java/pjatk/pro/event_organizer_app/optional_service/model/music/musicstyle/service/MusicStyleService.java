package pjatk.pro.event_organizer_app.optional_service.model.music.musicstyle.service;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pjatk.pro.event_organizer_app.exceptions.NotFoundException;
import pjatk.pro.event_organizer_app.optional_service.model.music.musicstyle.MusicStyle;
import pjatk.pro.event_organizer_app.optional_service.model.music.musicstyle.repository.MusicStyleRepository;

@Service
@AllArgsConstructor
@Slf4j
public class MusicStyleService {

    private final MusicStyleRepository musicStyleRepository;

    public ImmutableList<MusicStyle> getByServiceId(long serviceId) {
        return ImmutableList.copyOf(musicStyleRepository.findByServiceId(serviceId));
    }

    public MusicStyle getByName(String name) {
        return musicStyleRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException("No music style with name " + name));
    }



}
