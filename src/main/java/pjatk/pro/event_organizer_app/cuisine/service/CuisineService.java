package pjatk.pro.event_organizer_app.cuisine.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pjatk.pro.event_organizer_app.cuisine.mapper.CuisineMapper;
import pjatk.pro.event_organizer_app.cuisine.model.Cuisine;
import pjatk.pro.event_organizer_app.cuisine.model.dto.CuisineDto;
import pjatk.pro.event_organizer_app.cuisine.repository.CuisineRepository;
import pjatk.pro.event_organizer_app.exceptions.IllegalArgumentException;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class CuisineService {

    private final CuisineRepository cuisineRepository;

    public List<Cuisine> list() {
        return cuisineRepository.findAll(Sort.by("name"));
    }

    public Cuisine create(CuisineDto dto) {
        if (exists(dto.getName())) {
            throw new IllegalArgumentException("Cuisine with name " + dto.getName() + " already exists");
        }
        final Cuisine cuisine = CuisineMapper.fromDto(dto);

        cuisineRepository.save(cuisine);

        return cuisine;
    }

    public Cuisine getByName(String name) {
        return cuisineRepository.findByName(name);
    }

    private boolean exists(String name) {
        return cuisineRepository.existsByName(name);
    }

}
