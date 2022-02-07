package pjatk.pro.event_organizer_app.location.service

import pjatk.pro.event_organizer_app.location.model.LocationDescriptionItem
import pjatk.pro.event_organizer_app.location.repository.LocationDescriptionItemRepository
import spock.lang.Specification
import spock.lang.Subject

class LocationDescriptionItemServiceTest extends Specification  {

    @Subject
    LocationDescriptionItemService locationDescriptionItemService

    LocationDescriptionItemRepository repository

    def setup() {
        repository = Mock()

        locationDescriptionItemService = new LocationDescriptionItemService(repository)
    }

    def "GetById"() {
        given:
        def id = 'Has WiFi'
        def locationDescriptionItem = LocationDescriptionItem.builder()
                .id('Has WiFi')
                .description('description')
                .build()
        def target = locationDescriptionItem

        when:
        def result = locationDescriptionItemService.getById(id)
        then:
        1 * repository.getLocationDescriptionItemByName(id) >> locationDescriptionItem

        result == target

    }
}
