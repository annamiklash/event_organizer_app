package pjatk.pro.event_organizer_app.reviews.catering.controller

import com.google.common.collect.ImmutableList
import org.mockito.BDDMockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import pjatk.pro.event_organizer_app.common.paginator.CustomPage
import pjatk.pro.event_organizer_app.reviews.catering.service.CateringReviewService
import pjatk.pro.event_organizer_app.reviews.mapper.ReviewMapper
import pjatk.pro.event_organizer_app.table.TableDto
import pjatk.pro.event_organizer_app.test_helper.TestSerializer
import pjatk.pro.event_organizer_app.trait.reviews.ReviewTrait
import pjatk.pro.event_organizer_app.trait.reviews.catering.CateringReviewTrait
import spock.lang.Specification

import static org.mockito.ArgumentMatchers.eq
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(controllers = [CateringReviewController.class])
class CateringReviewControllerTest extends Specification
        implements CateringReviewTrait,
                ReviewTrait {

    @Autowired
    private MockMvc mockMvc

    @MockBean
    private CateringReviewService cateringReviewService

    @WithMockUser
    def "GET api/reviews/catering/allowed/all returns 200 positive test scenario"() {
        given:
        def cateringId = 1L
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

        def cateringReview = fakeCateringReviewWithCustomer
        def reviewList = ImmutableList.of(cateringReview)
        def reviewDto = ReviewMapper.toDto(cateringReview)
        def resultList = ImmutableList.of(reviewDto)
        def locationTableDto =
                new TableDto<>(new TableDto.MetaDto(count, pageNo, pageSize, sortBy), resultList)
        def jsonResponse = TestSerializer.serialize(locationTableDto)

        BDDMockito.given(cateringReviewService.getByCateringId(eq(customPage), eq(cateringId)))
                .willReturn(reviewList)
        BDDMockito.given(cateringReviewService.count(eq(cateringId)))
                .willReturn(count)

        expect:
        mockMvc.perform(
                get('/api/reviews/catering/allowed/all')
                        .param('cateringId', cateringId.toString())
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
    def "POST api/reviews/catering returns 200 positive test scenario"() {
        given:
        def id = 1L
        def cateringId = 2L
        def dto = fakeReviewDto

        def cateringReview = fakeCateringReviewWithCustomer
        def result = ReviewMapper.toDto(cateringReview)

        def jsonRequest = TestSerializer.serialize(dto)
        def jsonResponse = TestSerializer.serialize(result)

        BDDMockito.given(cateringReviewService.leaveCateringReview(eq(id), eq(cateringId), eq(dto)))
                .willReturn(cateringReview)

        expect:
        mockMvc.perform(
                post("/api/reviews/catering")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(jsonRequest)
                        .param('id', id.toString())
                        .param('cateringId', cateringId.toString())
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))
    }
}
