package pjatk.pro.event_organizer_app.location.locationforevent.controller

import com.google.common.collect.ImmutableList
import org.mockito.BDDMockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import pjatk.pro.event_organizer_app.event.mapper.OrganizedEventMapper
import pjatk.pro.event_organizer_app.event.model.EventType
import pjatk.pro.event_organizer_app.location.locationforevent.mapper.LocationForEventMapper
import pjatk.pro.event_organizer_app.location.locationforevent.service.LocationForEventService
import pjatk.pro.event_organizer_app.test_helper.TestSerializer
import pjatk.pro.event_organizer_app.trait.address.AddressTrait
import pjatk.pro.event_organizer_app.trait.customer.CustomerTrait
import pjatk.pro.event_organizer_app.trait.location.LocationTrait
import pjatk.pro.event_organizer_app.trait.location.locationforevent.LocationForEventTrait
import spock.lang.Specification

import static org.mockito.ArgumentMatchers.eq
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(controllers = [LocationForEventController.class])
class LocationForEventControllerTest extends Specification
        implements LocationForEventTrait, AddressTrait, CustomerTrait, LocationTrait {

    @Autowired
    private MockMvc mockMvc

    @MockBean
    private LocationForEventService locationForEventService

    @WithMockUser(authorities = ['BUSINESS'])
    def "PUT api/event/location/confirm returns 200 positive test scenario"() {
        given:
        def id = 1l
        def eventId = 2l

        def locationReservation = fakeFullLocationForEvent
        def location = locationReservation.getLocation()
        location.setLocationAddress(fakeAddress)

        locationReservation.setLocation(location)
        def result = LocationForEventMapper.toDto(locationReservation)

        def jsonResponse = TestSerializer.serialize(result)

        BDDMockito.given(locationForEventService.confirmReservation(eq(id), eq(eventId)))
                .willReturn(fakeFullLocationForEvent)

        expect:
        mockMvc.perform(
                put("/api/event/location/confirm")
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
    def "GET api/event/location/status returns 200 positive test scenario"() {
        given:
        def statusStr = 'SAMPLE STATUS'
        def locationId = 1L

        def locationForEvent = fakeFullLocationForEvent
        def event = locationForEvent.getEvent()
        def customer = fakeCustomer
        event.setCustomer(customer)
        event.setEventType(
                EventType.builder()
                        .id(1l)
                        .type('WEDDING')
                        .build())
        def location = locationForEvent.getLocation()
        location.setLocationAddress(fakeAddress)
        locationForEvent.setLocation(location)
        locationForEvent.setEvent(event)

        def locationForEventList = ImmutableList.of(locationForEvent)
        def locationDorEventDto = LocationForEventMapper.toDtoWithLocationAndEvent(locationForEvent)
        def resultList = ImmutableList.of(locationDorEventDto)

        def jsonResponse = TestSerializer.serialize(resultList)

        BDDMockito.given(locationForEventService.listAllByStatus(eq(locationId), eq(statusStr)))
                .willReturn(locationForEventList)

        expect:
        mockMvc.perform(
                get("/api/event/location/status")
                        .param("status", statusStr)
                        .param("locationId", locationId.toString())
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))

    }

    @WithMockUser(authorities = ['BUSINESS'])
    def "GET api/event/location/business/status returns 200 positive test scenario"() {
        given:
        def statusStr = 'SAMPLE STATUS'
        def businessId = 1L

        def locationForEvent = fakeFullLocationForEvent
        def event = locationForEvent.getEvent()
        def customer = fakeCustomer
        event.setCustomer(customer)
        event.setEventType(
                EventType.builder()
                        .id(1l)
                        .type('WEDDING')
                        .build())
        def location = locationForEvent.getLocation()
        location.setLocationAddress(fakeAddress)
        locationForEvent.setLocation(location)
        locationForEvent.setEvent(event)

        def locationForEventList = ImmutableList.of(locationForEvent)
        def locationDorEventDto = LocationForEventMapper.toDtoWithEvent(locationForEvent)
        def resultList = ImmutableList.of(locationDorEventDto)

        def jsonResponse = TestSerializer.serialize(resultList)

        BDDMockito.given(locationForEventService.listAllByStatusAndBusinessId(eq(businessId), eq(statusStr)))
                .willReturn(locationForEventList)

        expect:
        mockMvc.perform(
                get("/api/event/location/business/status")
                        .param("businessId", businessId.toString())
                        .param("status", statusStr)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))
    }

    @WithMockUser(authorities = ['CUSTOMER'])
    def "POST api/event/location returns 200 positive test scenario"() {
        given:
        def customerId = 1L
        def eventId = 2L
        def locationId = 3L
        def dto = fakeLocationForEvent

        def locationForEvent = fakeFullLocationForEvent
        def location = fakeFullLocation
        location.setLocationAddress(fakeAddressWithId)

        locationForEvent.setLocation(location)
        fakeFullLocationForEvent.setLocation(fakeFullLocation)

        def event = locationForEvent.getEvent()
        def customer = fakeCustomer
        event.setCustomer(customer)
        event.setLocationForEvent(Set.of(locationForEvent))
        event.setEventType(
                EventType.builder()
                        .id(1l)
                        .type('WEDDING')
                        .build())

        locationForEvent.setEvent(event)
        def locationF = locationForEvent

        def result = OrganizedEventMapper.toDtoWithLocation(locationF.getEvent())

        def jsonRequest = TestSerializer.serialize(dto)
        def jsonResponse = TestSerializer.serialize(result)

        BDDMockito.given(locationForEventService.create(eq(customerId), eq(eventId), eq(locationId), eq(dto)))
                .willReturn(locationF)

        expect:
        mockMvc.perform(
                post("/api/event/location")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(jsonRequest)
                        .param('customerId', customerId.toString())
                        .param('eventId', eventId.toString())
                        .param('locationId', locationId.toString())
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))
    }

    @WithMockUser(authorities = ['BUSINESS'])
    def "DELETE api/event/location/cancel returns 204 positive test scenario"() {
        given:
        def id = 1L
        def locationForEvent = fakeFullLocationForEvent
        def location = fakeFullLocation
        location.setLocationAddress(fakeAddressWithId)

        locationForEvent.setLocation(location)
        fakeFullLocationForEvent.setLocation(fakeFullLocation)

        def event = locationForEvent.getEvent()
        def customer = fakeCustomer
        event.setCustomer(customer)
        event.setLocationForEvent(Set.of(locationForEvent))
        event.setEventType(
                EventType.builder()
                        .id(1l)
                        .type('WEDDING')
                        .build())

        locationForEvent.setEvent(event)
        def locationF = locationForEvent
        def result = LocationForEventMapper.toDtoWithLocationAndEvent(locationF)

        def jsonResponse = TestSerializer.serialize(result)

        BDDMockito.given(locationForEventService.cancelReservation(eq(id)))
                .willReturn(locationForEvent)
        expect:
        mockMvc.perform(
                delete("/api/event/location/cancel")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param('id', id.toString())
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))

    }
}
