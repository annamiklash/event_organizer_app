package pjatk.pro.event_organizer_app.customer.guest.controller

import com.google.common.collect.ImmutableList
import org.mockito.BDDMockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import pjatk.pro.event_organizer_app.common.paginator.CustomPage
import pjatk.pro.event_organizer_app.customer.guest.mapper.GuestMapper
import pjatk.pro.event_organizer_app.customer.guest.service.GuestService
import pjatk.pro.event_organizer_app.test_helper.TestSerializer
import pjatk.pro.event_organizer_app.trait.customer.guest.GuestTrait
import spock.lang.Specification

import static org.mockito.ArgumentMatchers.eq
import static org.mockito.Mockito.times
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(controllers = [GuestController.class])
class GuestControllerTest extends Specification
        implements GuestTrait {

    @Autowired
    private MockMvc mockMvc

    @MockBean
    private GuestService guestService

    @WithMockUser(authorities = ['ADMIN'])
    def "GET api/guests/all returns 200 positive test scenario"() {
        given:
        def keyword = 'keyword'
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

        def guest = fakeGuest
        def guestList = ImmutableList.of(guest)
        def resultList = ImmutableList.of(GuestMapper.toDto(guest))
        def jsonResponse = TestSerializer.serialize(resultList)

        BDDMockito.given(guestService.list(eq(customPage), eq(keyword)))
                .willReturn(guestList)

        expect:
        mockMvc.perform(
                get('/api/guests/all')
                        .param('keyword', keyword)
                        .param('pageNo', pageNo.toString())
                        .param('pageSize', pageSize.toString())
                        .param('sortBy', sortBy)
                        .param('order', order)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))

    }

    @WithMockUser(authorities = ['ADMIN'])
    def "GET api/guests/customer returns 200 positive test scenario"() {
        given:
        def customerId = 1L

        def guest = fakeGuest
        def guestList = ImmutableList.of(guest)
        def resultList = ImmutableList.of(GuestMapper.toDto(guest))
        def jsonResponse = TestSerializer.serialize(resultList)

        BDDMockito.given(guestService.listAllByCustomerId(eq(customerId)))
                .willReturn(guestList)

        expect:
        mockMvc.perform(
                get('/api/guests/customer')
                        .param('customerId', customerId.toString())
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))

    }

    @WithMockUser(authorities = ['CUSTOMER'])
    def "GET api/guests with id parameter returns 200 positive test scenario"() {
        given:
        def id = 1L

        def guest = fakeGuest
        def result = GuestMapper.toDto(guest)
        def jsonResponse = TestSerializer.serialize(result)

        BDDMockito.given(guestService.get(eq(id)))
                .willReturn(guest)

        expect:
        mockMvc.perform(
                get('/api/guests')
                        .param('id', id.toString())
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))

    }

    @WithMockUser(authorities = ['CUSTOMER'])
    def "POST api/guests/new returns 200 positive test scenario"() {
        given:
        def customerId = 1L
        def dto = fakeGuestDTO

        def guest = fakeGuest
        def result = GuestMapper.toDtoWithCustomer(guest)

        def jsonRequest = TestSerializer.serialize(dto)
        def jsonResponse = TestSerializer.serialize(result)

        BDDMockito.given(guestService.create(eq(customerId), eq(dto)))
                .willReturn(guest)

        expect:
        mockMvc.perform(
                post("/api/guests/new")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(jsonRequest)
                        .param('customerId', customerId.toString())
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))
    }

    @WithMockUser(authorities = ['ADMIN'])
    def "DELETE api/guests returns 204 positive test scenario"() {
        given:
        def customerId = 1L
        def id = 2L

        expect:
        mockMvc.perform(
                delete("/api/guests")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param('customerId', customerId.toString())
                        .param('id', id.toString())
        )
                .andExpect(status().isNoContent())

        BDDMockito.verify(guestService, times(1))
                .delete(eq(customerId), eq(id))
    }

}
