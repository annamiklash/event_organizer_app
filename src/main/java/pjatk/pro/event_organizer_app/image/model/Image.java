package pjatk.pro.event_organizer_app.image.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@SuperBuilder
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@MappedSuperclass
public abstract class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_image")
    private long id;

    @Column(nullable = false)
    @Type(type = "org.hibernate.type.BinaryType")
    private byte[] image;

    private String fileName;

}
