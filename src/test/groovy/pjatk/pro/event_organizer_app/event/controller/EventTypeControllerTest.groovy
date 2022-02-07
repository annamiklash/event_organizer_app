package pjatk.pro.event_organizer_app.event.controller

import com.google.common.collect.ImmutableList
import org.mockito.BDDMockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import pjatk.pro.event_organizer_app.event.mapper.EventTypeMapper
import pjatk.pro.event_organizer_app.event.service.EventTypeService
import pjatk.pro.event_organizer_app.test_helper.TestSerializer
import pjatk.pro.event_organizer_app.trait.event.EventTypeTrait
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(controllers = [EventTypeController.class])
class EventTypeControllerTest extends Specification
        implements EventTypeTrait {

    @Autowired
    private MockMvc mockMvc

    @MockBean
    private EventTypeService eventTypeService

    @WithMockUser
    def "GET api/events/types/allowed/all returns 200 positive test scenario"() {
        given:
        def eventType = fakeEventType
        def eventTypeList = ImmutableList.of(eventType)
        def resultList = ImmutableList.of(EventTypeMapper.toDto(eventType))
        def jsonResponse = TestSerializer.serialize(resultList)

        BDDMockito.given(eventTypeService.findAll())
                .willReturn(eventTypeList)

        expect:
        mockMvc.perform(get('/api/events/types/allowed/all'))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))

    }

}
