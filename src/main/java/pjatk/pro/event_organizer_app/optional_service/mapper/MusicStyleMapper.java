package pjatk.pro.event_organizer_app.optional_service.mapper;

import lombok.experimental.UtilityClass;
import pjatk.pro.event_organizer_app.optional_service.model.dto.MusicStyleDto;
import pjatk.pro.event_organizer_app.optional_service.model.music.musicstyle.MusicStyle;

@UtilityClass
public class MusicStyleMapper {

    public MusicStyleDto toDto(MusicStyle musicStyle) {
        return MusicStyleDto.builder()
                .id(musicStyle.getId())
                .name(musicStyle.getName())
                .build();
    }

    public MusicStyle fromDto(MusicStyleDto dto) {
        return MusicStyle.builder()
                .name(dto.getName())
                .build();
    }
}
