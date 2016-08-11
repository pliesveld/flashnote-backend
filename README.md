A web-service educational tool for creating flashcards.  The two primary entities a user can create are a QuestionBank of questions, and a Deck of Flashcards.  Users can publish a set of questions for others to answer; and publish their own answers in flashcards.  A single Question entity can be shared with many QuestionBanks or Flashcards, so that updates to the original Question statement are reflected.  Users can use annotations to offer critiques.
 
Feature Set:
- Spring MVC with a RESTful json API
- Spring Data generated Repositories for DAO
- Spring Data JPA Specifications for searching, and pagination
- Hibernate Validation on service methods, and web controllers.
- Spring Security protected routes and method invocations
- Stateless authorization in Spring Security using JWT
- Handler interceptors to limit the rate of posts by account.
- Spring scheduled tasks to purge accounts with expired registration tokens.
- Account registration, and confirmation email with Spring Boot Mail
- Image attachment upload, and dynamic resizing for mobile devices.
- Validates contents of audio / image attachments

Hibernate generated [Database Schema](doc/schema.jpg)
 
## Setup 

$ git clone

$ mvn clean generate-sources compile

Configure database settings in dev-datasource.properties

Configure mail settings in application-local.properties

$ mvn spring-boot:run

## Debugging

Logging can be controlled through the system properties; LOG_APP_LEVEL, LOG_SQL_LEVEL, etc.  See log4j2.xml for details

$ mvn spring-boot:run -DLOG_APP_LEVEL=DEBUG -DLOG_MVC_LEVEL=INFO

Logging can be reconfigured at runtime:

$ python crud.py --login --user "admin@example.com" admin/log --json '{ "log" : "LOG_SQL_LEVEL", "level" : "DEBUG" }'

## Testing

See [TESTING.md](doc/TESTING.md)
