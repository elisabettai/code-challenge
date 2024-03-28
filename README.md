# Test suite for a Simple Trading Application

This is a simple trading application, extended with Smoke and Unit tests.

## Build and run the tests

To build and test the application, execute in a shell:

```shell
git clone https://github.com/elisabettai/code-challenge.git
cd code-challenge
mvn -U clean verify
```

The test results will be printed in the shell.

## Required tools and libraries

1. [JDK 17](https://www.oracle.com/java/technologies/downloads/#java17)
2. At least [Maven 3.6.3](https://maven.apache.org/download.cgi)

Install both the JDK (define the environment variable `JAVA_HOME` appropriately), then
Maven (define the environment variable `M2_HOME` appropriately).

## Implemented Improvements

1. Unit tests for each component (see *Unit.java files in the [tests](/src/test/java/name/lattuada/trading/tests/) folder)
2. Constraints on DB schema to ensure that orders occure only for existing users and securities
3. Introduced a test parameter for the Cucumber tests to read the base url from the [applications.properties](/src/test/resources/application.properties)
4. Fixed some of the errors notified by SpotBugs (you can check them via `mvn spotbugs:gui`)
5. Fixed ed some of the errors notified by Checkstyle (and eventually tune the settings file). You can check the errors via
    ```shell
   mvn checkstyle:checkstyle
   ```
   then open the HTML report located to `target/site/checkstyle.html`.
