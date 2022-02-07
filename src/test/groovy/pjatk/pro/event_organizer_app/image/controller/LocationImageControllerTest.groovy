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
import pjatk.pro.event_organizer_app.image.service.LocationImageService
import pjatk.pro.event_organizer_app.test_helper.TestSerializer
import pjatk.pro.event_organizer_app.trait.image.LocationImageTrait
import spock.lang.Specification

import static org.mockito.ArgumentMatchers.any
import static org.mockito.ArgumentMatchers.eq
import static org.mockito.Mockito.times
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(controllers = [LocationImageController.class])
class LocationImageControllerTest extends Specification
        implements LocationImageTrait {

    @Autowired
    private MockMvc mockMvc

    @MockBean
    private LocationImageService locationImageService


    @WithMockUser
    def "GET api/images/location/allowed/all returns 200 positive test scenario"() {
        given:
        def locationId = 1L

        def locationImage = fakeLocationImage
        def locationImageList = ImmutableList.of(locationImage)
        def resultList = ImmutableList.of(ImageMapper.toDto(locationImage))
        def jsonResponse = TestSerializer.serialize(resultList)

        BDDMockito.given(locationImageService.findByLocationId(eq(locationId)))
                .willReturn(locationImageList)

        expect:
        mockMvc.perform(get('/api/images/location/allowed/all')
                .param("locationId", locationId.toString())
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))

    }


    @WithMockUser(authorities = ['BUSINESS'])
    def "POST api/images/location/upload returns 200 positive test scenario"() {
        given:
        def locationId = 1L
        def file = new MockMultipartFile("data", "filename.txt", "text/plain", "some xml".getBytes())

        expect:
        mockMvc.perform(
                MockMvcRequestBuilders.multipart('/api/images/location/upload')
                        .file("file", file.getBytes())
                        .param("locationId", locationId.toString())
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
        )
                .andExpect(status().isOk())

        BDDMockito.verify(locationImageService, times(1))
                .upload(eq(locationId), any())

    }

    @WithMockUser(authorities = ['BUSINESS'])
    def "DELETE api/images/location returns 200 positive test scenario"() {
        given:
        def id = 2L

        expect:
        mockMvc.perform(
                delete('/api/images/location')
                        .param("id", id.toString())
        )
                .andExpect(status().isOk())

        BDDMockito.verify(locationImageService, times(1))
                .deleteById(eq(id))

    }
}
