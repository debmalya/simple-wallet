# Technology stack
* Java 11
* Spring boot ( with Spring Data JPA)
* H2 Database
* Maven
# Build
Use `mvn compile` to build.
## Linting

Formatting follows Google Java Format. It will be enforced during `verify` phase of maven. To
fix reported issues `mvn spotless:apply` can be used.

## Tests
`mvn test` should run unit tests.
Tests that are not at component/class level i.e. requiring Spring container are suffixed with IT and are run via `mvn integration-test`.

## Coverage
Coverage
Unit test coverage using Jacoco can be run via `mvn clean test jacoco:report` and report will be generated in target/site/jacoco/index.html. For integration tests run mvn clean test-compile failsafe:integration-test jacoco:report.

## Run
`mvn spring-boot:run`

## Install
`mvn install`
