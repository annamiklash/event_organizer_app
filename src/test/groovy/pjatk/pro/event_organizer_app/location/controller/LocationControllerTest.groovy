package pjatk.pro.event_organizer_app.location.controller

import com.google.common.collect.ImmutableList
import org.mockito.BDDMockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import pjatk.pro.event_organizer_app.common.paginator.CustomPage
import pjatk.pro.event_organizer_app.location.mapper.LocationMapper
import pjatk.pro.event_organizer_app.location.service.LocationService
import pjatk.pro.event_organizer_app.table.TableDto
import pjatk.pro.event_organizer_app.test_helper.TestSerializer
import pjatk.pro.event_organizer_app.trait.location.FilterLocationsDtoTrait
import pjatk.pro.event_organizer_app.trait.location.LocationTrait
import spock.lang.Specification

import static org.mockito.ArgumentMatchers.eq
import static org.mockito.Mockito.times
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(controllers = [LocationController.class])
class LocationControllerTest extends Specification
        implements LocationTrait,
                FilterLocationsDtoTrait {

    @Autowired
    private MockMvc mockMvc

    @MockBean
    private LocationService locationService

    @WithMockUser
    def "GET api/locations/allowed/all returns 200 positive test scenario"() {
        given:
        def keyword = 'keyword'
        def pageNo = 1
        def pageSize = 50
        def sortBy = 'id'
        def order = 'desc'

        def count = 1L

        def customPage = CustomPage.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .sortBy(sortBy)
                .order(order)
                .build()

        def location = fakeFullLocation
        def locationList = ImmutableList.of(location)
        def locationDto = LocationMapper.toDto(location)
        def resultList = ImmutableList.of(locationDto)
        def locationTableDto =
                new TableDto<>(new TableDto.MetaDto(count, pageNo, pageSize, sortBy), resultList)
        def jsonResponse = TestSerializer.serialize(locationTableDto)

        BDDMockito.given(locationService.list(eq(customPage), eq(keyword)))
                .willReturn(locationList)
        BDDMockito.given(locationService.count(eq(keyword)))
                .willReturn(count)

        expect:
        mockMvc.perform(
                get('/api/locations/allowed/all')
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

    @WithMockUser
    def "POST api/locations/allowed/search returns 200 positive test scenario"() {
        given:
        def customerId = 1L
        def dto = fakeFilterLocationsDto

        def count = 1L

        def location = fakeFullLocation
        def locationList = ImmutableList.of(location)
        def locationDto = LocationMapper.toDto(location)
        def resultList = ImmutableList.of(locationDto)
        def locationTableDto =
                new TableDto<>(new TableDto.MetaDto((long) count, null, null, null), resultList)

        def jsonRequest = TestSerializer.serialize(dto)
        def jsonResponse = TestSerializer.serialize(locationTableDto)

        BDDMockito.given(locationService.search(eq(dto)))
                .willReturn(locationList)

        expect:
        mockMvc.perform(
                post("/api/locations/allowed/search")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(jsonRequest)
                        .param('customerId', customerId.toString())
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))
    }


    @WithMockUser
    def "GET api/locations/allowed/available returns 200 positive test scenario"() {
        given:
        def locationId = 1L
        def date = '2017-01-01'
        def timeFrom = '10:00'
        def timeTo = '12:00'

        def result = true

        BDDMockito.given(locationService.isAvailable(eq(locationId), eq(date), eq(timeFrom), eq(timeTo)))
                .willReturn(result)

        expect:
        mockMvc.perform(
                get("/api/locations/allowed/available")
                        .param('locationId', locationId.toString())
                        .param('date', date)
                        .param('timeFrom', timeFrom)
                        .param('timeTo', timeTo)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(result.toString()))
    }

    @WithMockUser
    def "GET api/locations/allowed/cities returns 200 positive test scenario"() {
        given:
        def result = ["Warszawa", "Wrocław"]

        def jsonResponse = TestSerializer.serialize(result)

        BDDMockito.given(locationService.getCities())
                .willReturn(result)

        expect:
        mockMvc.perform(
                get("/api/locations/allowed/cities")
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))
    }

    @WithMockUser
    def "GET api/locations/allowed/{id}/detail returns 200 positive test scenario"() {
        given:
        def id = 1L

        def location = fakeFullLocation
        def result = LocationMapper.toDtoWithDetail(location)

        def jsonResponse = TestSerializer.serialize(result)

        BDDMockito.given(locationService.getWithDetail(eq(id)))
                .willReturn(location)

        expect:
        mockMvc.perform(
                get("/api/locations/allowed/{id}/detail", id)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))
    }


    @WithMockUser(authorities = ['ADMIN'])
    def "POST api/locations returns 200 positive test scenario"() {
        given:
        def dto = fakeLocationDto

        def location = fakeFullLocation
        def locationDto = LocationMapper.toDto(location)

        def jsonRequest = TestSerializer.serialize(dto)
        def jsonResponse = TestSerializer.serialize(locationDto)

        BDDMockito.given(locationService.create(eq(dto)))
                .willReturn(location)

        expect:
        mockMvc.perform(
                post("/api/locations")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(jsonRequest)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))
    }

    @WithMockUser(authorities = ['ADMIN'])
    def "PUT api/locations/edit returns 200 positive test scenario"() {
        given:
        def dto = fakeLocationDto
        def id = 1L

        def location = fakeFullLocation
        def locationDto = LocationMapper.toDtoWithDetailWithCaterings(location)

        def jsonRequest = TestSerializer.serialize(dto)
        def jsonResponse = TestSerializer.serialize(locationDto)

        BDDMockito.given(locationService.edit(eq(dto), eq(id)))
                .willReturn(location)

        expect:
        mockMvc.perform(
                put("/api/locations/edit")
                        .param('id', id.toString())
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(jsonRequest)

        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))
    }


    @WithMockUser(authorities = ['ADMIN'])
    def "GET api/locations/{id}/availability returns 200 positive test scenario"() {
        given:
        def id = 1L
        def date = '2017-01-01'

        def location = fakeFullLocation
        def result = LocationMapper.toDto(location)

        def jsonResponse = TestSerializer.serialize(result)

        BDDMockito.given(locationService.getWithAvailability(eq(id), eq(date)))
                .willReturn(location)

        expect:
        mockMvc.perform(
                get("/api/locations/{id}/availability", id)
                        .param('date', date)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))
    }


    @WithMockUser(authorities = ['CUSTOMER'])
    def "GET api/locations/business returns 200 positive test scenario"() {
        given:
        def id = 1L

        def location = fakeFullLocation
        def locationList = ImmutableList.of(location)
        def locationDto = LocationMapper.toDto(location)
        def resultList = ImmutableList.of(locationDto)

        def jsonResponse = TestSerializer.serialize(resultList)

        BDDMockito.given(locationService.getByBusinessId(eq(id)))
                .willReturn(locationList)

        expect:
        mockMvc.perform(
                get("/api/locations/business")
                        .param('id', id.toString())
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))
    }

    @WithMockUser(authorities = ['BUSINESS'])
    def "GET api/locations/catering returns 200 positive test scenario"() {
        given:
        def cateringId = 1L

        def location = fakeFullLocation
        def locationList = ImmutableList.of(location)
        def locationDto = LocationMapper.toDto(location)
        def resultList = ImmutableList.of(locationDto)

        def jsonResponse = TestSerializer.serialize(resultList)

        BDDMockito.given(locationService.getByCateringId(eq(cateringId)))
                .willReturn(locationList)

        expect:
        mockMvc.perform(
                get("/api/locations/allowed/catering")
                        .param('cateringId', cateringId.toString())
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))
    }

    @WithMockUser(authorities = ['ADMIN'])
    def "DELETE api/locations returns 204 positive test scenario"() {
        given:
        def id = 1L

        expect:
        mockMvc.perform(
                delete("/api/locations")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param('id', id.toString())
        )
                .andExpect(status().isNoContent())

        BDDMockito.verify(locationService, times(1))
                .delete(eq(id))
    }
}
