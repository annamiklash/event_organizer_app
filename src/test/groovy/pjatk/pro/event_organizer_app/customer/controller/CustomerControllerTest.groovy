package pjatk.pro.event_organizer_app.customer.controller

import com.google.common.collect.ImmutableList
import org.mockito.BDDMockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import pjatk.pro.event_organizer_app.common.paginator.CustomPage
import pjatk.pro.event_organizer_app.customer.mapper.CustomerMapper
import pjatk.pro.event_organizer_app.customer.service.CustomerService
import pjatk.pro.event_organizer_app.exceptions.NotFoundException
import pjatk.pro.event_organizer_app.test_helper.TestSerializer
import pjatk.pro.event_organizer_app.trait.customer.CustomerTrait
import pjatk.pro.event_organizer_app.trait.customer.message.MessageDtoTrait
import spock.lang.Specification

import static org.mockito.ArgumentMatchers.any
import static org.mockito.ArgumentMatchers.eq
import static org.mockito.Mockito.times
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(controllers = [CustomerController.class])
class CustomerControllerTest extends Specification
        implements CustomerTrait,
                MessageDtoTrait {

    @Autowired
    private MockMvc mockMvc

    @MockBean
    private CustomerService customerService


    @WithMockUser(authorities = ['ADMIN'])
    def "GET api/customers returns 200 positive test scenario"() {
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

        def customer = fakeCustomer
        def customerList = ImmutableList.of(customer)
        def resultList = ImmutableList.of(CustomerMapper.toDto(customer))
        def jsonResponse = TestSerializer.serialize(resultList)

        BDDMockito.given(customerService.list(eq(customPage), eq(keyword)))
                .willReturn(customerList)

        expect:
        mockMvc.perform(
                get('/api/customers')
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

    @WithMockUser(authorities = ['ADMIN'])
    def "GET api/customers with id param returns 200 positive test scenario"() {
        given:
        def id = 1L

        def customer = fakeCustomer
        def result = CustomerMapper.toDto(customer)
        def jsonResponse = TestSerializer.serialize(result)

        BDDMockito.given(customerService.get(eq(id)))
                .willReturn(customer)

        expect:
        mockMvc.perform(
                get('/api/customers')
                        .param('id', id.toString())
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))

    }

    @WithMockUser(authorities = ['ADMIN'])
    def "GET api/customers with id param returns 400 negative test scenario"() {
        given:
        def id = 1L

        BDDMockito.given(customerService.get(eq(id)))
                .willThrow(new NotFoundException("SAMPLE ERROR MESSAGE"))

        expect:
        mockMvc.perform(
                get('/api/customers')
                        .param('id', id.toString())
        )
                .andExpect(status().isBadRequest())

    }

    @WithMockUser(authorities = ['ADMIN'])
    def "GET api/customers/{id}/detail returns 200 positive test scenario"() {
        given:
        def id = 1L

        def customer = fakeCustomer
        def result = CustomerMapper.toDtoWithDetail(customer)
        def jsonResponse = TestSerializer.serialize(result)

        BDDMockito.given(customerService.getWithDetail(eq(id)))
                .willReturn(customer)

        expect:
        mockMvc.perform(
                get('/api/customers/{id}/detail', id)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))

    }

    @WithMockUser(authorities = ['ADMIN'])
    def "GET api/customers/{id}/detail returns 400 negative test scenario"() {
        given:
        def id = 1L

        BDDMockito.given(customerService.getWithDetail(eq(id)))
                .willThrow(new NotFoundException("SAMPLE ERROR MESSAGE"))

        expect:
        mockMvc.perform(
                get('/api/customers/{id}/detail', id)
        )
                .andExpect(status().isBadRequest())

    }

    @WithMockUser(authorities = ['ADMIN'])
    def "GET api/customers/{id}/guests returns 200 positive test scenario"() {
        given:
        def id = 1L

        def result = fakeCustomerDTO
        def jsonResponse = TestSerializer.serialize(result)

        BDDMockito.given(customerService.getWithGuests(eq(id)))
                .willReturn(result)

        expect:
        mockMvc.perform(
                get('/api/customers/{id}/guests', id)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))

    }

    @WithMockUser(authorities = ['ADMIN'])
    def "GET api/customers/{id}/guests returns 400 negative test scenario"() {
        given:
        def id = 1L

        BDDMockito.given(customerService.getWithGuests(eq(id)))
                .willThrow(new NotFoundException("SAMPLE ERROR MESSAGE"))

        expect:
        mockMvc.perform(
                get('/api/customers/{id}/guests', id)
        )
                .andExpect(status().isBadRequest())

    }

    @WithMockUser(authorities = ['ADMIN'])
    def "GET api/customers/{id}/problems returns 200 positive test scenario"() {
        given:
        def id = 1L

        def result = fakeCustomerDTO
        def jsonResponse = TestSerializer.serialize(result)

        BDDMockito.given(customerService.getWithProblems(eq(id)))
                .willReturn(result)

        expect:
        mockMvc.perform(
                get('/api/customers/{id}/problems', id)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))

    }

    @WithMockUser(authorities = ['ADMIN'])
    def "GET api/customers/{id}/problems returns 400 negative test scenario"() {
        given:
        def id = 1L

        BDDMockito.given(customerService.getWithProblems(eq(id)))
                .willThrow(new NotFoundException("SAMPLE ERROR MESSAGE"))

        expect:
        mockMvc.perform(
                get('/api/customers/{id}/problems', id)
        )
                .andExpect(status().isBadRequest())

    }


    @WithMockUser(authorities = ['ADMIN'])
    def "GET api/customers/{id}/events/all returns 200 positive test scenario"() {
        given:
        def id = 1L

        def customer = fakeCustomer
        def result = CustomerMapper.toDtoWithDetail(customer)
        def jsonResponse = TestSerializer.serialize(result)

        BDDMockito.given(customerService.getWithAllEvents(eq(id)))
                .willReturn(customer)

        expect:
        mockMvc.perform(
                get('/api/customers/{id}/events/all', id)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))

    }

    @WithMockUser(authorities = ['ADMIN'])
    def "GET api/customers/{id}/events/all returns 400 negative test scenario"() {
        given:
        def id = 1L

        BDDMockito.given(customerService.getWithAllEvents(eq(id)))
                .willThrow(new NotFoundException("SAMPLE ERROR MESSAGE"))

        expect:
        mockMvc.perform(
                get('/api/customers/{id}/events/all', id)
        )
                .andExpect(status().isBadRequest())

    }


    @WithMockUser(authorities = ['ADMIN'])
    def "PUT api/customers with id parameter returns 200 positive test scenario"() {
        given:
        def id = 1L
        def dto = fakeCustomerDTO

        def customer = fakeCustomer
        def result = CustomerMapper.toDto(customer)

        def jsonRequest = TestSerializer.serialize(dto)
        def jsonResponse = TestSerializer.serialize(result)

        BDDMockito.given(customerService.edit(eq(dto), eq(id)))
                .willReturn(customer)

        expect:
        mockMvc.perform(
                put('/api/customers', id)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(jsonRequest)
                        .param('id', id.toString())
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))

    }

    @WithMockUser(authorities = ['CUSTOMER'])
    def "DELETE api/customers with id parameter returns 200 positive test scenario"() {
        given:
        def id = 1L

        expect:
        mockMvc.perform(
                delete('/api/customers')
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param('id', id.toString())
        )
                .andExpect(status().isOk())

        BDDMockito.verify(customerService, times(1))
                .delete(eq(id))
    }

    @WithMockUser(authorities = ['CUSTOMER'])
    def "POST api/customers/invite/send returns 200 positive test scenario"() {
        given:
        def id = 1L
        def eventId = 2L

        expect:
        mockMvc.perform(
                post('/api/customers/invite/send')
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param('id', id.toString())
                        .param('eventId', eventId.toString())
        )
                .andExpect(status().isOk())

        BDDMockito.verify(customerService, times(1))
                .sendInvitationToGuest(eq(eventId), eq(id))

    }

    @WithMockUser(authorities = ['CUSTOMER'])
    def "POST api/customers/message/location/send returns 200 positive test scenario"() {
        given:
        def customerId = 1L
        def locationId = 2L
        def dto = fakeMessageDto

        def jsonRequest = TestSerializer.serialize(dto)

        expect:
        mockMvc.perform(
                post('/api/customers/message/location/send')
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(jsonRequest)
                        .param('customerId', customerId.toString())
                        .param('locationId', locationId.toString())
        )
                .andExpect(status().isOk())

        BDDMockito.verify(customerService, times(1))
                .sendMessage(eq(customerId), eq(locationId), eq(dto), any())

    }

    @WithMockUser(authorities = ['CUSTOMER'])
    def "POST api/customers/message/catering/send returns 200 positive test scenario"() {
        given:
        def customerId = 1L
        def cateringId = 2L
        def dto = fakeMessageDto

        def jsonRequest = TestSerializer.serialize(dto)

        expect:
        mockMvc.perform(
                post('/api/customers/message/catering/send')
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(jsonRequest)
                        .param('customerId', customerId.toString())
                        .param('cateringId', cateringId.toString())
        )
                .andExpect(status().isOk())

        BDDMockito.verify(customerService, times(1))
                .sendMessage(eq(customerId), eq(cateringId), eq(dto), any())

    }

    @WithMockUser(authorities = ['CUSTOMER'])
    def "POST api/customers/message/service/send returns 200 positive test scenario"() {
        given:
        def customerId = 1L
        def serviceId = 2L
        def dto = fakeMessageDto

        def jsonRequest = TestSerializer.serialize(dto)

        expect:
        mockMvc.perform(
                post('/api/customers/message/service/send')
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(jsonRequest)
                        .param('customerId', customerId.toString())
                        .param('serviceId', serviceId.toString())
        )
                .andExpect(status().isOk())

        BDDMockito.verify(customerService, times(1))
                .sendMessage(eq(customerId), eq(serviceId), eq(dto), any())

    }

    @WithMockUser(authorities = ['CUSTOMER'])
    def "PUT api/customers/guests/invite returns 201 positive test scenario"() {
        given:
        def customerId = 1L
        def eventId = 2L
        def guestIds = [4L].toArray() as long[]

        expect:
        mockMvc.perform(
                put('/api/customers/guests/invite')
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param('customerId', customerId.toString())
                        .param('eventId', eventId.toString())
                        .param('guestIds', '4')
        )
                .andExpect(status().isCreated())

        BDDMockito.verify(customerService, times(1))
                .addGuestsToEvent(eq(customerId), eq(eventId), eq(guestIds))

    }
}
