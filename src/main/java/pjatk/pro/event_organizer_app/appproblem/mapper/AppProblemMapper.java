package pjatk.pro.event_organizer_app.appproblem.mapper;

import lombok.experimental.UtilityClass;
import pjatk.pro.event_organizer_app.appproblem.model.AppProblem;
import pjatk.pro.event_organizer_app.appproblem.model.dto.AppProblemDto;
import pjatk.pro.event_organizer_app.common.util.DateTimeUtil;
import pjatk.pro.event_organizer_app.user.mapper.UserMapper;

@UtilityClass
public class AppProblemMapper {

    public AppProblemDto toDto(AppProblem appProblem) {
        return AppProblemDto.builder()
                .id(appProblem.getId())
                .createdAt(DateTimeUtil.fromLocalDateTimetoString(appProblem.getCreatedAt()))
                .resolvedAt(DateTimeUtil.fromLocalDateTimetoString(appProblem.getResolvedAt()))
                .concern(appProblem.getConcern())
                .description(appProblem.getDescription())
                .build();
    }

    public AppProblemDto toDtoWithUser(AppProblem appProblem) {
        final AppProblemDto appProblemDto = toDto(appProblem);
        appProblemDto.setUser(UserMapper.toDto(appProblem.getUser()));
        return appProblemDto;
    }

    public AppProblem fromDto(AppProblemDto dto) {
        return AppProblem.builder()
                .concern(dto.getConcern())
                .description(dto.getDescription())
                .build();
    }
}
