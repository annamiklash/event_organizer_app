package pjatk.pro.event_organizer_app.optional_service.controller

import com.google.common.collect.ImmutableList
import org.mockito.BDDMockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import pjatk.pro.event_organizer_app.common.paginator.CustomPage
import pjatk.pro.event_organizer_app.optional_service.enums.KidPerformerTypeEnum
import pjatk.pro.event_organizer_app.optional_service.enums.LanguagesEnum
import pjatk.pro.event_organizer_app.optional_service.enums.MusicStyleEnum
import pjatk.pro.event_organizer_app.optional_service.enums.OptionalServiceTypeEnum
import pjatk.pro.event_organizer_app.optional_service.mapper.OptionalServiceMapper
import pjatk.pro.event_organizer_app.optional_service.model.dto.FilterOptionalServiceDto
import pjatk.pro.event_organizer_app.optional_service.service.OptionalServiceService
import pjatk.pro.event_organizer_app.reviews.service.service.OptionalServiceReviewService
import pjatk.pro.event_organizer_app.table.TableDto
import pjatk.pro.event_organizer_app.test_helper.TestSerializer
import pjatk.pro.event_organizer_app.trait.optional_service.OptionalServiceTrait
import spock.lang.Specification

import java.util.stream.Stream

import static org.mockito.ArgumentMatchers.eq
import static org.mockito.Mockito.times
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(controllers = [OptionalServiceController.class])
class OptionalServiceControllerTest extends Specification
        implements OptionalServiceTrait {

    @Autowired
    private MockMvc mockMvc

    @MockBean
    private OptionalServiceService optionalServiceService
    @MockBean
    private OptionalServiceReviewService optionalServiceReviewService

    @WithMockUser
    def "GET api/services/allowed/all returns 200 positive test scenario"() {
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

        def count = 1L
        def optionalServiceId = fakeOptionalService.getId()
        def rating = 10.0D
        def optionalService = fakeOptionalService
        def optionalServiceList = ImmutableList.of(optionalService)
        def optionalServiceDto = OptionalServiceMapper.toDto(optionalService)
        optionalServiceDto.setRating(rating)
        def resultList = ImmutableList.of(optionalServiceDto)
        def resultTableDto = new TableDto<>(
                TableDto.MetaDto.builder()
                        .pageNo(pageNo)
                        .pageSize(pageSize)
                        .sortBy(sortBy)
                        .total(count)
                        .build(),
                resultList)
        def jsonResponse = TestSerializer.serialize(resultTableDto)

        BDDMockito.given(optionalServiceService.list(eq(customPage), eq(keyword)))
                .willReturn(optionalServiceList)
        BDDMockito.given(optionalServiceService.count(eq(keyword)))
                .willReturn(count)
        BDDMockito.given(optionalServiceReviewService.getRating(eq(optionalServiceId)))
                .willReturn(rating)

        expect:
        mockMvc.perform(
                get('/api/services/allowed/all')
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
    def "GET api/services/allowed returns 200 positive test scenario"() {
        given:
        def id = 1L

        def optionalServiceId = fakeOptionalService.getId()
        def rating = 10.0D
        def optionalService = fakeOptionalService
        def optionalServiceDto = OptionalServiceMapper.toDto(optionalService)
        optionalServiceDto.setRating(rating)

        def jsonResponse = TestSerializer.serialize(optionalServiceDto)

        BDDMockito.given(optionalServiceService.get(eq(id)))
                .willReturn(optionalService)
        BDDMockito.given(optionalServiceReviewService.getRating(eq(optionalServiceId)))
                .willReturn(rating)

        expect:
        mockMvc.perform(
                get('/api/services/allowed')
                        .param('id', id.toString())
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))

    }

    @WithMockUser
    def "GET api/services/allowed/cities returns 200 positive test scenario"() {
        given:
        def resultList = ImmutableList.of("Warszawa", "Wrocław")

        def jsonResponse = TestSerializer.serialize(resultList)

        BDDMockito.given(optionalServiceService.getCities())
                .willReturn(resultList)
        expect:
        mockMvc.perform(
                get('/api/services/allowed/cities')
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))

    }

    @WithMockUser
    def "GET api/services/allowed/{id}/detail returns 200 positive test scenario"() {
        given:
        def id = 1L

        def optionalServiceId = fakeOptionalService.getId()
        def rating = 10.0D
        def optionalService = fakeOptionalService
        def optionalServiceDto = OptionalServiceMapper.toDtoWithDetails(optionalService)
        optionalServiceDto.setRating(rating)

        def jsonResponse = TestSerializer.serialize(optionalServiceDto)

        BDDMockito.given(optionalServiceService.getWithDetail(eq(id)))
                .willReturn(optionalService)
        BDDMockito.given(optionalServiceReviewService.getRating(eq(optionalServiceId)))
                .willReturn(rating)

        expect:
        mockMvc.perform(
                get('/api/services/allowed/{id}/detail', id)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))

    }

    @WithMockUser
    def "GET api/services/allowed/available returns 200 positive test scenario"() {
        given:
        def serviceId = 1L
        def date = '2012-01-01'
        def timeFrom = '10:00'
        def timeTo = '12:00'

        def result = true

        BDDMockito.given(optionalServiceService.isAvailable(eq(serviceId), eq(date), eq(timeFrom), eq(timeTo)))
                .willReturn(result)
        expect:
        mockMvc.perform(
                get('/api/services/allowed/available')
                .param('serviceId', serviceId.toString())
                .param('date', date)
                .param('timeFrom', timeFrom)
                .param('timeTo', timeTo)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(result.toString()))

    }

    @WithMockUser
    def "GET api/services/allowed/types returns 200 positive test scenario"() {
        given:
        def resultList = Stream.of(OptionalServiceTypeEnum.values())
                .map({ it.getValue() })
                .collect(ImmutableList.toImmutableList())

        def jsonResponse = TestSerializer.serialize(resultList)

        expect:
        mockMvc.perform(
                get('/api/services/allowed/types')
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))

    }

    @WithMockUser
    def "GET api/services/allowed/kid/performer/types returns 200 positive test scenario"() {
        given:
        def resultList = Stream.of(KidPerformerTypeEnum.values())
                .map({ it.getValue() })
                .collect(ImmutableList.toImmutableList())

        def jsonResponse = TestSerializer.serialize(resultList)

        expect:
        mockMvc.perform(
                get('/api/services/allowed/kid/performer/types')
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))

    }

    @WithMockUser
    def "GET api/services/allowed/music/styles returns 200 positive test scenario"() {
        given:
        def resultList = Stream.of(MusicStyleEnum.values())
                .map({ it.getValue() })
                .collect(ImmutableList.toImmutableList())

        def jsonResponse = TestSerializer.serialize(resultList)

        expect:
        mockMvc.perform(
                get('/api/services/allowed/music/styles')
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))

    }

    @WithMockUser
    def "GET api/services/allowed/languages returns 200 positive test scenario"() {
        given:
        def resultList = Stream.of(LanguagesEnum.values())
                .map({ it.getValue() })
                .collect(ImmutableList.toImmutableList())

        def jsonResponse = TestSerializer.serialize(resultList)

        expect:
        mockMvc.perform(
                get('/api/services/allowed/languages')
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))

    }

    @WithMockUser
    def "POST api/services/allowed/search returns 200 positive test scenario"() {
        given:
        def dto = FilterOptionalServiceDto.builder().city("Warszawa").build()

        def count = 1L
        def optionalServiceId = fakeOptionalService.getId()
        def rating = 10.0D
        def optionalService = fakeOptionalService
        def optionalServiceList = ImmutableList.of(optionalService)
        def optionalServiceDto = OptionalServiceMapper.toDto(optionalService)
        optionalServiceDto.setRating(rating)
        def resultList = ImmutableList.of(optionalServiceDto)
        def resultTableDto = new TableDto<>(
                new TableDto.MetaDto((long) count, null, null, null),
                resultList)

        def jsonRequest = TestSerializer.serialize(dto)
        def jsonResponse = TestSerializer.serialize(resultTableDto)

        BDDMockito.given(optionalServiceService.search(eq(dto)))
                .willReturn(optionalServiceList)
        BDDMockito.given(optionalServiceReviewService.getRating(eq(optionalServiceId)))
                .willReturn(rating)

        expect:
        mockMvc.perform(
                post('/api/services/allowed/search')
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(jsonRequest)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))

    }

    @WithMockUser(authorities = ['BUSINESS'])
    def "GET api/services/business returns 200 positive test scenario"() {
        given:
        def id = 1L

        def optionalService = fakeOptionalService
        def optionalServiceList = ImmutableList.of(optionalService)
        def optionalServiceDto = OptionalServiceMapper.toDto(optionalService)
        def resultList = ImmutableList.of(optionalServiceDto)

        def jsonResponse = TestSerializer.serialize(resultList)

        BDDMockito.given(optionalServiceService.getByBusinessId(eq(id)))
                .willReturn(optionalServiceList)

        expect:
        mockMvc.perform(
                get('/api/services/business')
                        .param('id', id.toString())
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))

    }

    @WithMockUser(authorities = ['BUSINESS'])
    def "POST api/services returns 200 positive test scenario"() {
        given:
        def dto = fakeOptionalServiceHostDto

        def optionalService = fakeOptionalService
        def result = OptionalServiceMapper.toDto(optionalService)

        def jsonRequest = TestSerializer.serialize(dto)
        def jsonResponse = TestSerializer.serialize(result)

        BDDMockito.given(optionalServiceService.create(eq(dto)))
                .willReturn(optionalService)

        expect:
        mockMvc.perform(
                post("/api/services")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(jsonRequest)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))
    }

    @WithMockUser(authorities = ['BUSINESS'])
    def "PUT api/services/edit returns 200 positive test scenario"() {
        given:
        def id = 1L
        def dto = fakeOptionalServiceHostDto

        def optionalServiceId = fakeOptionalService.getId()
        def rating = 10.0D
        def optionalService = fakeOptionalService
        def result = OptionalServiceMapper.toDto(optionalService)
        result.setRating(rating)

        def jsonRequest = TestSerializer.serialize(dto)
        def jsonResponse = TestSerializer.serialize(result)

        BDDMockito.given(optionalServiceService.edit(eq(dto), eq(id)))
                .willReturn(optionalService)
        BDDMockito.given(optionalServiceReviewService.getRating(eq(optionalServiceId)))
                .willReturn(rating)

        expect:
        mockMvc.perform(
                put("/api/services/edit")
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
    def "DELETE api/services returns 204 positive test scenario"() {
        given:
        def id = 1L

        expect:
        mockMvc.perform(
                delete("/api/services")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param('id', id.toString())
        )
                .andExpect(status().isNoContent())

        BDDMockito.verify(optionalServiceService, times(1))
                .delete(eq(id))
    }

}
