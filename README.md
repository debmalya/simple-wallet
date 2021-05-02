# Story
Imagine customers create digital wallet with merchants. They store their coins and pay time to time using that wallet.
* By calling api http://localhost:8080/api/wallet/v0/create method POST. Sample request
```
{
	"coins":[2,3,1,2,1]
}
```
Sample response
```
{
	"walletId": "29435d59-889b-4515-8f39-4a136149c187",
	"message": "Success",
	"errors": []
}
```
* By calling api http://localhost:8080/api/wallet/v0/get method GET. Sample request
```
{
     "walletId": "29435d59-889b-4515-8f39-4a136149c187"
}
```
Sample response
```
{
    "walletId": "29435d59-889b-4515-8f39-4a136149c187",
    "message": "current coins are [1, 1, 2, 2, 3]",
    "errors": []
}
```
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
