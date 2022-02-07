package pjatk.pro.event_organizer_app.business.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import pjatk.pro.event_organizer_app.address.model.Address;
import pjatk.pro.event_organizer_app.catering.model.Catering;
import pjatk.pro.event_organizer_app.location.model.Location;
import pjatk.pro.event_organizer_app.optional_service.model.OptionalService;
import pjatk.pro.event_organizer_app.user.model.User;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Set;

@SuperBuilder
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity(name = "business")
@Table(name = "business")
public class Business extends User implements Serializable {

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "business_name", nullable = false)
    private String businessName;

    @Column(name = "phone_number", nullable = false)
    private BigInteger phoneNumber;

    @Column(name = "verification_status", nullable = false)
    private String verificationStatus;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "id_business_address")
    private Address address;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "business", cascade = CascadeType.MERGE)
    private Set<Catering> caterings;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "business", cascade = CascadeType.MERGE)
    private Set<Location> locations;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "business", cascade = CascadeType.MERGE)
    private Set<OptionalService> services;

}
