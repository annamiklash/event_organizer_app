package pjatk.pro.event_organizer_app.trait.user

import pjatk.pro.event_organizer_app.user.model.User
import pjatk.pro.event_organizer_app.user.model.dto.UserDto

trait UserTrait {

    User fakeUser = User.builder()
            .id(1)
            .type('C' as char)
            .email('test@email.com')
            .build()

    User fakeUserB = User.builder()
            .id(1)
            .type('B' as char)
            .email('test@email.com')
            .build()

    UserDto fakeUserDto = UserDto.builder()
            .id(1)
            .type('C' as char)
            .email('test@email.com')
            .build()
}
