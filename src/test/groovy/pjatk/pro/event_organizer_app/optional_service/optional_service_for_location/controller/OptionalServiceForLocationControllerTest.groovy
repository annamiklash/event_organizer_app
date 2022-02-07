package pjatk.pro.event_organizer_app.optional_service.optional_service_for_location.controller

import com.google.common.collect.ImmutableList
import org.mockito.BDDMockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import pjatk.pro.event_organizer_app.event.mapper.OrganizedEventMapper
import pjatk.pro.event_organizer_app.optional_service.optional_service_for_location.mapper.OptionalServiceForLocationMapper
import pjatk.pro.event_organizer_app.optional_service.optional_service_for_location.service.OptionalServiceForLocationService
import pjatk.pro.event_organizer_app.test_helper.TestSerializer
import pjatk.pro.event_organizer_app.trait.event.OrganizedEventTrait
import pjatk.pro.event_organizer_app.trait.optional_service.OptionalServiceForChosenLocationTrait
import spock.lang.Specification

import static org.mockito.ArgumentMatchers.eq
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(controllers = [OptionalServiceForLocationController.class])
class OptionalServiceForLocationControllerTest extends Specification
        implements OptionalServiceForChosenLocationTrait,
                OrganizedEventTrait{

    @Autowired
    private MockMvc mockMvc

    @MockBean
    private OptionalServiceForLocationService optionalServiceForLocationService

    @WithMockUser(authorities = ['BUSINESS'])
    def "PUT api/event/service/confirm returns 200 positive test scenario"() {
        given:
        def id = 1L
        def eventId = 2L

        def optionalServiceForChosenLocation = fakeOptionalServiceForChosenLocation
        def result = OptionalServiceForLocationMapper.toDto(optionalServiceForChosenLocation)

        def jsonResponse = TestSerializer.serialize(result)

        BDDMockito.given(optionalServiceForLocationService.confirmReservation(eq(id), eq(eventId)))
                .willReturn(optionalServiceForChosenLocation)

        expect:
        mockMvc.perform(
                put("/api/event/service/confirm")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param('id', id.toString())
                        .param('eventId', eventId.toString())
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))
    }

    @WithMockUser(authorities = ['BUSINESS'])
    def "GET api/event/service/status returns 200 positive test scenario"() {
        given:
        def statusStr = 'SAMPLE STATUS'
        def locationId = 1L

        def optionalServiceForChosenLocation = fakeOptionalServiceForChosenLocation
        def optionalServiceForChosenLocationList = ImmutableList.of(optionalServiceForChosenLocation)
        def optionalServiceForChosenLocationDto = OptionalServiceForLocationMapper.toDto(optionalServiceForChosenLocation)
        def resultList = ImmutableList.of(optionalServiceForChosenLocationDto)

        def jsonResponse = TestSerializer.serialize(resultList)

        BDDMockito.given(optionalServiceForLocationService.listAllByStatus(eq(locationId), eq(statusStr)))
                .willReturn(optionalServiceForChosenLocationList)

        expect:
        mockMvc.perform(
                get("/api/event/service/status")
                        .param('status', statusStr)
                        .param('locationId', locationId.toString())
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))
    }

    @WithMockUser(authorities = ['BUSINESS'])
    def "GET api/event/service/business/status returns 200 positive test scenario"() {
        given:
        def statusStr = 'SAMPLE STATUS'
        def businessId = 1L

        def optionalServiceForChosenLocation = fakeOptionalServiceForChosenLocation
        optionalServiceForChosenLocation.getLocationForEvent().setEvent(fakeFullOrganizedEvent)
        def optionalServiceForChosenLocationList =
                ImmutableList.of(optionalServiceForChosenLocation)
        def optionalServiceForChosenLocationDto =
                OptionalServiceForLocationMapper.toDtoWithLocationAndEvent(optionalServiceForChosenLocation)
        def resultList = ImmutableList.of(optionalServiceForChosenLocationDto)

        def jsonResponse = TestSerializer.serialize(resultList)

        BDDMockito.given(optionalServiceForLocationService.listAllByStatusAndBusinessId(eq(businessId), eq(statusStr)))
                .willReturn(optionalServiceForChosenLocationList)

        expect:
        mockMvc.perform(
                get("/api/event/service/business/status")
                        .param('status', statusStr)
                        .param('businessId', businessId.toString())
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))
    }

    @WithMockUser(authorities = ['ADMIN'])
    def "POST api/event/service returns 200 positive test scenario"() {
        given:
        def customerId = 1L
        def eventId = 2L
        def serviceId = 3L
        def dto = fakeOptionalServiceForChosenLocationDto

        def optionalServiceForChosenLocation = fakeOptionalServiceForChosenLocation
        optionalServiceForChosenLocation.getLocationForEvent().setEvent(fakeFullOrganizedEvent)
        def result = OrganizedEventMapper.toDtoWithServices(fakeFullOrganizedEvent)

        def jsonRequest = TestSerializer.serialize(dto)
        def jsonResponse = TestSerializer.serialize(result)

        BDDMockito.given(optionalServiceForLocationService.create(eq(customerId), eq(eventId), eq(serviceId), eq(dto)))
                .willReturn(optionalServiceForChosenLocation)

        expect:
        mockMvc.perform(
                post("/api/event/service")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(jsonRequest)
                        .param('customerId', customerId.toString())
                        .param('eventId', eventId.toString())
                        .param('serviceId', serviceId.toString())
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))
    }

    @WithMockUser(authorities = ['ADMIN'])
    def "DELETE api/event/service/cancel returns 200 positive test scenario"() {
        given:
        def id = 1L

        def optionalServiceForChosenLocation = fakeOptionalServiceForChosenLocation
        def result = OptionalServiceForLocationMapper.toDto(optionalServiceForChosenLocation)

        def jsonResponse = TestSerializer.serialize(result)

        BDDMockito.given(optionalServiceForLocationService.cancelReservation(eq(id)))
                .willReturn(optionalServiceForChosenLocation)

        expect:
        mockMvc.perform(
                delete("/api/event/service/cancel")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param('id', id.toString())
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))
    }


}
