package pjatk.pro.event_organizer_app.trait.user

import pjatk.pro.event_organizer_app.user.model.dto.ChangePasswordDto

trait ChangePasswordDtoTrait {

    ChangePasswordDto fakeChangePasswordDto = ChangePasswordDto.builder()
            .oldPassword('123Password!')
            .newPassword('123Password@')
            .build()

}