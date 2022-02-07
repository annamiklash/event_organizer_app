package pjatk.pro.event_organizer_app.common.util;

import lombok.experimental.UtilityClass;
import org.springframework.mail.SimpleMailMessage;

import static pjatk.pro.event_organizer_app.common.constants.Const.APP_EMAIL;

@UtilityClass
public class EmailUtil {


    public SimpleMailMessage buildEmail(String content, String sendTo, String subject, String replyToEmail) {

        final SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(APP_EMAIL);
        simpleMailMessage.setTo(sendTo);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(content);

        if (replyToEmail != null) {
            simpleMailMessage.setReplyTo(replyToEmail);
        }

        return simpleMailMessage;
    }
}
