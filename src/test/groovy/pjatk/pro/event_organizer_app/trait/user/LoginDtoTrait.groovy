package pjatk.pro.event_organizer_app.trait.user

import pjatk.pro.event_organizer_app.user.model.dto.LoginDto

trait LoginDtoTrait {

    LoginDto fakeLoginDto = LoginDto.builder()
            .email('test@email.com')
            .password('123Password!')
            .build()

}