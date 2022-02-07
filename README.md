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
