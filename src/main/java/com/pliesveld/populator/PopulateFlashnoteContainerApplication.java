package com.pliesveld.populator;

import com.pliesveld.flashnote.domain.*;
import com.pliesveld.flashnote.repository.*;
import com.pliesveld.flashnote.util.generator.QuestionGenerator;
import com.pliesveld.flashnote.util.generator.StudentGenerator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
@EnableAutoConfiguration //(exclude = {SecurityAutoConfiguration.class, WebMvcAutoConfiguration.class, ErrorMvcAutoConfiguration.class, DispatcherServletAutoConfiguration.class, EmbeddedServletContainerAutoConfiguration.class})
@ComponentScan(basePackages = { "com.pliesveld" })
@EnableTransactionManagement
public class PopulateFlashnoteContainerApplication {
    private static final Logger LOG = LogManager.getLogger();

    @Autowired
    FlashCardRepository flashCardRepository;
    @Autowired
    DeckRepository deckRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    StudentDetailsRepository studentDetailsRepository;
    @Autowired
    QuestionBankRepository questionBankRepository;
    @Autowired
    NotificationRepository notificationRepository;
    @Autowired
    PopulateSettings populateSettings;

	public static void main(String[] args) {
//        System.setProperty("spring.profiles.default", System.getProperty("spring.profiles.default", "local"));
//        System.setProperty("spring.profiles.active", "local");
        LOG.info("Starting ApplicationContext with initially populated data");

        SpringApplication application = new SpringApplication(PopulateFlashnoteContainerApplication.class);
        //application.setWebEnvironment(false);
//        application.addListeners(new ApplicationPidFileWriter("./bin/app.pid"));
        application.run(args);
	}

    @Bean
    @Order(3)
    public CommandLineRunner populateDefaultCategories() {
        return (args) -> {
            createCategoryIfNotFound("Computer Programming", "Broadly related computer programming category.  Flashnote decks with this category may covers topics such as programming language definitions, data structures, algorithms, operating system principles, and computer science theory");
            createCategoryIfNotFound("Software Development","The software development category focuses on the application of computer technology in the workplace.   Study common libraries and web development frameworks.  Software development theory includes topics like: object oriented programming, design patterns, user experience, and agile development.");
            createCategoryIfNotFound("Information Technology","Focus on the configuration and administration of operating systems, network components, and application services.");
            createCategoryIfNotFound("The Software Interview","Practice the topics commonly asked when applying for a software engineering position.  Can cover the technical explanation of definitions; includes commonly asked questions in algorithms, data structures, languages, modeling, databases, and networking.  May also include the soft interview questions.  Commonly asked questions designed to measure personality traits and team culture fits.");
        };

    }

    @Transactional
    private Category makeCategory(String name, String description, Category root)
    {
        Category category = new Category();
        category.setName(name);
        category.setDescription(description);
        if(root != null)
            category.setParentCategory(root);
        categoryRepository.save(category);
        return category;
    }


    @Transactional
    private Category createCategoryIfNotFound(String name, String description)
    {
        Category category = categoryRepository.findOneByNameEquals(name);

        if( category == null )
        {
            category = new Category();
            category.setName(name);
            category.setDescription(description);
            categoryRepository.save(category);
        }

        return category;
    }

    @Transactional
    private Category makeCategory(String name, String description)
    {
        return makeCategory(name, description, null);

    }

    @Bean
    @Order(4)
    public CommandLineRunner populateDatabaseStatements() {
        LOG.info("Populating database with statements.");
        return (args) -> {

            Category category = createCategoryIfNotFound("Basic HTTP flashnotes","Discusses the topic of web servers, browsers and the HTTP specificatinon.");

            Stream<FlashCard> cards = Stream.of(
                new FlashCard(new Question("What is the difference between a HTTP Redirect and a HTTP Forward?"),new Answer("In an HTTP forward control is processed internally by the server.   A redirect instructs the browser to a different url, potentially on another host.")),
                new FlashCard(new Question("Describe the HTTP status code categories: 1xx, 2xx, 3xx, 4xx, 5xx"),new Answer("1xx informational\n2xx success\n 3xx redirection\n4xx client error\n5xx server error"))
            );
            List<FlashCard> flashCards = cards.collect(Collectors.toList());

            try {
                flashCardRepository.save(flashCards);
            } catch(Exception pe) {
                LOG.warn(pe.getStackTrace());
            }

            StudentDetails studentDetails = studentDetailsRepository.findByName("basic");

            if(studentDetails == null) {
                LOG.warn("Could not find student@example.com");
                return;
            }

            populateMessagesFor(studentDetails);

            Deck deck = new Deck();
            deck.setFlashcards(flashCards);
            deck.setCategory(category);

            try {
                deckRepository.save(deck);
            } catch(Exception pe) {
                pe.printStackTrace();
                LOG.catching(Level.WARN, pe.getCause());
            }

            QuestionBank questionBank = new QuestionBank(category, "Commonly asked web developer questions.");
            questionBank.setCategory(category);
            questionBank = questionBankRepository.save(questionBank);
            questionBank.add(new Question("What is a RESTful web service?"));
            questionBank.add(new Question("What methods are supported by the HTTP specification?"));
            questionBank.add(new Question("What is a Cross-Origin-Request?"));
            questionBank.add(new Question("Give an example of a stateless request."));
            questionBankRepository.save(questionBank);

/*

            questionBank = new QuestionBank(category, "Object Oriented Programming");
            questionBank.add(new Question("What is encapsulation?"));
            questionBank.add(new Question("What is composition?"));
            questionBank.add(new Question("What is inheritance?"));
            questionBank.add(new Question("What is abstraction?"));
            Question question = new Question("What is an object?");
            question.addAnnotation(new AnnotatedStatement(studentDetails,"Add to your description the definition of a class."));
            questionBank.add(question);
            questionBank.setCategory(category);
            questionBankRepository.save(questionBank);
*/
        };
    }

    @Transactional
    private void populateMessagesFor(StudentDetails studentDetails) {
        List<Notification> list = new ArrayList<>(15);

        for(int i = 0; i < 15; i++) {
            String message = "This is a sample notification number " + i;

            Notification notification = new Notification(studentDetails, message);
            list.add(notification);
        }

        notificationRepository.save(list);
    }

    @Bean
    @Order(8)
    @Transactional
    public CommandLineRunner populateStudents() {
        return (args) -> {
            try {
                for(int i = 0;i < populateSettings.getCountStudents(); i++) {
                    Student student = StudentGenerator.randomizedStudent(true);
                    studentRepository.save(student);
                }
            } catch(ConstraintViolationException cve) {
                LOG.error("Could not save student: {}", cve);
            }
        };
    }
/*
    @Bean
    @Order(10)
    @Transactional
    public CommandLineRunner populateBanks() {
        return (args) -> {

            Category qb_cat = createCategoryIfNotFound("TEST CATEGORY", "Category of populated question banks.");
            for(int i = 0;i < populateSettings.getCountBanks(); i++) {

                QuestionBank questionBank = new QuestionBank(qb_cat, "Question Bank #" + i + ".  " + UUID.randomUUID().toString());

                int nQuestions = populateSettings.getCountQuestionPerBank();
                int cnt = 0;
                do {
                    Question question = QuestionGenerator.randomQuestion();
                    questionBank.add(question);
                } while(++cnt < nQuestions);
                questionBankRepository.save(questionBank);
            }

        };
    }
*/
}
