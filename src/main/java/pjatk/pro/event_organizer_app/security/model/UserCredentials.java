package pjatk.pro.event_organizer_app.security.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserCredentials {

    String login;

    Long userId;

    Character userType;
}
