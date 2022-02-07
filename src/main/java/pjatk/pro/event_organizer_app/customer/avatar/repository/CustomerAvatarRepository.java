package pjatk.pro.event_organizer_app.customer.avatar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pjatk.pro.event_organizer_app.customer.avatar.model.CustomerAvatar;

@Repository
public interface CustomerAvatarRepository extends JpaRepository<CustomerAvatar, Long> {

//    Optional<CustomerAvatar> findCustomerAvatarByCustomer_Id(long id);
}
