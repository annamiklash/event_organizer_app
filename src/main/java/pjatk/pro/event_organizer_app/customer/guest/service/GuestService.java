package pjatk.pro.event_organizer_app.customer.guest.service;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pjatk.pro.event_organizer_app.common.helper.TimestampHelper;
import pjatk.pro.event_organizer_app.common.mapper.PageableMapper;
import pjatk.pro.event_organizer_app.common.paginator.CustomPage;
import pjatk.pro.event_organizer_app.customer.guest.mapper.GuestMapper;
import pjatk.pro.event_organizer_app.customer.guest.model.Guest;
import pjatk.pro.event_organizer_app.customer.guest.model.dto.GuestDto;
import pjatk.pro.event_organizer_app.customer.guest.repository.GuestRepository;
import pjatk.pro.event_organizer_app.customer.model.Customer;
import pjatk.pro.event_organizer_app.customer.repository.CustomerRepository;
import pjatk.pro.event_organizer_app.exceptions.NotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class GuestService {

    private final GuestRepository guestRepository;

    private final CustomerRepository customerRepository;

    private final TimestampHelper timestampHelper;

    public ImmutableList<Guest> list(CustomPage customPage, String keyword) {
        keyword = Strings.isNullOrEmpty(keyword) ? "" : keyword.toLowerCase();

        final Pageable paging = PageableMapper.map(customPage);
        final Page<Guest> page = guestRepository.findAllWithKeyword(paging, keyword);

        return ImmutableList.copyOf(page.get().collect(Collectors.toList()));
    }

    public List<Guest> listAllByCustomerId(long id) {
        if (!customerRepository.existsById(id)) {
            throw new NotFoundException("No customer with id " + id);
        }

        return guestRepository.getAllByCustomer_Id(id);
    }

    public List<Guest> getGuestsByIds(List<Long> guestIds) {
        return guestIds.stream()
                .map(guestRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    public Guest get(long id) {
        return guestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No guest with id " + id));

    }

    public Guest create(long customerId, GuestDto dto) {
        final Optional<Customer> optionalCustomer = customerRepository.getByIdWithUser(customerId);
        if (optionalCustomer.isEmpty()) {
            throw new NotFoundException("No optionalCustomer with id " + customerId);
        }
        final Customer customer = optionalCustomer.get();
        final Guest guest = GuestMapper.fromDto(dto);

        guest.setCustomer(customer);
        guest.setCreatedAt(timestampHelper.now());
        guest.setModifiedAt(timestampHelper.now());
        guestRepository.save(guest);
        return guest;
    }

    public Guest edit(long guestId, GuestDto dto) {
        final Guest guest = get(guestId);

        guest.setFirstName(dto.getFirstName());
        guest.setLastName(dto.getLastName());
        guest.setEmail(dto.getEmail());
        guest.setModifiedAt(timestampHelper.now());

        guestRepository.save(guest);
        return guest;
    }

    public void delete(long customerId, long guestId) {
        final Optional<Customer> optionalCustomer = customerRepository.getByIdWithUser(customerId);
        if (optionalCustomer.isEmpty()) {
            throw new NotFoundException("No optionalCustomer with id " + customerId);
        }
        final Guest guest = get(guestId);
        guestRepository.delete(guest);
    }

    public void delete(Guest guest) {
        guestRepository.delete(guest);
    }

}
