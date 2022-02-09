# Back-end installation
1. Create new empty database and restore from the `dump.sql` file

2. In the `application.yml` file change fields marked as CHANGE_ME for the appropriate values

```java
    url: CHANGE_ME
        username: CHANGE_ME
        password: CHANGE_ME
```
3. In the project root run the command
```bash
.\gradlew clean bootRun
```

After, the tests for the application will start running and when they are successfully completed the application will be ready to be used.


To disable running tests before the start of the application, comment out the following line in the `build.gradle` file.
```java
bootRun.dependsOn(test)
```

### Setting up application email
The credentials for the email are as follows:

- Email: `testsocialeventorg@gmail.com`

- Password: `PRO_password1!`

It is also important to allows anauthorized application to use given email which is possible to change in the Gmail account settings as follows:
![alt text](https://devanswers.co/wp-content/uploads/2017/02/gmail-allow-less-secure-apps.png)

The email and password used for the application can be viewed and changed in the `application.yml` file located in the social_event_support_back folder in the resources directory (`src/main/resources`).