package pjatk.pro.event_organizer_app.cateringforchosenevent.controller

import com.google.common.collect.ImmutableList
import org.mockito.BDDMockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import pjatk.pro.event_organizer_app.cateringforchosenevent.mapper.CateringOrderChoiceMapper
import pjatk.pro.event_organizer_app.cateringforchosenevent.model.dto.CateringOrderChoiceDto
import pjatk.pro.event_organizer_app.cateringforchosenevent.service.CateringOrderChoiceService
import pjatk.pro.event_organizer_app.test_helper.TestSerializer
import pjatk.pro.event_organizer_app.trait.cateringforchosenevent.CateringOrderChoiceTrait
import spock.lang.Specification

import static org.mockito.ArgumentMatchers.eq
import static org.mockito.Mockito.times
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(controllers = [CateringOrderChoiceController.class])
class CateringOrderChoiceControllerTest extends Specification
        implements CateringOrderChoiceTrait {

    @Autowired
    private MockMvc mockMvc

    @MockBean
    private CateringOrderChoiceService cateringOrderChoiceService

    @WithMockUser(authorities = ['ADMIN'])
    def "GET api/catering/order returns 200 positive test scenario"() {
        given:
        def cateringId = 1L

        def cateringOrderChoice = fakeCateringOrderChoiceConfirmed
        def cateringOrderChoiceList = ImmutableList.of(cateringOrderChoice)
        def cateringOrderChoiceDto = CateringOrderChoiceMapper.toDtoWithItem(cateringOrderChoice)
        def resultList = ImmutableList.of(cateringOrderChoiceDto)

        def jsonResponse = TestSerializer.serialize(resultList)

        BDDMockito.given(cateringOrderChoiceService.getAll(eq(cateringId)))
                .willReturn(cateringOrderChoiceList)

        expect:
        mockMvc.perform(
                get("/api/catering/order")
                        .param("cateringId", cateringId.toString())
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))
    }


    @WithMockUser(authorities = ['CUSTOMER'])
    def "GET api/catering/order/reservation returns 200 positive test scenario"() {
        given:
        def cateringId = 1L
        def reservationId = 2L

        def cateringOrderChoice = fakeCateringOrderChoiceConfirmed
        def cateringOrderChoiceList = ImmutableList.of(cateringOrderChoice)
        def cateringOrderChoiceDto = CateringOrderChoiceMapper.toDtoWithItem(cateringOrderChoice)
        def resultList = ImmutableList.of(cateringOrderChoiceDto)

        def jsonResponse = TestSerializer.serialize(resultList)

        BDDMockito.given(cateringOrderChoiceService.getAll(eq(cateringId), eq(reservationId)))
                .willReturn(cateringOrderChoiceList)

        expect:
        mockMvc.perform(
                get("/api/catering/order/reservation")
                        .param("cateringId", cateringId.toString())
                        .param("reservationId", reservationId.toString())
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))
    }

    @WithMockUser(authorities = ['ADMIN'])
    def "POST api/catering/order returns 200 positive test scenario"() {
        given:
        def reservationId = 1L
        def dtos = [fakeCateringOrderChoiceDto].toArray() as CateringOrderChoiceDto[]

        def cateringOrderChoice = fakeCateringOrderChoiceConfirmed
        def orderChoiceList = [fakeCateringOrderChoiceConfirmed]
        def cateringOrderChoiceDto = CateringOrderChoiceMapper.toDto(cateringOrderChoice)
        def resultList = ImmutableList.of(cateringOrderChoiceDto)

        def jsonRequest = TestSerializer.serialize(dtos)
        def jsonResponse = TestSerializer.serialize(resultList)

        BDDMockito.given(cateringOrderChoiceService.create(eq(dtos), eq(reservationId)))
                .willReturn(orderChoiceList)

        expect:
        mockMvc.perform(
                post("/api/catering/order")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(jsonRequest)
                        .param('reservationId', reservationId.toString())
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))
    }

    @WithMockUser(authorities = ['CUSTOMER'])
    def "PUT api/catering/order returns 200 positive test scenario"() {
        given:
        def orderChoiceId = 1L
        def dto = fakeCateringOrderChoiceDto

        def cateringOrderChoice = fakeCateringOrderChoiceNotConfirmed
        def result = CateringOrderChoiceMapper.toDtoWithItem(cateringOrderChoice)

        def jsonRequest = TestSerializer.serialize(dto)
        def jsonResponse = TestSerializer.serialize(result)

        BDDMockito.given(cateringOrderChoiceService.edit(eq(dto), eq(orderChoiceId)))
                .willReturn(cateringOrderChoice)

        expect:
        mockMvc.perform(
                put("/api/catering/order")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(jsonRequest)
                        .param('orderChoiceId', orderChoiceId.toString())
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))
    }

    @WithMockUser(authorities = ['ADMIN'])
    def "DELETE api/catering/order returns 204 positive test scenario"() {
        given:
        def id = 1L

        expect:
        mockMvc.perform(
                delete("/api/catering/order")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param('id', id.toString())
        )
                .andExpect(status().isNoContent())

        BDDMockito.verify(cateringOrderChoiceService, times(1))
                .delete(eq(id))
    }
}
