package pjatk.pro.event_organizer_app.trait.user

import pjatk.pro.event_organizer_app.user.model.dto.NewPasswordDto

trait NewPasswordDtoTrait {

    NewPasswordDto fakeNewPasswordDto = NewPasswordDto.builder()
            .password('123Password!')
            .build()
}
