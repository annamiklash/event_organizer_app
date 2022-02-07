package pjatk.pro.event_organizer_app.availability.location.controller

import com.google.common.collect.ImmutableList
import org.mockito.BDDMockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import pjatk.pro.event_organizer_app.availability.location.service.LocationAvailabilityService
import pjatk.pro.event_organizer_app.availability.mapper.AvailabilityMapper
import pjatk.pro.event_organizer_app.test_helper.TestSerializer
import pjatk.pro.event_organizer_app.trait.availability.AvailabilityTrait
import pjatk.pro.event_organizer_app.trait.availability.LocationAvailabilityTrait
import spock.lang.Specification

import static org.mockito.ArgumentMatchers.eq
import static org.mockito.Mockito.times
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(controllers = [LocationAvailabilityController.class])
class LocationAvailabilityControllerTest extends Specification
        implements LocationAvailabilityTrait, AvailabilityTrait {

    @Autowired
    private MockMvc mockMvc

    @MockBean
    private LocationAvailabilityService locationAvailabilityService

    @WithMockUser(authorities = ['BUSINESS'])
    def "GET api/availability/location/allowed returns 200 positive test scenario"() {
        given:
        def id = 1L
        def date = "01.01.2021"

        def locationAvailability = fakeLocationAvailability
        def locationAvailabilityList = ImmutableList.of(locationAvailability)
        def resultList = ImmutableList.of(AvailabilityMapper.toDto(locationAvailability))
        def jsonResponse = TestSerializer.serialize(resultList)

        BDDMockito.given(locationAvailabilityService.findAllByLocationIdAndDate(eq(id), eq(date)))
                .willReturn(locationAvailabilityList)

        expect:
        mockMvc.perform(
                get("/api/availability/location/allowed")
                        .param("id", id.toString())
                        .param("date", date)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))
    }

    @WithMockUser(authorities = ['BUSINESS'])
    def "GET api/availability/location/allowed/period returns 200 positive test scenario"() {
        given:
        def id = 1L
        def dateFrom = "01.01.2021"
        def dateTo = "01.01.2021"

        def locationAvailability = fakeLocationAvailability
        def locationAvailabilityList = ImmutableList.of(locationAvailability)
        def resultList = ImmutableList.of(AvailabilityMapper.toDto(locationAvailability))
        def jsonResponse = TestSerializer.serialize(resultList)

        BDDMockito.given(locationAvailabilityService.findAllByLocationIdAndDatePeriod(eq(id), eq(dateFrom), eq(dateTo)))
                .willReturn(locationAvailabilityList)

        expect:
        mockMvc.perform(
                get("/api/availability/location/allowed/period")
                        .param("id", id.toString())
                        .param("dateFrom", dateFrom)
                        .param("dateTo", dateTo)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))
    }


    @WithMockUser(authorities = ['BUSINESS'])
    def "POST api/availability/location returns 200 positive test scenario"() {
        given:
        def id = 1L

        def locationAvailability = fakeLocationAvailability
        def availabilityDtos = ImmutableList.of(fakeAvailabilityDto)
        def dtos = availabilityDtos.toArray()
        def locationAvailabilityList = ImmutableList.of(locationAvailability)
        def resultList = ImmutableList.of(AvailabilityMapper.toDto(locationAvailability))

        def jsonRequest = TestSerializer.serialize(dtos)
        def jsonResponse = TestSerializer.serialize(resultList)

        BDDMockito.given(locationAvailabilityService.update(eq(availabilityDtos), eq(id), eq(true)))
                .willReturn(locationAvailabilityList)

        expect:
        mockMvc.perform(
                post("/api/availability/location")
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
    def "DELETE api/availability/location returns 200 positive test scenario"() {
        given:
        def id = 1L

        expect:
        mockMvc.perform(
                delete("/api/availability/location")
                        .param('id', id.toString())
        )
                .andExpect(status().isOk())

        BDDMockito.verify(locationAvailabilityService, times(1))
                .deleteById(eq(id))
    }


}
