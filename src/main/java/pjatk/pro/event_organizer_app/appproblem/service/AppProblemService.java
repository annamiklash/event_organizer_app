package pjatk.pro.event_organizer_app.appproblem.service;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pjatk.pro.event_organizer_app.appproblem.mapper.AppProblemMapper;
import pjatk.pro.event_organizer_app.appproblem.model.AppProblem;
import pjatk.pro.event_organizer_app.appproblem.model.dto.AppProblemDto;
import pjatk.pro.event_organizer_app.appproblem.model.enums.AppProblemStatusEnum;
import pjatk.pro.event_organizer_app.appproblem.repository.AppProblemRepository;
import pjatk.pro.event_organizer_app.common.helper.TimestampHelper;
import pjatk.pro.event_organizer_app.common.mapper.PageableMapper;
import pjatk.pro.event_organizer_app.common.paginator.CustomPage;
import pjatk.pro.event_organizer_app.enums.AppProblemTypeEnum;
import pjatk.pro.event_organizer_app.exceptions.NotFoundException;
import pjatk.pro.event_organizer_app.user.model.User;
import pjatk.pro.event_organizer_app.user.service.UserService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class AppProblemService {

    private final AppProblemRepository appProblemRepository;

    private final UserService userService;

    private final TimestampHelper timestampHelper;

    public List<AppProblem> list(CustomPage customPage, String keyword, AppProblemStatusEnum status) {
        keyword = Strings.isNullOrEmpty(keyword) ? "" : keyword.toLowerCase();
        final Pageable paging = PageableMapper.map(customPage);

        if (status == AppProblemStatusEnum.RESOLVED) {
            final Page<AppProblem> page = appProblemRepository.findAllWithKeywordResolved(paging);
            return ImmutableList.copyOf(page.get().collect(Collectors.toList()));
        }
        if (status == AppProblemStatusEnum.NOT_RESOLVED) {
            final Page<AppProblem> page = appProblemRepository.findAllWithKeywordNotResolved(paging);
            return ImmutableList.copyOf(page.get().collect(Collectors.toList()));
        }

        final Page<AppProblem> page = appProblemRepository.findAllWithKeyword(paging);
        return ImmutableList.copyOf(page.get().collect(Collectors.toList()));
    }

    public List<AppProblem> list(String dateFrom, String dateTo) {
        final List<AppProblem> list = appProblemRepository.findAll(dateFrom, dateTo);
        return ImmutableList.copyOf(list);
    }

    public AppProblem get(long id) {
        return appProblemRepository.findByIdWithDetail(id)
                .orElseThrow(() -> new NotFoundException("App problem with id " + id + " DOES NOT EXIST"));
    }

    public ImmutableList<AppProblem> getByUserId(long id) {
        return ImmutableList.copyOf(appProblemRepository.findByUser_Id(id));

    }

    public AppProblem resolve(long id) {
        final AppProblem appProblem = get(id);

        appProblem.setResolvedAt(timestampHelper.now());
        save(appProblem);

        return appProblem;
    }

    public AppProblem create(AppProblemDto dto, long id) {
        final User user = userService.get(id);

        final Optional<AppProblemTypeEnum> optionalConcern = Arrays.stream(
                AppProblemTypeEnum.values())
                .filter(appProblemType -> appProblemType.getValue().equals(dto.getConcern()))
                .findAny();

        if (optionalConcern.isEmpty()) {
            throw new NotFoundException("No matching concern found");
        }
        final AppProblem appProblem = AppProblemMapper.fromDto(dto);

        appProblem.setUser(user);
        appProblem.setCreatedAt(timestampHelper.now());

        save(appProblem);

        return appProblem;
    }

    private void save(AppProblem appProblem) {
        appProblemRepository.save(appProblem);
    }

}
