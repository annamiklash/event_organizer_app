package pjatk.pro.event_organizer_app.common.helper;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TimestampHelper {

    public LocalDateTime now() {
        return LocalDateTime.now();
    }

}
