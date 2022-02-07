package pjatk.pro.event_organizer_app.location.controller

import com.google.common.collect.ImmutableList
import org.mockito.BDDMockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import pjatk.pro.event_organizer_app.location.mapper.LocationDescriptionItemMapper
import pjatk.pro.event_organizer_app.location.model.LocationDescriptionItem
import pjatk.pro.event_organizer_app.location.service.LocationDescriptionItemService
import pjatk.pro.event_organizer_app.test_helper.TestSerializer
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(controllers = [LocationDescriptionItemController.class])
class LocationDescriptionItemControllerTest extends Specification {

    @Autowired
    private MockMvc mockMvc

    @MockBean
    private LocationDescriptionItemService service

    @WithMockUser
    def "GET api/location_description/allowed/all returns 200 positive test scenario"() {
        given:
        def locationDescriptionItem = LocationDescriptionItem.builder()
                .id('Has Wifi')
                .description('Description')
                .build()
        def locationDescriptionItemList = ImmutableList.of(locationDescriptionItem)
        def locationDescriptionDto = LocationDescriptionItemMapper.toDto(locationDescriptionItem)
        def resultList = ImmutableList.of(locationDescriptionDto)

        def jsonResponse = TestSerializer.serialize(resultList)

        BDDMockito.given(service.listAllDistinct())
                .willReturn(locationDescriptionItemList)

        expect:
        mockMvc.perform(get('/api/location_description/allowed/all')
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(jsonResponse))

    }

}
