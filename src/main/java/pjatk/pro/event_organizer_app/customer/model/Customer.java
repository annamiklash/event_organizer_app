package pjatk.pro.event_organizer_app.customer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import pjatk.pro.event_organizer_app.customer.avatar.model.CustomerAvatar;
import pjatk.pro.event_organizer_app.customer.guest.model.Guest;
import pjatk.pro.event_organizer_app.event.model.OrganizedEvent;
import pjatk.pro.event_organizer_app.user.model.User;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Set;

@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "customer")
@Entity(name = "customer")
public class Customer extends User implements Serializable {


    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private LocalDate birthdate;

    @Column(nullable = false)
    private BigInteger phoneNumber;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_customer")
    @JsonIgnore
    private Set<Guest> guests;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinColumn(name = "id_customer")
    @JsonIgnore
    private Set<OrganizedEvent> events;

    @OneToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "id_image")
    private CustomerAvatar avatar;
    
    //TODO: add reviews


}
