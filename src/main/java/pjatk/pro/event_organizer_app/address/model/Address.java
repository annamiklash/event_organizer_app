package pjatk.pro.event_organizer_app.address.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Entity(name = "address")
@Table(name = "address")
public class Address implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_address")
    private Long id;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String streetName;

    @Column(nullable = false)
    private int streetNumber;

    @Column(nullable = false)
    private String zipCode;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime modifiedAt;

    private LocalDateTime deletedAt;

}
