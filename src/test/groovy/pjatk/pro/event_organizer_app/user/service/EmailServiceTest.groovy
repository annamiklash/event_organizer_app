package pjatk.pro.event_organizer_app.user.service

import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import spock.lang.Specification
import spock.lang.Subject

class EmailServiceTest extends Specification {

    @Subject
    EmailService emailService

    JavaMailSender mailSender

    def setup() {
        mailSender = Mock()
        emailService = new EmailService(mailSender)
    }

    def "SendEmail"() {
        given:
        def mail = new SimpleMailMessage();

        when:
        emailService.sendEmail(mail)

        then:
        mailSender.send(mail)

    }
}
