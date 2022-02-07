package pjatk.pro.event_organizer_app.availability.optionalservice.controller

import com.google.common.collect.ImmutableList
import org.mockito.BDDMockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import pjatk.pro.event_organizer_app.availability.mapper.AvailabilityMapper
import pjatk.pro.event_organizer_app.availability.optionalservice.service.OptionalServiceAvailabilityService
import pjatk.pro.event_organizer_app.test_helper.TestSerializer
import pjatk.pro.event_organizer_app.trait.availability.AvailabilityTrait
import pjatk.pro.event_organizer_app.trait.availability.optionalservice.OptionalServiceAvailabilityTrait
import spock.lang.Specification

import static org.mockito.ArgumentMatchers.eq
import static org.mockito.Mockito.times
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(controllers = [OptionalServiceAvailabilityController.class])
class OptionalServiceAvailabilityControllerTest extends Specification
        implements OptionalServiceAvailabilityTrait, AvailabilityTrait {

    @Autowired
    private MockMvc mockMvc

    @MockBean
    private OptionalServiceAvailabilityService optionalServiceAvailabilityService

    @WithMockUser
    def "GET api/availability/service returns 200 positive test scenario"() {
        given:
        def id = 1L
        def date = "01.01.2021"

        def optionalServiceAvailability = fakeOptionalServiceAvailability
        def optionalServiceAvailabilityList = ImmutableList.of(optionalServiceAvailability)
        def resultList = ImmutableList.of(AvailabilityMapper.toDto(optionalServiceAvailability))
        def jsonResponse = TestSerializer.serialize(resultList)

        BDDMockito.given(optionalServiceAvailabilityService.findAllByServiceIdAndDate(eq(id), eq(date)))
                .willReturn(optionalServiceAvailabilityList)

        expect:
        mockMvc.perform(
                get("/api/availability/service")
                        .param("id", id.toString())
                        .param("date", date)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))
    }

    @WithMockUser(authorities = ['BUSINESS'])
    def "POST api/availability/service returns 200 positive test scenario"() {
        given:
        def id = 1L

        def availabilityDtos = ImmutableList.of(fakeAvailabilityDto)
        def dtos = availabilityDtos.toArray()
        def optionalServiceAvailabilityList = ImmutableList.of(fakeOptionalServiceAvailability)
        def resultList = ImmutableList.of(AvailabilityMapper.toDto(fakeOptionalServiceAvailability))

        def jsonRequest = TestSerializer.serialize(dtos)
        def jsonResponse = TestSerializer.serialize(resultList)

        BDDMockito.given(optionalServiceAvailabilityService.update(eq(availabilityDtos), eq(id), eq(true)))
                .willReturn(optionalServiceAvailabilityList)

        expect:
        mockMvc.perform(
                post("/api/availability/service")
                        .param("id", id.toString())
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(jsonRequest)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))
    }

    @WithMockUser(authorities = ['BUSINESS'])
    def "GET api/availability/service/allowed/period returns 200 positive test scenario"() {
        given:
        def id = 1L
        def dateFrom = "01.01.2021"
        def dateTo = "01.01.2021"

        def optionalServiceAvailability = fakeOptionalServiceAvailability
        def optionalServiceAvailabilityList = ImmutableList.of(optionalServiceAvailability)
        def resultList = ImmutableList.of(AvailabilityMapper.toDto(optionalServiceAvailability))
        def jsonResponse = TestSerializer.serialize(resultList)

        BDDMockito.given(optionalServiceAvailabilityService.findAllByServiceIdAndDatePeriod(eq(id), eq(dateFrom), eq(dateTo)))
                .willReturn(optionalServiceAvailabilityList)

        expect:
        mockMvc.perform(
                get("/api/availability/service/allowed/period")
                        .param("id", id.toString())
                        .param("dateFrom", dateFrom)
                        .param("dateTo", dateTo)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))
    }


    @WithMockUser(authorities = ['BUSINESS'])
    def "DELETE api/availability/service returns 200 positive test scenario"() {
        given:
        def id = 1L

        expect:
        mockMvc.perform(
                delete("/api/availability/service")
                        .param('id', id.toString())
        )
                .andExpect(status().isOk())

        BDDMockito.verify(optionalServiceAvailabilityService, times(1))
                .deleteById(eq(id))
    }
}
