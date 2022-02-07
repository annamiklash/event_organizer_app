package pjatk.pro.event_organizer_app.security.password;


import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PasswordEncoderSecurity {

    private final PasswordEncoder encoder = new BCryptPasswordEncoder();

    public String bcryptEncryptor(String plainText) {
        return encoder.encode(plainText);
    }

    public Boolean doPasswordsMatch(String rawPassword,String encodedPassword) {
        return encoder.matches(rawPassword, encodedPassword);
    }
}
