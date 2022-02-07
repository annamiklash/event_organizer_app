package pjatk.pro.event_organizer_app.location.model;

import lombok.*;
import pjatk.pro.event_organizer_app.address.model.Address;
import pjatk.pro.event_organizer_app.availability.location.model.LocationAvailability;
import pjatk.pro.event_organizer_app.business.model.Business;
import pjatk.pro.event_organizer_app.businesshours.location.model.LocationBusinessHours;
import pjatk.pro.event_organizer_app.catering.model.Catering;
import pjatk.pro.event_organizer_app.exceptions.IllegalArgumentException;
import pjatk.pro.event_organizer_app.image.model.LocationImage;
import pjatk.pro.event_organizer_app.location.locationforevent.model.LocationForEvent;
import pjatk.pro.event_organizer_app.reviews.location.model.LocationReview;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Builder
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "location")
@Entity(name = "location")
public class Location implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_location")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private BigInteger phoneNumber;

    @Column(nullable = false)
    private Integer seatingCapacity;

    @Column(nullable = false)
    private Integer standingCapacity;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private BigDecimal dailyRentCost;

    @Column(nullable = false)
    private Integer sizeInSqMeters;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "modified_at", nullable = false)
    private LocalDateTime modifiedAt;

    private LocalDateTime deletedAt;

    @Column(nullable = false)
    private Double rating;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn(name = "id_business")
    private Business business;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "id_location_address")
    private Address locationAddress;

    @EqualsAndHashCode.Exclude
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "catering_location",
            joinColumns = @JoinColumn(name = "id_location"),
            inverseJoinColumns = @JoinColumn(name = "id_catering"))
    private Set<Catering> caterings = new HashSet<>();


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "location_description",
            joinColumns = @JoinColumn(name = "id_location"),
            inverseJoinColumns = @JoinColumn(name = "name"))
    private Set<LocationDescriptionItem> descriptions = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_location")
    private Set<LocationImage> images = new HashSet<>();

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_location")
    private Set<LocationForEvent> locationForEvent = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_location")
    private Set<LocationAvailability> availability;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_location")
    private Set<LocationBusinessHours> locationBusinessHours;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_location")
    private Set<LocationReview> reviews;

    public void addDescriptionItem(LocationDescriptionItem locationDescriptionItem) {
        if (locationDescriptionItem == null) {
            throw new IllegalArgumentException("Location description item cannot be null");
        }

        descriptions.add(locationDescriptionItem);
    }

    public void removeDescriptionItem(LocationDescriptionItem locationDescriptionItem) {
        if (locationDescriptionItem == null) {
            throw new IllegalArgumentException("Location description item cannot be null");
        }

        descriptions.remove(locationDescriptionItem);
    }

    public void addCatering(Catering catering) {
        if (catering == null) {
            throw new IllegalArgumentException("Catering cannot be null");
        }
        caterings.add(catering);
    }

    public void removeCatering(Catering catering) {
        if (catering == null) {
            throw new IllegalArgumentException("Catering cannot be null");
        }
        caterings.remove(catering);
    }
}
