package pjatk.pro.event_organizer_app.user.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Async
    public void sendEmail(SimpleMailMessage email) {
        mailSender.send(email);
    }
}
