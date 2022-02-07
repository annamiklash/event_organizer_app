package pjatk.pro.event_organizer_app.address.service;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pjatk.pro.event_organizer_app.address.mapper.AddressMapper;
import pjatk.pro.event_organizer_app.address.model.Address;
import pjatk.pro.event_organizer_app.address.model.dto.AddressDto;
import pjatk.pro.event_organizer_app.address.repository.AddressRepository;
import pjatk.pro.event_organizer_app.common.helper.TimestampHelper;
import pjatk.pro.event_organizer_app.common.mapper.PageableMapper;
import pjatk.pro.event_organizer_app.common.paginator.CustomPage;
import pjatk.pro.event_organizer_app.exceptions.NotFoundException;

import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class AddressService {

    private final AddressRepository addressRepository;

    private final TimestampHelper timestampHelper;

    public ImmutableList<Address> list(CustomPage customPage) {
        final Pageable paging = PageableMapper.map(customPage);
        final Page<Address> page = addressRepository.findAll(paging);

        return ImmutableList.copyOf(page.get().collect(Collectors.toList()));
    }

    public Address get(long id) {
        return addressRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Address with id " + id + " DOES NOT EXIST"));
    }

    public Address getByUserId(long id) {
        return addressRepository.findByUserId(id)
                .orElseThrow(() -> new NotFoundException("Address with user id " + id + " DOES NOT EXIST"));
    }

    public Address getByLocationId(long id) {
        return addressRepository.findByLocationId(id)
                .orElseThrow(() -> new NotFoundException("Address with location id " + id + " DOES NOT EXIST"));
    }

    public Address getByCateringId(long id) {
        return addressRepository.findByCateringId(id)
                .orElseThrow(() -> new NotFoundException("Address with catering id " + id + " DOES NOT EXIST"));
    }

    public Address getByServiceId(long id) {
        return addressRepository.findByServiceId(id)
                .orElseThrow(() -> new NotFoundException("Address with service id " + id + " DOES NOT EXIST"));
    }

    public boolean addressWithIdExists(Long id) {
        return addressRepository.existsById(id);
    }

    public Address create(AddressDto dto) {
        final Address address = AddressMapper.fromDto(dto);

        address.setCreatedAt(timestampHelper.now());
        address.setModifiedAt(timestampHelper.now());

        save(address);
        return address;
    }

    public Address edit(long id, AddressDto dto) {
        if (!addressWithIdExists(id)) {
            throw new NotFoundException("Address with ID " + id + " does not exist");
        }
        final Address address = get(id);

        address.setStreetName(dto.getStreetName());
        address.setStreetNumber(dto.getStreetNumber());
        address.setZipCode(dto.getZipCode());
        address.setModifiedAt(timestampHelper.now());

        save(address);

        return address;
    }

    public void delete(long id) {
        final Address address = get(id);

        address.setModifiedAt(timestampHelper.now());
        address.setDeletedAt(timestampHelper.now());

        addressRepository.save(address);
    }

    public void delete(Address address) {
        addressRepository.delete(address);
    }

    public Long count() {
        return addressRepository.count();
    }

    private void save(Address address) {
        addressRepository.save(address);
    }

}
