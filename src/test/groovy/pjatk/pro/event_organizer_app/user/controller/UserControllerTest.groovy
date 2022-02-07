package pjatk.pro.event_organizer_app.user.controller

import com.google.common.collect.ImmutableList
import org.mockito.BDDMockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import pjatk.pro.event_organizer_app.business.mapper.BusinessMapper
import pjatk.pro.event_organizer_app.business.service.BusinessService
import pjatk.pro.event_organizer_app.common.paginator.CustomPage
import pjatk.pro.event_organizer_app.customer.mapper.CustomerMapper
import pjatk.pro.event_organizer_app.customer.service.CustomerService
import pjatk.pro.event_organizer_app.security.service.SecurityService
import pjatk.pro.event_organizer_app.table.TableDto
import pjatk.pro.event_organizer_app.test_helper.TestSerializer
import pjatk.pro.event_organizer_app.trait.business.BusinessTrait
import pjatk.pro.event_organizer_app.trait.customer.CustomerTrait
import pjatk.pro.event_organizer_app.trait.user.*
import pjatk.pro.event_organizer_app.user.mapper.UserMapper
import pjatk.pro.event_organizer_app.user.service.UserService
import spock.lang.Specification

import javax.servlet.http.HttpServletRequest

import static org.mockito.ArgumentMatchers.any
import static org.mockito.ArgumentMatchers.eq
import static org.mockito.Mockito.times
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(controllers = [UserController.class])
class UserControllerTest extends Specification
        implements BusinessTrait,
                BusinessUserRegistrationDtoTrait,
                CustomerTrait,
                CustomerUserRegistrationDtoTrait,
                LoginDtoTrait,
                UserTrait,
                ChangePasswordDtoTrait,
                NewPasswordDtoTrait {

    @Autowired
    private MockMvc mockMvc

    @MockBean
    private SecurityService securityService
    @MockBean
    private UserService userService
    @MockBean
    private CustomerService customerService
    @MockBean
    private BusinessService businessService

    @WithMockUser
    def "POST api/register/business returns 200 positive test scenario"() {
        given:
        def dto = fakeBusinessUserRegistrationDto

        def business = fakeVerifiedBusiness
        def result = BusinessMapper.toDtoWithUser(business)

        def jsonRequest = TestSerializer.serialize(dto)
        def jsonResponse = TestSerializer.serialize(result)

        BDDMockito.given(businessService.createBusinessAccount(eq(dto)))
                .willReturn(business)

        expect:
        mockMvc.perform(
                post("/api/register/business")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(jsonRequest)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))

    }

    @WithMockUser
    def "POST api/register/customer returns 200 positive test scenario"() {
        given:
        def dto = fakeCustomerUserRegistrationDto

        def customer = fakeCustomer
        def result = CustomerMapper.toDto(customer)

        def jsonRequest = TestSerializer.serialize(dto)
        def jsonResponse = TestSerializer.serialize(result)

        BDDMockito.given(customerService.createCustomerAccount(eq(dto)))
                .willReturn(customer)

        expect:
        mockMvc.perform(
                post("/api/register/customer")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(jsonRequest)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))

    }

    @WithMockUser
    def "POST api/login returns 403 positive test scenario"() {
        given:
        def loginDto = fakeLoginDto

        def jsonRequest = TestSerializer.serialize(loginDto)

        BDDMockito.given(userService.isActive(eq(loginDto)))
                .willReturn(false)

        expect:
        mockMvc.perform(
                post("/api/login")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(jsonRequest)
        )
                .andExpect(status().isForbidden())

    }

    @WithMockUser
    def "POST api/login returns 401 positive test scenario"() {
        given:
        def loginDto = fakeLoginDto

        def jsonRequest = TestSerializer.serialize(loginDto)

        BDDMockito.given(userService.isActive(eq(loginDto)))
                .willReturn(true)
        BDDMockito.given(securityService.isPasswordMatch(eq(loginDto)))
                .willReturn(false)

        expect:
        mockMvc.perform(
                post("/api/login")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(jsonRequest)
        )
                .andExpect(status().isUnauthorized())

    }

    @WithMockUser
    def "POST api/login for type B returns 200 positive test scenario"() {
        given:
        def loginDto = fakeLoginDto

        def user = fakeUser
        user.setType('B' as Character)
        def business = fakeVerifiedBusiness
        def result = BusinessMapper.toDtoWithUser(business)

        def jsonRequest = TestSerializer.serialize(loginDto)
        def jsonResponse = TestSerializer.serialize(result)

        BDDMockito.given(userService.isActive(eq(loginDto)))
                .willReturn(true)
        BDDMockito.given(securityService.isPasswordMatch(eq(loginDto)))
                .willReturn(true)
        BDDMockito.given(userService.getUserByEmail(eq(loginDto.getEmail())))
                .willReturn(user)
        BDDMockito.given(businessService.get(eq(user.getId())))
                .willReturn(business)

        expect:
        mockMvc.perform(
                post("/api/login")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(jsonRequest)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))

        BDDMockito.verify(securityService, times(1))
                .buildSecurityContext(eq(loginDto), any(HttpServletRequest.class))

    }

    @WithMockUser
    def "POST api/login for USER returns 200 positive test scenario"() {
        given:
        def loginDto = fakeLoginDto

        def user = fakeUser
        user.setType('U' as Character)
        def result = UserMapper.toDto(user)

        def jsonRequest = TestSerializer.serialize(loginDto)
        def jsonResponse = TestSerializer.serialize(result)

        BDDMockito.given(userService.isActive(eq(loginDto)))
                .willReturn(true)
        BDDMockito.given(securityService.isPasswordMatch(eq(loginDto)))
                .willReturn(true)
        BDDMockito.given(userService.getUserByEmail(eq(loginDto.getEmail())))
                .willReturn(user)

        expect:
        mockMvc.perform(
                post("/api/login")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(jsonRequest)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))

        BDDMockito.verify(securityService, times(1))
                .buildSecurityContext(eq(loginDto), any(HttpServletRequest.class))

    }

    @WithMockUser(authorities = ['CUSTOMER'])
    def "POST api/password/change returns 200 positive test scenario"() {
        given:
        def id = 1l
        def dto = fakeChangePasswordDto

        def jsonRequest = TestSerializer.serialize(dto)

        expect:
        mockMvc.perform(
                post("/api/password/change")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(jsonRequest)
                        .param('id', id.toString())
        )
                .andExpect(status().isOk())

        BDDMockito.verify(userService, times(1))
                .changePassword(eq(id), eq(dto))

    }

    @WithMockUser
    def "POST api/reset returns 200 positive test scenario"() {
        given:
        def token = 'SAMPLE TOKEN'
        def dto = fakeNewPasswordDto

        def jsonRequest = TestSerializer.serialize(dto)

        expect:
        mockMvc.perform(
                post("/api/reset")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(jsonRequest)
                        .param('token', token)
        )
                .andExpect(status().isOk())

        BDDMockito.verify(userService, times(1))
                .setNewPassword(eq(token), eq(dto))

    }

    @WithMockUser(authorities = ['ADMIN'])
    def "POST api/{id}/block returns 200 positive test scenario"() {
        given:
        def id = 1L

        expect:
        mockMvc.perform(
                post("/api/{id}/block", id)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        )
                .andExpect(status().isOk())

        BDDMockito.verify(userService, times(1))
                .block(eq(id))

    }

    @WithMockUser(authorities = ['ADMIN'])
    def "POST api/{id}/activate returns 200 positive test scenario"() {
        given:
        def id = 1L

        expect:
        mockMvc.perform(
                post("/api/{id}/activate", id)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        )
                .andExpect(status().isOk())

        BDDMockito.verify(userService, times(1))
                .activate(eq(id))

    }

    @WithMockUser(authorities = ['ADMIN'])
    def "GET api/users/all returns 200 positive test scenario"() {
        given:
        def keyword = "sample keyword"
        def pageNo = 1
        def pageSize = 50
        def sortBy = 'id'
        def order = 'desc'

        def customPage = CustomPage.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .sortBy(sortBy)
                .order(order)
                .build()

        def count = 1L
        def user = fakeUser
        def users = ImmutableList.of(user)
        def resultList = ImmutableList.of(UserMapper.toDto(user))
        def result = new TableDto<>(
                TableDto.MetaDto.builder()
                        .total(count)
                        .pageNo(pageNo)
                        .pageSize(pageSize)
                        .sortBy(sortBy)
                        .build(),
                resultList)
        def jsonResponse = TestSerializer.serialize(result)

        BDDMockito.given(userService.list(eq(customPage), eq(keyword)))
                .willReturn(users)
        BDDMockito.given(userService.count(keyword))
                .willReturn(count)

        expect:
        mockMvc.perform(
                get("/api/users/all")
                        .param("keyword", keyword)
                        .param("pageNo", pageNo.toString())
                        .param("pageSize", pageSize.toString())
                        .param("sortBy", sortBy)
                        .param("order", order)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))
    }

    @WithMockUser(authorities = ['ADMIN'])
    def "GET api/users returns 200 positive test scenario"() {
        given:
        def id = 1L

        def userDto = fakeUserDto

        def jsonResponse = TestSerializer.serialize(userDto)

        BDDMockito.given(userService.getWithDetail(eq(id)))
                .willReturn(userDto)

        expect:
        mockMvc.perform(
                get("/api/users")
                .param('id', id.toString())
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))
    }

    @WithMockUser
    def "GET api/logout returns 200 positive test scenario"() {

        expect:
        mockMvc.perform(
                get("/api/logout")
        )
                .andExpect(status().isOk())
    }

}
