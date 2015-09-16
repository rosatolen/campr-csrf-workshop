# Campr Injection Workshop

## Getting started

Run the application and trigger the CSRF vulnerability.

1. Start the application.
```bash
./gradlew clean bootRun
```

2. Go to localhost:8080 and login as admin with the username/password:
admin/admin

3. Edit the evil.html in the main project directory to trigger the malicious CSRF attack.

4. Start a simple http server in the main project directory to start your attack (Note: make sure you have both the Vendor Portal on port 8080 and this server running on 8000):

 ```bash
 python -mhttp.server
 ```

5. Open the localhost:8000/evil.html in a browser window and trigger the attack.

## Fixing CSRF

Recommended: Use Intellij to open and import the application’s build.gradle file. This will sync your gradle system to the IDE.

If you don’t use Intellij, you can sync the gradle system according to your environment tool.

Run the tests in debug to see the failing security test in detail.

```bash
./gradlew clean test --debug
```

It will take more than an hour to properly implement CSRF protection, but the first step is to get the application to return a 403 forbidden action.

Now use your wits to get the first test to pass! After the session, go ahead and look into what it would take to properly configure protection for your web application and look into what tools are provided to help you against CSRF in your tech stack.

## Requirements

* JDK 1.7+
* A Java IDE

## Resources
[Spring Security](http://projects.spring.io/spring-security/#quick-start)
[OWASP CSRF Prevention Cheat Sheet](https://www.owasp.org/index.php/CSRF_Prevention_Cheat_Sheet)
