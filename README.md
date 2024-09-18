# Java Clock
## Desktop application using maven and the Swing framework.

This project has two profiles: jdk8 and jdk21.

To build an executable jar file compatible with JDK 8 use
```bash
mvn clean install -Pjdk8
```

To build an executable jar file compatible wtih JDK 21 use
```bash
mvn clean install -Pjdk21
```
The application starts in a 400 x 400 pixel frame.

Pressing the letter 'f' key will toggle between full screen mode and normal mode.
