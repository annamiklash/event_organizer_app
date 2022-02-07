package pjatk.pro.event_organizer_app.appproblem.controller;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pjatk.pro.event_organizer_app.appproblem.mapper.AppProblemMapper;
import pjatk.pro.event_organizer_app.appproblem.model.AppProblem;
import pjatk.pro.event_organizer_app.appproblem.model.dto.AppProblemDto;
import pjatk.pro.event_organizer_app.appproblem.model.enums.AppProblemStatusEnum;
import pjatk.pro.event_organizer_app.appproblem.service.AppProblemService;
import pjatk.pro.event_organizer_app.common.paginator.CustomPage;
import pjatk.pro.event_organizer_app.common.tools.CsvTools;
import pjatk.pro.event_organizer_app.enums.AppProblemTypeEnum;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("api/problems")
public class AppProblemController {

    private final AppProblemService appProblemService;

    private final CsvTools csvTools;

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<AppProblemDto>> list(@RequestParam(required = false) String keyword,
                                                             @RequestParam(defaultValue = "0") Integer pageNo,
                                                             @RequestParam(defaultValue = "50") Integer pageSize,
                                                             @RequestParam(defaultValue = "id") String sort,
                                                             @RequestParam(defaultValue = "desc") String order,
                                                             @RequestParam AppProblemStatusEnum status) {
        log.info("GET ALL APP_PROBLEMS");
        final CustomPage customPage = CustomPage.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .sortBy(sort)
                .order(order)
                .build();
        final List<AppProblem> appProblemList = appProblemService.list(customPage, keyword, status);

        final ImmutableList<AppProblemDto> resultList = appProblemList.stream()
                .map(AppProblemMapper::toDtoWithUser)
                .collect(ImmutableList.toImmutableList());

        return ResponseEntity.ok(resultList);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseBody
    @RequestMapping(
            path = "export",
            method = RequestMethod.GET,
            produces = "text/csv")
    public ResponseEntity<byte[]> list(@RequestParam String dateFrom,
                                     @RequestParam String dateTo) throws IOException {

        final List<AppProblem> appProblemList = appProblemService.list(dateFrom, dateTo);

        final ImmutableList<AppProblemDto> resultList = appProblemList.stream()
                .map(AppProblemMapper::toDtoWithUser)
                .collect(ImmutableList.toImmutableList());

        final String path = csvTools.writeToFile(resultList);

        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(Files.readAllBytes(new File(path).toPath()));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(
            method = RequestMethod.GET,
            params = {"id"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppProblemDto> get(@RequestParam long id) {
        log.info("GET " + id);
        final AppProblem appProblem = appProblemService.get(id);

        return ResponseEntity.ok(AppProblemMapper.toDtoWithUser(appProblem));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'BUSINESS', 'CUSTOMER')")
    @RequestMapping(
            method = RequestMethod.GET,
            path = "/types",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImmutableList<String>> concerns() {
        final ImmutableList<String> resultList = Stream.of(AppProblemTypeEnum.values())
                .map(AppProblemTypeEnum::getValue)
                .collect(ImmutableList.toImmutableList());

        return ResponseEntity.ok(resultList);
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppProblemDto> resolve(@RequestParam long id) {
        log.info("GET " + id);
        final AppProblem appProblem = appProblemService.resolve(id);

        return ResponseEntity.ok(AppProblemMapper.toDto(appProblem));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'BUSINESS', 'CUSTOMER')")
    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppProblemDto> reportProblem(@Valid @RequestBody AppProblemDto dto,
                                                       @RequestParam long userId) {

        final AppProblem appProblem = appProblemService.create(dto, userId);

        return ResponseEntity.ok(AppProblemMapper.toDtoWithUser(appProblem));
    }

}
