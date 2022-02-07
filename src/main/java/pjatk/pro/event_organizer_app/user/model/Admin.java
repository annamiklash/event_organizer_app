package pjatk.pro.event_organizer_app.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "admins")
@Entity(name = "admins")
public class Admin extends User implements Serializable {

    private String access;


}
