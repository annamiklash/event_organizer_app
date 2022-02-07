package pjatk.pro.event_organizer_app.user.service

import com.google.common.collect.ImmutableList
import org.springframework.data.domain.PageImpl
import pjatk.pro.event_organizer_app.business.repository.BusinessRepository
import pjatk.pro.event_organizer_app.common.helper.TimestampHelper
import pjatk.pro.event_organizer_app.customer.repository.CustomerRepository
import pjatk.pro.event_organizer_app.security.password.PasswordEncoderSecurity
import pjatk.pro.event_organizer_app.trait.business.BusinessTrait
import pjatk.pro.event_organizer_app.trait.customer.CustomerTrait
import pjatk.pro.event_organizer_app.trait.page.PageTrait
import pjatk.pro.event_organizer_app.trait.user.UserTrait
import pjatk.pro.event_organizer_app.user.mapper.UserMapper
import pjatk.pro.event_organizer_app.user.model.dto.ChangePasswordDto
import pjatk.pro.event_organizer_app.user.model.dto.LoginDto
import pjatk.pro.event_organizer_app.user.model.dto.NewPasswordDto
import pjatk.pro.event_organizer_app.user.repository.UserRepository
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDateTime

class UserServiceTest extends Specification implements PageTrait,
        UserTrait, CustomerTrait, BusinessTrait {

    @Subject
    UserService userService

    UserRepository userRepository
    PasswordEncoderSecurity passwordEncoderSecurity
    EmailService emailService
    CustomerRepository customerRepository
    BusinessRepository businessRepository
    TimestampHelper timestampHelper

    LocalDateTime now = LocalDateTime.parse('2007-12-03T10:15:30')


    def setup() {
        userRepository = Mock()
        passwordEncoderSecurity = Mock()
        emailService = Mock()
        customerRepository = Mock()
        businessRepository = Mock()
        timestampHelper = Mock()

        timestampHelper.now() >> now

        userService = new UserService(userRepository,
                passwordEncoderSecurity,
                emailService,
                customerRepository,
                businessRepository,
                timestampHelper
        )
    }

    def "List"() {
        given:
        def keyword = 'keyword'
        def customPagination = fakePage

        def paging = fakePaging
        def page = new PageImpl<>([fakeUser])

        def target = ImmutableList.of(fakeUser)
        when:
        def result = userService.list(customPagination, keyword)

        then:
        1 * userRepository.findAllWithKeyword(paging, keyword) >> page

        result == target
    }

    def "GetUserByEmail"() {
        given:
        def user = fakeUser
        def email = 'test@email.com'

        def target = user

        when:
        def result = userService.getUserByEmail(email)

        then:
        1 * userRepository.findUserByEmail(email) >> Optional.of(user)

        result == target
    }

    def "Get"() {
        given:
        def user = fakeUser
        def id = 1l

        def target = user

        when:
        def result = userService.get(id)

        then:
        1 * userRepository.findById(id) >> Optional.of(user)

        result == target
    }

    def "GetWithDetail customer"() {
        given:
        def user = fakeUser
        def id = 1l
        def customer = fakeCustomer
        def target = UserMapper.toDtoWithCustomer(user, customer)

        when:
        def result = userService.getWithDetail(id)

        then:
        1 * userRepository.findById(id) >> Optional.of(user)
        1 * customerRepository.findById(id) >> Optional.of(customer)

        result == target
    }

    def "GetWithDetail business"() {
        given:
        def user = fakeUserB
        def id = 1l
        def business = fakeVerifiedBusiness
        def target = UserMapper.toDtoWithBusiness(user, business)

        when:
        def result = userService.getWithDetail(id)

        then:
        1 * userRepository.findById(id) >> Optional.of(user)
        1 * businessRepository.findById(id) >> Optional.of(business)

        result == target
    }

    def "SendResetEmailLink"() {
        given:
        def user = fakeUser
        def email = 'test@email.com'
        def appUrl = 'localhost'

        when:
        userService.sendResetEmailLink(email, appUrl)

        then:
        1 * userRepository.findUserByEmail(email) >> Optional.of(user)
        1 * userRepository.save(_)
        1 * emailService.sendEmail(_)

    }

    def "ChangePassword"() {
        given:
        def id = 1l
        def dto = ChangePasswordDto.builder()
                .oldPassword('oldPassword')
                .newPassword('oldPassword1')
                .build()
        def user = fakeUser
        def userWithNewPassword = user
        def encryptedPassword = 'encrypted'
        user.setPassword(encryptedPassword)

        when:
        userService.changePassword(id, dto)

        then:
        1 * userRepository.findById(id) >> Optional.of(user)
        1 * passwordEncoderSecurity.doPasswordsMatch(dto.getOldPassword(), user.getPassword()) >> true
        1 * passwordEncoderSecurity.bcryptEncryptor(dto.getNewPassword()) >> encryptedPassword
        1 * userRepository.save(userWithNewPassword)
    }

    def "SetNewPassword"() {
        given:
        def token = 'token'
        def id = 1l
        def dto = NewPasswordDto.builder()
                .password('oldPassword1')
                .build()
        def user = fakeUser
        user.setResetPasswordToken(token)

        def userWithNewPassword = user
        def encryptedPassword = 'encrypted'
        user.setPassword(encryptedPassword)

        when:
        userService.setNewPassword(token, dto)

        then:
        1 * userRepository.findUserByResetPasswordToken(token) >> Optional.of(user)
        1 * passwordEncoderSecurity.bcryptEncryptor(dto.getPassword()) >> encryptedPassword
        1 * userRepository.save(userWithNewPassword)
    }

    def "UserExists"() {
        given:
        def user = fakeUser
        def email = 'test@email.com'

        when:
        def result = userService.userExists(email)

        then:
        userRepository.existsByEmail(email) >> true

        result
    }

    def "Block"() {
        given:
        def user = fakeUser
        def id = 1l
        def blockedUser = user
        blockedUser.setActive(false)
        blockedUser.setBlockedAt(now)

        def target = blockedUser

        when:
        userService.block(id)

        then:
        1 * userRepository.findById(id) >> Optional.of(user)
        1 * userRepository.save(user)
    }

    def "Activate"() {
        given:
        def user = fakeUser
        user.setActive(false)
        user.setBlockedAt(now)

        def id = 1l
        def activeUser = user
        activeUser.setActive(true)
        user.setBlockedAt(null)
        user.setModifiedAt(now)

        when:
        userService.activate(id)

        then:
        1 * userRepository.findById(id) >> Optional.of(user)
        1 * userRepository.save(activeUser)

    }

    def "IsActive"() {
        given:
        def user = fakeUser
        def loginDto = LoginDto.builder()
                .email('test@email.com')
                .password('password')
                .build()

        when:
        def result = userService.isActive(loginDto)

        then:
        userRepository.active(loginDto.getEmail()) >> Optional.of(user)

        result
    }

    def "Count"() {
        given:
        def keyword = '123'

        def target = 123L

        when:
        def result = userService.count(keyword)

        then:
        1 * userRepository.countAll(keyword) >> target

        result == target

    }
}
