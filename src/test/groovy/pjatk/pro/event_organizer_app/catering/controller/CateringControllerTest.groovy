package pjatk.pro.event_organizer_app.catering.controller

import com.google.common.collect.ImmutableList
import org.mockito.BDDMockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import pjatk.pro.event_organizer_app.catering.mapper.CateringMapper
import pjatk.pro.event_organizer_app.catering.service.CateringService
import pjatk.pro.event_organizer_app.common.paginator.CustomPage
import pjatk.pro.event_organizer_app.exceptions.IllegalArgumentException
import pjatk.pro.event_organizer_app.reviews.catering.service.CateringReviewService
import pjatk.pro.event_organizer_app.table.TableDto
import pjatk.pro.event_organizer_app.test_helper.TestSerializer
import pjatk.pro.event_organizer_app.trait.catering.CateringTrait
import pjatk.pro.event_organizer_app.trait.catering.FilterCateringsDtoTrait
import spock.lang.Specification

import static org.mockito.ArgumentMatchers.eq
import static org.mockito.Mockito.times
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(controllers = [CateringController.class])
class CateringControllerTest extends Specification
        implements CateringTrait, FilterCateringsDtoTrait {

    @Autowired
    private MockMvc mockMvc

    @MockBean
    private CateringService cateringService
    @MockBean
    private CateringReviewService cateringReviewService

    @WithMockUser
    def "GET api/caterings/allowed/all returns 200 positive test scenario"() {
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

        def catering = fakeCatering
        def cateringList = ImmutableList.of(catering)
        def count = 1L
        def dto = CateringMapper.toDto(catering)
        def rating = 1.0D
        dto.setRating(rating)

        def resultList = ImmutableList.of(dto)
        def cateringTableDto =
                new TableDto<>(new TableDto.MetaDto(count, pageNo, pageSize, sortBy), resultList)
        def jsonResponse = TestSerializer.serialize(cateringTableDto)

        BDDMockito.given(cateringService.list(eq(customPage), eq(keyword)))
                .willReturn(cateringList)
        BDDMockito.given(cateringService.count(eq(keyword)))
                .willReturn(count)
        BDDMockito.given(cateringReviewService.getRating(eq(dto.getId())))
                .willReturn(rating)

        expect:
        mockMvc.perform(
                get("/api/caterings/allowed/all")
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

    @WithMockUser
    def "POST api/caterings/allowed/search returns 200 positive test scenario"() {
        given:
        def dto = fakeFilterCateringsDto
        def locationId = 1l
        def catering = fakeCatering
        def cateringList = ImmutableList.of(catering)
        def cateringDto = CateringMapper.toDto(catering)
        def rating = 1.0D
        def count = 1
        cateringDto.setRating(rating)
        def resultList = ImmutableList.of(cateringDto)
        def result =
                new TableDto<>(new TableDto.MetaDto(count, null, null, null), resultList)

        def jsonRequest = TestSerializer.serialize(dto)
        def jsonResponse = TestSerializer.serialize(result)

        BDDMockito.given(cateringService.search(eq(dto), eq(locationId)))
                .willReturn(cateringList)
        BDDMockito.given(cateringReviewService.getRating(eq(cateringDto.getId())))
                .willReturn(rating)

        expect:
        mockMvc.perform(
                post("/api/caterings/allowed/search")
                        .param("locationId", locationId.toString())
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(jsonRequest)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))
    }


    @WithMockUser
    def "GET api/caterings/allowed with id parameter returns 200 positive test scenario"() {
        given:
        def id = 1L

        def catering = fakeCatering
        def cateringDto = CateringMapper.toDto(catering)
        def rating = 1.0D
        cateringDto.setRating(rating)

        def jsonResponse = TestSerializer.serialize(cateringDto)

        BDDMockito.given(cateringService.get(eq(id)))
                .willReturn(catering)
        BDDMockito.given(cateringReviewService.getRating(eq(cateringDto.getId())))
                .willReturn(rating)

        expect:
        mockMvc.perform(
                get("/api/caterings/allowed")
                        .param('id', id.toString())
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))
    }

    @WithMockUser
    def "GET api/caterings/allowed/{id}/detail returns 200 positive test scenario"() {
        given:
        def id = 1L

        def catering = fakeCateringWithDetails
        def cateringDto = CateringMapper.toDtoWithDetail(catering)
        def rating = 1.0D
        cateringDto.setRating(rating)

        def jsonResponse = TestSerializer.serialize(cateringDto)

        BDDMockito.given(cateringService.getWithDetail(eq(id)))
                .willReturn(catering)
        BDDMockito.given(cateringReviewService.getRating(eq(cateringDto.getId())))
                .willReturn(rating)

        expect:
        mockMvc.perform(
                get("/api/caterings/allowed/{id}/detail", id)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))
    }


    @WithMockUser(authorities = ['CUSTOMER'])
    def "GET api/caterings/business returns 200 positive test scenario"() {
        given:
        def id = 1L

        def catering = fakeCatering
        def cateringList = ImmutableList.of(catering)
        def cateringDto = CateringMapper.toDto(catering)
        def rating = 1.0D
        cateringDto.setRating(rating)

        def resultList = ImmutableList.of(cateringDto)

        def jsonResponse = TestSerializer.serialize(resultList)

        BDDMockito.given(cateringService.getByBusinessId(eq(id)))
                .willReturn(cateringList)
        BDDMockito.given(cateringReviewService.getRating(eq(cateringDto.getId())))
                .willReturn(rating)

        expect:
        mockMvc.perform(
                get("/api/caterings/business")
                        .param("id", id.toString())
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))
    }

    @WithMockUser(authorities = ['BUSINESS'])
    def "GET api/caterings/allowed/location returns 200 positive test scenario"() {
        given:
        def id = 1L

        def catering = fakeCatering
        def cateringList = ImmutableList.of(catering)
        def cateringDto = CateringMapper.toDto(catering)
        def rating = 1.0D
        cateringDto.setRating(rating)

        def resultList = ImmutableList.of(cateringDto)

        def jsonResponse = TestSerializer.serialize(resultList)

        BDDMockito.given(cateringService.getByLocationId(eq(id)))
                .willReturn(cateringList)
        BDDMockito.given(cateringReviewService.getRating(eq(cateringDto.getId())))
                .willReturn(rating)

        expect:
        mockMvc.perform(
                get("/api/caterings/allowed/location")
                        .param("id", id.toString())
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))
    }

    @WithMockUser(authorities = ['ADMIN'])
    def "DELETE api/caterings/delete returns 204 positive test scenario"() {
        given:
        def id = 1L

        expect:
        mockMvc.perform(
                delete("/api/caterings/delete")
                        .param('id', id.toString())
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        )
                .andExpect(status().isNoContent())

        BDDMockito.verify(cateringService, times(1))
                .delete(eq(id))
    }

    @WithMockUser(authorities = ['ADMIN'])
    def "DELETE api/caterings/delete returns 400 negative test scenario"() {
        given:
        def id = 1L

        BDDMockito.given(cateringService.delete(eq(id)))
                .willThrow(new IllegalArgumentException("SAMPLE ERROR MESSAGE"))

        expect:
        mockMvc.perform(
                delete("/api/caterings/delete")
                        .param('id', id.toString())
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        )
                .andExpect(status().isBadRequest())
    }

    @WithMockUser(authorities = ['ADMIN'])
    def "POST api/caterings/new/location returns 200 positive test scenario"() {
        given:
        def dto = fakeCateringDtoOffersOutsideCatering
        def locationId = 1L

        def catering = fakeCatering
        def cateringDto = CateringMapper.toDto(catering)

        def jsonRequest = TestSerializer.serialize(dto)
        def jsonResponse = TestSerializer.serialize(cateringDto)

        BDDMockito.given(cateringService.create(eq(dto), eq(locationId)))
                .willReturn(catering)

        expect:
        mockMvc.perform(
                post("/api/caterings/new/location")
                        .param("locationId", locationId.toString())
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(jsonRequest)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))
    }


    @WithMockUser(authorities = ['ADMIN'])
    def "POST api/caterings/new returns 200 positive test scenario"() {
        given:
        def dto = fakeCateringDtoOffersOutsideCatering

        def catering = fakeCatering
        def cateringDto = CateringMapper.toDto(catering)

        def jsonRequest = TestSerializer.serialize(dto)
        def jsonResponse = TestSerializer.serialize(cateringDto)

        BDDMockito.given(cateringService.create(eq(dto), eq(null)))
                .willReturn(catering)

        expect:
        mockMvc.perform(
                post("/api/caterings/new")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(jsonRequest)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))
    }

    @WithMockUser(authorities = ['BUSINESS'])
    def "PUT api/caterings/edit returns 200 positive test scenario"() {
        given:
        def dto = fakeCateringDtoOffersOutsideCatering
        def id = 1L

        def catering = fakeCateringWithDetails
        def cateringDto = CateringMapper.toDtoWithDetailAndLocations(catering)
        def rating = 1.0D
        cateringDto.setRating(rating)

        def jsonRequest = TestSerializer.serialize(dto)
        def jsonResponse = TestSerializer.serialize(cateringDto)

        BDDMockito.given(cateringService.edit(eq(id), eq(dto)))
                .willReturn(catering)
        BDDMockito.given(cateringReviewService.getRating(eq(cateringDto.getId())))
                .willReturn(rating)

        expect:
        mockMvc.perform(
                put("/api/caterings/edit")
                        .param("id", id.toString())
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(jsonRequest)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))
    }

    @WithMockUser(authorities = ['ADMIN'])
    def "PUT api/caterings/edit returns 404 negative test scenario"() {
        given:
        def dto = fakeCateringDtoOffersOutsideCatering
        def id = 1L

        def catering = fakeCateringWithDetails
        def cateringDto = CateringMapper.toDtoWithDetailAndLocations(catering)

        def jsonRequest = TestSerializer.serialize(dto)

        BDDMockito.given(cateringService.edit(eq(id), eq(dto)))
                .willReturn(catering)
        BDDMockito.given(cateringReviewService.getRating(eq(cateringDto.getId())))
                .willThrow(new IllegalArgumentException("SAMPLE MESSAGE"))

        expect:
        mockMvc.perform(
                put("/api/caterings/edit")
                        .param("id", id.toString())
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(jsonRequest)
        )
                .andExpect(status().isNotFound())
    }

}

