package pjatk.pro.event_organizer_app.location.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pjatk.pro.event_organizer_app.location.model.LocationDescriptionItem;
import pjatk.pro.event_organizer_app.location.repository.LocationDescriptionItemRepository;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class LocationDescriptionItemService {

   private final LocationDescriptionItemRepository repository;

    public LocationDescriptionItem getById(String id) {

        return repository.getLocationDescriptionItemByName(id);
    }

    public List<LocationDescriptionItem> listAllDistinct() {
        return repository.findAllDistinct();
    }
}
