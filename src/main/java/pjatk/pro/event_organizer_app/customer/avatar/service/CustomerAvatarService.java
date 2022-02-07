package pjatk.pro.event_organizer_app.customer.avatar.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pjatk.pro.event_organizer_app.customer.avatar.model.CustomerAvatar;
import pjatk.pro.event_organizer_app.customer.avatar.repository.CustomerAvatarRepository;
import pjatk.pro.event_organizer_app.customer.model.Customer;
import pjatk.pro.event_organizer_app.customer.repository.CustomerRepository;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class CustomerAvatarService {

    private final CustomerAvatarRepository customerAvatarRepository;

    private final CustomerRepository customerRepository;

    public void deleteById(long id) {
        final Optional<Customer> optionalCustomer = customerRepository.getByIdWithAvatar(id);

        if(optionalCustomer.isEmpty()){
            return;
        }
        final Customer customer = optionalCustomer.get();
        final long avatarId = customer.getAvatar().getId();
        customer.setAvatar(null);
        customerRepository.save(customer);

        customerAvatarRepository.deleteById(avatarId);
    }

    public void save(CustomerAvatar customerAvatar) {
        customerAvatarRepository.save(customerAvatar);
    }

    public void delete(CustomerAvatar customerAvatar) {
        customerAvatarRepository.delete(customerAvatar);
    }
}
