package pjatk.pro.event_organizer_app.address.controller

import com.google.common.collect.ImmutableList
import org.mockito.BDDMockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import pjatk.pro.event_organizer_app.address.mapper.AddressMapper
import pjatk.pro.event_organizer_app.address.service.AddressService
import pjatk.pro.event_organizer_app.common.paginator.CustomPage
import pjatk.pro.event_organizer_app.test_helper.TestSerializer
import pjatk.pro.event_organizer_app.trait.address.AddressTrait
import spock.lang.Specification

import static org.mockito.ArgumentMatchers.eq
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(controllers = [AddressController.class])
class AddressControllerTest extends Specification
        implements AddressTrait {

    @Autowired
    private MockMvc mockMvc

    @MockBean
    private AddressService addressService

    @WithMockUser(authorities = ['ADMIN'])
    def "GET api/address returns 200 positive test scenario"() {
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

        def address = fakeAddress
        def addressList = ImmutableList.of(address)
        def resultList = ImmutableList.of(AddressMapper.toDto(address))
        def jsonResponse = TestSerializer.serialize(resultList)

        BDDMockito.given(addressService.list(eq(customPage)))
                .willReturn(addressList)

        expect:
        mockMvc.perform(
                get("/api/address")
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
    def "GET api/address with id param returns 200 positive test scenario"() {
        given:
        def id = 1L

        def address = fakeAddress
        def result = AddressMapper.toDto(address)
        def jsonResponse = TestSerializer.serialize(result)

        BDDMockito.given(addressService.get(eq(id)))
                .willReturn(address)

        expect:
        mockMvc.perform(
                get("/api/address")
                .param("id", id.toString())
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))

    }

}
