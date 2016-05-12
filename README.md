A flashcard web service educational tool.  The two primary entities a user can create are a QuestionBank of questions, and a Deck of flashcards.  Users can publish a set of questions for others to answer; and publish their own answers in flashcards.  Other users of the service can add annotation messages to offer critiques or alternative answers to flashcard questions.
 
Feature Set:
Spring MVC with a RESTful json API
Spring Data generated Repositories for DAO
Spring Data JPA Specifications for searching, and pagination
Spring Security with stateless authorization using JWT
Handler interceptors to limit the rate of posts by account.
Spring scheduled tasks to purge accounts with expired registration tokens.
Account registration, and confirmation email with Spring Boot Mail
Image attachment upload, and dynamic resizing for mobile devices.
Verification of content-type in audio / image file attachments
 
## Installation

$ git clone
$ mvn clean generate-sources compile
Configure the datasource in dev-datasource.properties

Configure mail settings in application-local.properties

$ mvn spring-boot:run

Logging can be controlled through the system properties; LOG_APP_LEVEL, LOG_SQL_LEVEL, etc.  See log4j2.xml for details
$ mvn spring-boot:run -DLOG_APP_LEVEL=DEBUG -DLOG_MVC_LEVEL=INFO


The resource/src/test/resources/scripts contains a python utility script crud.py for testing, and python unit-tests:

$ python3 -m unittest tests.test_auth_jwt

You can enable debugging with the DEBUG environment variable:

$ DEBUG=true python3 -m unittest tests.test_auth_jwt


Logging map be reconfigured at runtime through the /admin/log endpoint
$ python crud.py --login --user "admin@example.com" admin/log --json '{ "log" : "LOG_SQL_LEVEL", "level" : "DEBUG" }'


## Testing
To use an H2 embedded database with populated data, use:

$ mvn exec:java -Dstart-class="com.pliesveld.populator.PopulateFlashnoteContainerApplication" -Dspring.config.location=classpath:/dev-datasource.properties,classpath:/override/dev-datasource-override.properties -Dspring.profiles.active=auth,local

Removing the auth Spring profile will disable Spring Security.

Setup a test email server
$ git clone https://github.com/Nilhcem/FakeSMTP && cd FakeSMTP
$ mvn clean compile package
$ java -jar target/fakeSMTP-2.1-SNAPSHOT.jar -p 2525 -s


The two main user resources are QuestionBanks and Decks.  A QuestionBank contains a set of questions.  A Deck contains a set of question-answer pairs.  A question may be shared with multiple QuestionBank, or Deck entities.  Both of which are associated with a category.  The following examples have an existing category with id = 6.

The following examples use the crud.py utility.  It is a simple wrappper around the requests module.  

$ python3 crud.py categories 

Creating a new questionbank

$ python3 crud.py questionbanks --json '
    { 
      "category" : { "id" : "6" },
      "description" : "A new questionbank", 
      "questions" : [
            {"content" : "A new question"}
      ]
    }'


Listing all question banks

$ python3 crud.py questionbanks

Listing speficic questionbank with id 37

$ python3 crud.py questionbanks/37


Creating a new flashcard

$ python3 crud.py decks --json '
    { "category" : { "id": "6" },
      "description" : "A new flashcard", 
      "flashcards" : [ 
            { "question" : { "content" : "A new question" }, 
              "answer" : { "content" : "An answer to the question" } 
            }
      ]
    }'

Listing all decks

$ python3 crud.py decks 


Both endpoints support searching by a single search term.  The description and the contained statements are searched.

$ python3 crud.py decks/search --params '{ "query" : "new" }'
$ python3 crud.py questionbanks/search --params '{ "query" : "Object" }'
