# Campr Injection Workshop

## Getting started

Run the application and trigger the CSRF vulnerability

```bash
./gradlew clean bootRun
```

## Fixing CSRF

Recommended: Use Intellij to open and import the application’s build.gradle file. This will sync your gradle system to the IDE.

If you don’t use Intellij, you can sync the gradle system according to your environment tool.

Run the tests in debug to see the failing security test in detail.

```bash
./gradlew clean test --debug
```

Now use your wits to get the test to pass!

## Requirements

* JDK 1.7+
* A Java IDE

## Resources
[Spring Security](http://projects.spring.io/spring-security/#quick-start)
