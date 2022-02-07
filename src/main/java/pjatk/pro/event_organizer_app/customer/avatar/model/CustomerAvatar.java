package pjatk.pro.event_organizer_app.customer.avatar.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import pjatk.pro.event_organizer_app.image.model.Image;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@Table(name = "customer_avatar")
@Entity(name = "customer_avatar")
public class CustomerAvatar extends Image implements Serializable {



}
