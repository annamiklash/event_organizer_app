package pjatk.pro.event_organizer_app.appproblem.service

import org.apache.commons.collections4.CollectionUtils
import org.springframework.data.domain.Page
import pjatk.pro.event_organizer_app.appproblem.model.enums.AppProblemStatusEnum
import pjatk.pro.event_organizer_app.appproblem.repository.AppProblemRepository
import pjatk.pro.event_organizer_app.common.helper.TimestampHelper
import pjatk.pro.event_organizer_app.exceptions.NotFoundException
import pjatk.pro.event_organizer_app.trait.page.PageTrait
import pjatk.pro.event_organizer_app.trait.problem.AppProblemTrait
import pjatk.pro.event_organizer_app.trait.user.UserTrait
import pjatk.pro.event_organizer_app.user.service.UserService
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDateTime

class AppProblemServiceTest extends Specification implements PageTrait, AppProblemTrait, UserTrait {

    @Subject
    AppProblemService appProblemService

    AppProblemRepository appProblemRepository

    TimestampHelper timestampUtil

    UserService userService

    def setup() {
        appProblemRepository = Mock()
        timestampUtil = Mock()
        userService = Mock()

        appProblemService = new AppProblemService(appProblemRepository, userService, timestampUtil)
    }

    def "list all positive"() {
        given:
        def page = fakePage
        def paging = fakePaging

        when:
        appProblemService.list(page, '', AppProblemStatusEnum.ALL)

        then:
        appProblemRepository.findAllWithKeyword(paging) >> Page.empty()
    }

    def "list RESOLVED positive"() {
        given:
        def page = fakePage
        def paging = fakePaging

        when:
        appProblemService.list(page, '', AppProblemStatusEnum.RESOLVED)

        then:
        appProblemRepository.findAllWithKeywordResolved(paging) >> Page.empty()
    }

    def "list NOT_RESOLVED positive"() {
        given:
        def page = fakePage
        def paging = fakePaging

        when:
        appProblemService.list(page, '', AppProblemStatusEnum.NOT_RESOLVED)

        then:
        appProblemRepository.findAllWithKeywordNotResolved(paging) >> Page.empty()
    }

    def "get positive scenario"() {
        given:

        def id = 1
        def appProblem = fakeAppProblem

        when:
        appProblemService.get(id)

        then:
        appProblemRepository.findByIdWithDetail(id) >> Optional.of(appProblem)
    }

    def "get negative scenario"() {
        given:
        def id = 999

        when:
        appProblemService.get(id)

        then:
        thrown(NotFoundException)
        appProblemRepository.findByIdWithDetail(id) >> { throw new NotFoundException('') }

    }

    def "get by userId positive scenario"() {
        given:
        def id = 1
        def now = LocalDateTime.parse('2007-12-03T10:15:30')

        when:
        appProblemService.getByUserId(id)

        then:
        appProblemRepository.findByUser_Id(id) >> CollectionUtils.emptyCollection();

    }

    def "resolve positive scenario"() {
        given:
        def id = 1
        def appProblem = fakeAppProblem
        def now = LocalDateTime.parse('2007-12-03T10:15:30')
        appProblem.setResolvedAt(now)

        when:
        appProblemService.resolve(id)

        then:
        1 * timestampUtil.now() >> now
        appProblemRepository.findByIdWithDetail(id) >> Optional.of(appProblem)
        1 * appProblemRepository.save(appProblem)
    }


    def "create positive scenario"() {
        given:
        def id = 1
        def user = fakeUser
        def appProblem = fakeAppProblem
        appProblem.setId(null)
        def dto = fakeAppProblemDto
        def now = LocalDateTime.parse('2007-12-03T10:15:30')
        appProblem.setCreatedAt(now)
        appProblem.setUser(user)

        when:
        appProblemService.create(dto, id)

        then:
        userService.get(id) >> user
        1 * timestampUtil.now() >> now
        1 * appProblemRepository.save(appProblem)
    }

    def "create negative scenario"() {
        given:
        def id = 1
        def user = fakeUser
        def dto = fakeAppProblemDto
        dto.setConcern("NOT EXISTING CONCERN")

        when:
        appProblemService.create(dto, id)

        then:
        1 * userService.get(id) >> user

        thrown(NotFoundException)
    }


}
