# Campr Injection Workshop

## Triggering the CSRF Vulnerability

Run the application and trigger the CSRF vulnerability.

1. Start the application.

  ```bash
  ./gradlew clean run
  ```

2. Go to localhost:8080 and login as admin with the username/password:
   admin/admin

3. Open evil.html in a web browser and click the button

4. Log out of the vendor portal, then try to log back in as admin

## Fixing CSRF

Recommended: Use Intellij to open and import the application’s build.gradle
file. This will sync your gradle system to the IDE.

If you don’t use Intellij, you can sync the gradle system according to your
environment tool.

Run the tests in debug to see the failing security test in detail.

```bash
./gradlew clean test --debug
```

Now use your wits to get the first test to pass!

## Requirements

* JDK 1.7+
* A Java IDE

## Resources

* [Spring Security](http://projects.spring.io/spring-security/#quick-start)
* [OWASP CSRF Prevention Cheat Sheet](https://www.owasp.org/index.php/CSRF_Prevention_Cheat_Sheet)
