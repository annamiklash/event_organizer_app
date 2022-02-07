package pjatk.pro.event_organizer_app.event.controller

import com.google.common.collect.ImmutableList
import org.mockito.BDDMockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import pjatk.pro.event_organizer_app.common.paginator.CustomPage
import pjatk.pro.event_organizer_app.enums.CustomerReservationTabEnum
import pjatk.pro.event_organizer_app.event.mapper.OrganizedEventMapper
import pjatk.pro.event_organizer_app.event.service.OrganizedEventService
import pjatk.pro.event_organizer_app.test_helper.TestSerializer
import pjatk.pro.event_organizer_app.trait.event.OrganizedEventTrait
import spock.lang.Specification

import static org.mockito.ArgumentMatchers.eq
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(controllers = [OrganizedEventController.class])
class OrganizedEventControllerTest extends Specification
        implements OrganizedEventTrait {

    @Autowired
    private MockMvc mockMvc

    @MockBean
    private OrganizedEventService organizedEventService

    @WithMockUser(authorities = ['ADMIN'])
    def "GET api/events returns 200 positive test scenario"() {
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

        def organizedEventDto = fakeOrganizedEventDto
        def resultList = ImmutableList.of(organizedEventDto)
        def jsonResponse = TestSerializer.serialize(resultList)

        BDDMockito.given(organizedEventService.list(eq(customPage), eq(keyword)))
                .willReturn(resultList)

        expect:
        mockMvc.perform(
                get('/api/events')
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
    def "GET api/events/detail returns 200 positive test scenario"() {
        given:
        def eventId = 1L
        def customerId = 2L

        def organizedEvent = fakeFullOrganizedEvent
        def result = OrganizedEventMapper.toDtoWithDetail(organizedEvent)
        def jsonResponse = TestSerializer.serialize(result)

        BDDMockito.given(organizedEventService.getWithDetail(eq(eventId), eq(customerId)))
                .willReturn(organizedEvent)

        expect:
        mockMvc.perform(
                get('/api/events/detail')
                        .param('eventId', eventId.toString())
                        .param('customerId', customerId.toString())
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))

    }

    @WithMockUser(authorities = ['ADMIN'])
    def "GET api/events/customer returns 200 positive test scenario"() {
        given:
        def customerId = 1L
        def tab = CustomerReservationTabEnum.ALL

        def organizedEvent = fakeFullOrganizedEvent
        def organizedEventList = ImmutableList.of(organizedEvent)
        def organizedEventDto = OrganizedEventMapper.toDtoWithLocationCustomer(organizedEvent)
        def resultList = ImmutableList.of(organizedEventDto)
        def jsonResponse = TestSerializer.serialize(resultList)

        BDDMockito.given(organizedEventService.getAllByCustomerIdAndTab(eq(customerId), eq(tab)))
                .willReturn(organizedEventList)

        expect:
        mockMvc.perform(
                get('/api/events/customer')
                        .param('customerId', customerId.toString())
                        .param('tab', tab.name())
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))

    }

    @WithMockUser(authorities = ['ADMIN'])
    def "POST api/events returns 200 positive test scenario"() {
        given:
        def customerId = 1L
        def dto = fakeOrganizedEventDto

        def organizedEvent = fakeFullOrganizedEvent
        def result = OrganizedEventMapper.toDtoWithCustomer(organizedEvent)

        def jsonRequest = TestSerializer.serialize(dto)
        def jsonResponse = TestSerializer.serialize(result)

        BDDMockito.given(organizedEventService.create(eq(customerId), eq(dto)))
                .willReturn(organizedEvent)

        expect:
        mockMvc.perform(
                post('/api/events')
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(jsonRequest)
                        .param('customerId', customerId.toString())
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))

    }
}
