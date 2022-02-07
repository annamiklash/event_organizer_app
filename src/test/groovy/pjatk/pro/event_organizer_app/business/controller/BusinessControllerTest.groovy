package pjatk.pro.event_organizer_app.business.controller

import com.google.common.collect.ImmutableList
import org.mockito.BDDMockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import pjatk.pro.event_organizer_app.business.mapper.BusinessMapper
import pjatk.pro.event_organizer_app.business.service.BusinessService
import pjatk.pro.event_organizer_app.common.paginator.CustomPage
import pjatk.pro.event_organizer_app.test_helper.TestSerializer
import pjatk.pro.event_organizer_app.trait.business.BusinessTrait
import spock.lang.Specification

import static org.mockito.ArgumentMatchers.eq
import static org.mockito.Mockito.times
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(controllers = [BusinessController.class])
class BusinessControllerTest extends Specification
        implements BusinessTrait {

    @Autowired
    private MockMvc mockMvc

    @MockBean
    private BusinessService businessService

    @WithMockUser(authorities = ['ADMIN'])
    def "GET api/business returns 200 positive test scenario"() {
        given:
        def pageNo = 1
        def pageSize = 50
        def sort = 'id'
        def order = 'desc'

        def customPage = CustomPage.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .sortBy(sort)
                .order(order)
                .build()

        def business = fakeVerifiedBusiness
        def businessList = ImmutableList.of(business)
        def resultList = ImmutableList.of(BusinessMapper.toDto(business))
        def jsonResponse = TestSerializer.serialize(resultList)

        BDDMockito.given(businessService.list(eq(customPage)))
                .willReturn(businessList)

        expect:
        mockMvc.perform(
                get("/api/business")
                        .param("pageNo", pageNo.toString())
                        .param("pageSize", pageSize.toString())
                        .param("sort", sort)
                        .param("order", order)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))
    }

    @WithMockUser(authorities = ['ADMIN'])
    def "GET api/business with id param returns 200 positive test scenario"() {
        given:
        def id = 1L

        def business = fakeVerifiedBusiness
        def result = BusinessMapper.toDto(business)
        def jsonResponse = TestSerializer.serialize(result)

        BDDMockito.given(businessService.get(eq(id)))
                .willReturn(business)

        expect:
        mockMvc.perform(
                get("/api/business")
                        .param("id", id.toString())
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))

    }

    @WithMockUser(authorities = ['ADMIN'])
    def "GET api/business/{id}/detail returns 200 positive test scenario"() {
        given:
        def id = 1L

        def business = fakeVerifiedBusiness
        def result = BusinessMapper.toDtoWithDetail(business)
        def jsonResponse = TestSerializer.serialize(result)

        BDDMockito.given(businessService.getWithDetail(eq(id)))
                .willReturn(business)

        expect:
        mockMvc.perform(
                get("/api/business/{id}/detail", id)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))

    }

    @WithMockUser(authorities = ['BUSINESS'])
    def "PUT api/business returns 200 positive test scenario"() {
        given:
        def id = 1L
        def dto = fakeVerifiedBusinessDto

        def business = fakeVerifiedBusiness
        def result = BusinessMapper.toDto(business)

        def jsonRequest = TestSerializer.serialize(dto)
        def jsonResponse = TestSerializer.serialize(result)

        BDDMockito.given(businessService.edit(eq(id), eq(dto)))
                .willReturn(business)

        expect:
        mockMvc.perform(
                put("/api/business")
                        .param('id', id.toString())
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(jsonRequest)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))

    }

    @WithMockUser(authorities = ['BUSINESS'])
    def "DELETE api/business returns 200 positive test scenario"() {
        given:
        def id = 1L

        expect:
        mockMvc.perform(
                delete("/api/business")
                        .param('id', id.toString())
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        )
                .andExpect(status().isOk())

        BDDMockito.verify(businessService, times(1))
                .delete(eq(id))
    }

    @WithMockUser(authorities = ['ADMIN'])
    def "PUT api/business/verify returns 200 positive test scenario"() {
        given:
        def id = 1L

        def business = fakeVerifiedBusiness
        def result = BusinessMapper.toDto(business)

        def jsonResponse = TestSerializer.serialize(result)

        BDDMockito.given(businessService.verify(eq(id)))
                .willReturn(business)

        expect:
        mockMvc.perform(
                put("/api/business/verify")
                        .param('id', id.toString())
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))

    }

}
