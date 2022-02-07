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
import pjatk.pro.event_organizer_app.image.service.OptionalServiceImageService
import pjatk.pro.event_organizer_app.test_helper.TestSerializer
import pjatk.pro.event_organizer_app.trait.image.OptionalServiceImageTrait
import spock.lang.Specification

import static org.mockito.ArgumentMatchers.any
import static org.mockito.ArgumentMatchers.eq
import static org.mockito.Mockito.times
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(controllers = [OptionalServiceImageController.class])
class OptionalServiceImageControllerTest extends Specification  
        implements OptionalServiceImageTrait {

    @Autowired
    private MockMvc mockMvc

    @MockBean
    private OptionalServiceImageService optionalServiceImageService


    @WithMockUser
    def "GET api/images/service/allowed/all returns 200 positive test scenario"() {
        given:
        def serviceId = 1L

        def optionalServiceImage = fakeOptionalServiceImage
        def optionalServiceImageList = ImmutableList.of(optionalServiceImage)
        def resultList = ImmutableList.of(ImageMapper.toDto(optionalServiceImage))
        def jsonResponse = TestSerializer.serialize(resultList)

        BDDMockito.given(optionalServiceImageService.findByServiceId(eq(serviceId)))
                .willReturn(optionalServiceImageList)

        expect:
        mockMvc.perform(get('/api/images/service/allowed/all')
                .param("serviceId", serviceId.toString())
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))

    }


    @WithMockUser(authorities = ['BUSINESS'])
    def "POST api/images/service/upload returns 200 positive test scenario"() {
        given:
        def serviceId = 1L
        def file = new MockMultipartFile("data", "filename.txt", "text/plain", "some xml".getBytes())

        expect:
        mockMvc.perform(
                MockMvcRequestBuilders.multipart('/api/images/service/upload')
                        .file("file", file.getBytes())
                        .param("serviceId", serviceId.toString())
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
        )
                .andExpect(status().isOk())

        BDDMockito.verify(optionalServiceImageService, times(1))
                .upload(eq(serviceId), any())

    }

    @WithMockUser(authorities = ['BUSINESS'])
    def "DELETE api/images/service returns 200 positive test scenario"() {
        given:
        def newId = 2L

        expect:
        mockMvc.perform(
                delete('/api/images/service')
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("id", newId.toString())
        )
                .andExpect(status().isOk())

        BDDMockito.verify(optionalServiceImageService, times(1))
                .deleteById(newId)

    }
}