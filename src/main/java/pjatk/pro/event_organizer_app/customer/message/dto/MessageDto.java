package pjatk.pro.event_organizer_app.customer.message.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageDto {

    @NotNull
    private String subject;

    @NotNull
    private String content;

    private String replyToEmail;

    private String receiverEmail;

}
