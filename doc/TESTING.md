To use an H2 embedded database with populated data, use:

$ mvn exec:java -Dstart-class="com.pliesveld.populator.repository.PopulateFlashnoteContainerApplication" -Dspring.config.location=classpath:/dev-datasource.properties,classpath:/override/dev-datasource-override.properties -Dspring.profiles.active=auth,local -Dexec.args="test_repository"

Where the directory test_repository contains json files used to populate the in-memory database.  See the unit tests for more examples.

Removing the auth Spring profile will disable Spring Security.  Allowing you to test the endpoints without access controls.

Setup a test email server

$ git clone https://github.com/Nilhcem/FakeSMTP && cd FakeSMTP

$ mvn clean compile package

$ java -jar target/fakeSMTP-2.1-SNAPSHOT.jar -p 2525 -s


The two main user resources are QuestionBanks and Decks.  A QuestionBank contains a set of questions.  A Deck contains a set of question-answer pairs.  A question may be shared with multiple QuestionBank, or Deck entities.  Both of which are associated with a category.  The following examples have an existing category with id = 6.


Or test with the utility script crud.py:

$ curl 'http://localhost:9000/categories'

$ python3 crud.py categories --print

Creating a new questionbank

$ python3 crud.py -vv questionbanks --json '
    { 
      "category" : { "id" : "500" },
      "description" : "A new questionbank", 
      "questions" : [
            {"content" : "A new question"}
      ]
    }'


Listing all question banks

$ python3 crud.py questionbanks

Listing specific questionbank

$ python3 crud.py questionbanks/8

Adding a new question to an existing question bank

$ python3 crud.py questionbanks/8 --json '{ "content": "What is an abstraction?" }' --print

Creating a new flashcard

$ python3 crud.py -vv decks --json '
    { "category" : { "id": "500" },
      "description" : "A new flashcard", 
      "flashcards" : [ 
            { "question" : { "content" : "A new question" }, 
              "answer" : { "content" : "An answer to the question" } 
            }
      ]
    }'

Listing all decks

$ python3 crud.py decks --print

Searching decks containing a keyword 

$ python3 crud.py decks/search --params '{ "query" : "new" }' --print

Searching questionbanks containing a keyword

$ python3 crud.py questionbanks --params '{ "content" : "Object" }' --print




## Integration Tests

You can run the python integration tests manually:

$ cd src/test/resources/scripts

$ python3 -m unittest tests.test_auth_jwt

$ DEBUG=true python3 -m unittest tests.test_questionbank.QuestionBankCreateTest

