package pjatk.pro.event_organizer_app.catering.controller

import com.google.common.collect.ImmutableList
import org.mockito.BDDMockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import pjatk.pro.event_organizer_app.catering.mapper.CateringItemMapper
import pjatk.pro.event_organizer_app.catering.service.CateringItemService
import pjatk.pro.event_organizer_app.enums.CateringItemTypeEnum
import pjatk.pro.event_organizer_app.test_helper.TestSerializer
import pjatk.pro.event_organizer_app.trait.catering.CateringItemTrait
import spock.lang.Specification

import java.util.stream.Stream

import static org.mockito.ArgumentMatchers.eq
import static org.mockito.Mockito.times
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(controllers = [CateringItemController.class])
class CateringItemControllerTest extends Specification
        implements CateringItemTrait {

    @Autowired
    private MockMvc mockMvc

    @MockBean
    private CateringItemService cateringItemService


    @WithMockUser
    def "GET api/catering/items/allowed returns 200 positive test scenario"() {
        given:
        def cateringId = 1L

        def cateringItem = fakeCateringItem
        def cateringItemList = ImmutableList.of(cateringItem)
        def cateringItemDto = CateringItemMapper.toDto(cateringItem)
        def resultList = ImmutableList.of(cateringItemDto)

        def jsonResponse = TestSerializer.serialize(resultList)

        BDDMockito.given(cateringItemService.listAllByCateringId(eq(cateringId)))
                .willReturn(cateringItemList)

        expect:
        mockMvc.perform(
                get("/api/catering/items/allowed")
                        .param("cateringId", cateringId.toString())
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))
    }


    @WithMockUser
    def "GET api/catering/items/allowed/types returns 200 positive test scenario"() {
        given:
        def resultList = Stream.of(CateringItemTypeEnum.values())
                .map({ it.getValue() })
                .collect(ImmutableList.toImmutableList())

        def jsonResponse = TestSerializer.serialize(resultList)

        expect:
        mockMvc.perform(
                get("/api/catering/items/allowed/types")
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))
    }

    @WithMockUser(authorities = ['BUSINESS'])
    def "GET api/catering/items returns 200 positive test scenario"() {
        given:
        def id = 1L

        def cateringItem = fakeCateringItem
        def result = CateringItemMapper.toDto(cateringItem)

        def jsonResponse = TestSerializer.serialize(result)

        BDDMockito.given(cateringItemService.get(eq(id)))
                .willReturn(cateringItem)

        expect:
        mockMvc.perform(
                get("/api/catering/items")
                        .param("id", id.toString())
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))
    }

    @WithMockUser(authorities = ['ADMIN'])
    def "POST api/catering/items returns 200 positive test scenario"() {
        given:
        def dto = fakeCateringItemDto
        def cateringId = 1L

        def cateringItem = fakeCateringItem
        def result = CateringItemMapper.toDto(cateringItem)

        def jsonRequest = TestSerializer.serialize(dto)
        def jsonResponse = TestSerializer.serialize(result)

        BDDMockito.given(cateringItemService.create(eq(dto), eq(cateringId)))
                .willReturn(cateringItem)

        expect:
        mockMvc.perform(
                post("/api/catering/items")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(jsonRequest)
                        .param('cateringId', cateringId.toString())
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))
    }

    @WithMockUser(authorities = ['BUSINESS'])
    def "PUT api/catering/items returns 200 positive test scenario"() {
        given:
        def dto = fakeCateringItemDto
        def id = 1L

        def cateringItem = fakeCateringItem
        def result = CateringItemMapper.toDto(cateringItem)

        def jsonRequest = TestSerializer.serialize(dto)
        def jsonResponse = TestSerializer.serialize(result)

        BDDMockito.given(cateringItemService.edit(eq(id), eq(dto)))
                .willReturn(cateringItem)

        expect:
        mockMvc.perform(
                put("/api/catering/items")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(jsonRequest)
                        .param('id', id.toString())
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))
    }
    
    @WithMockUser(authorities = ['BUSINESS'])
    def "DELETE api/catering/items returns 204 positive test scenario"() {
        given:
        def id = 1L

        expect:
        mockMvc.perform(
                delete("/api/catering/items")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param('id', id.toString())
        )
                .andExpect(status().isNoContent())

        BDDMockito.verify(cateringItemService, times(1))
                .delete(eq(id))
    }

}
