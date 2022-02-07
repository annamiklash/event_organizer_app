package pjatk.pro.event_organizer_app.image.controller

import com.google.common.collect.ImmutableList
import org.mockito.BDDMockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import pjatk.pro.event_organizer_app.image.mapper.ImageMapper
import pjatk.pro.event_organizer_app.image.service.CateringImageService
import pjatk.pro.event_organizer_app.test_helper.TestSerializer
import pjatk.pro.event_organizer_app.trait.image.CateringImageTrait
import spock.lang.Specification

import static org.mockito.ArgumentMatchers.any
import static org.mockito.ArgumentMatchers.eq
import static org.mockito.Mockito.times
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(controllers = [CateringImageController.class])
class CateringImageControllerTest extends Specification
        implements CateringImageTrait {

    @Autowired
    private MockMvc mockMvc

    @MockBean
    private CateringImageService cateringImageService


    @WithMockUser
    def "GET api/images/catering/allowed/all returns 200 positive test scenario"() {
        given:
        def cateringId = 1L

        def cateringImage = fakeCateringImage
        def cateringImageList = ImmutableList.of(cateringImage)
        def resultList = ImmutableList.of(ImageMapper.toDto(cateringImage))
        def jsonResponse = TestSerializer.serialize(resultList)

        BDDMockito.given(cateringImageService.findByCateringId(eq(cateringId)))
                .willReturn(cateringImageList)

        expect:
        mockMvc.perform(get('/api/images/catering/allowed/all')
                .param("cateringId", cateringId.toString())
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))

    }

    @WithMockUser(authorities = ['BUSINESS'])
    def "POST api/images/catering/upload returns 200 positive test scenario"() {
        given:
        def cateringId = 1L
        def file = new MockMultipartFile("data", "filename.txt", "text/plain", "some xml".getBytes())

        expect:
        mockMvc.perform(
                MockMvcRequestBuilders.multipart('/api/images/catering/upload')
                        .file("file", file.getBytes())
                        .param("cateringId", cateringId.toString())
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
        )
                .andExpect(status().isOk())

        BDDMockito.verify(cateringImageService, times(1))
                .upload(eq(cateringId), any())

    }

    @WithMockUser(authorities = ['BUSINESS'])
    def "DELETE api/images/catering returns 200 positive test scenario"() {
        given:
        def imageId = 2L

        expect:
        mockMvc.perform(
                delete('/api/images/catering')
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("id", imageId.toString())
        )
                .andExpect(status().isOk())

        BDDMockito.verify(cateringImageService, times(1))
                .deleteById(eq(imageId))

    }

}
