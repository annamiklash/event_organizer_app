package pjatk.pro.event_organizer_app.trait.user

import pjatk.pro.event_organizer_app.security.model.UserCredentials

trait UserCredentialsTrait {

    UserCredentials fakeBusinessUserCredentials = UserCredentials.builder()
            .login('busines@email.com')
            .userId(1)
            .userType('B' as char)
            .build()

    UserCredentials fakeCustomerUserCredentials = UserCredentials.builder()
            .login('customer@email.com')
            .userId(1)
            .userType('C' as char)
            .build()

    UserCredentials fakeAdminUserCredentials = UserCredentials.builder()
            .login('admin@email.com')
            .userId(1)
            .userType('A' as char)
            .build()

}