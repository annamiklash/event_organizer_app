package pjatk.pro.event_organizer_app.trait.customer.message

import pjatk.pro.event_organizer_app.customer.message.dto.MessageDto

trait MessageDtoTrait {

    MessageDto fakeMessageDto = MessageDto.builder()
            .subject("SAMPLE SUBJECGT")
            .content("SAMPLE CONTENT")
            .receiverEmail("receiver@email.com")
            .build()

}