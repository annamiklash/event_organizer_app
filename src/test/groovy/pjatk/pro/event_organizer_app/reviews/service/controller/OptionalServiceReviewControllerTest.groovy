package pjatk.pro.event_organizer_app.reviews.service.controller

import com.google.common.collect.ImmutableList
import org.mockito.BDDMockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import pjatk.pro.event_organizer_app.common.paginator.CustomPage
import pjatk.pro.event_organizer_app.reviews.mapper.ReviewMapper
import pjatk.pro.event_organizer_app.reviews.service.service.OptionalServiceReviewService
import pjatk.pro.event_organizer_app.table.TableDto
import pjatk.pro.event_organizer_app.test_helper.TestSerializer
import pjatk.pro.event_organizer_app.trait.optional_service.OptionalServiceTrait
import pjatk.pro.event_organizer_app.trait.reviews.ReviewTrait
import spock.lang.Specification

import static org.mockito.ArgumentMatchers.eq
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(controllers = [OptionalServiceReviewController.class])
class OptionalServiceReviewControllerTest extends Specification implements
        ReviewTrait, OptionalServiceTrait {

    @Autowired
    private MockMvc mockMvc

    @MockBean
    private OptionalServiceReviewService optionalServiceReviewService

    @WithMockUser
    def "GET api/reviews/service/allowed/all returns 200 positive test scenario"() {
        given:
        def serviceId = 1L
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

        def serviceReview = fakeServiceReviewWithCustomer
        def reviewList = ImmutableList.of(serviceReview)
        def reviewDto = ReviewMapper.toDto(serviceReview)
        def resultList = ImmutableList.of(reviewDto)
        def serviceTableDto =
                new TableDto<>(new TableDto.MetaDto(count, pageNo, pageSize, sortBy), resultList)
        def jsonResponse = TestSerializer.serialize(serviceTableDto)

        BDDMockito.given(optionalServiceReviewService.getByServiceId(eq(customPage), eq(serviceId)))
                .willReturn(reviewList)
        BDDMockito.given(optionalServiceReviewService.count(eq(serviceId)))
                .willReturn(count)

        expect:
        mockMvc.perform(
                get('/api/reviews/service/allowed/all')
                        .param('serviceId', serviceId.toString())
                        .param('pageNo', pageNo.toString())
                        .param('pageSize', pageSize.toString())
                        .param('sortBy', sortBy)
                        .param('order', order)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))
    }

    @WithMockUser(authorities = ['CUSTOMER'])
    def "POST api/reviews/service returns 200 positive test scenario"() {
        given:
        def customerId = 1L
        def serviceId = 2L
        def dto = fakeReviewDto

        def serviceReview = fakeServiceReviewWithCustomer
        def result = ReviewMapper.toDto(serviceReview)

        def jsonRequest = TestSerializer.serialize(dto)
        def jsonResponse = TestSerializer.serialize(result)

        BDDMockito.given(optionalServiceReviewService.leaveServiceReview(eq(customerId), eq(serviceId), eq(dto)))
                .willReturn(serviceReview)

        expect:
        mockMvc.perform(
                post("/api/reviews/service")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(jsonRequest)
                        .param('customerId', customerId.toString())
                        .param('serviceId', serviceId.toString())
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))
    }
}
