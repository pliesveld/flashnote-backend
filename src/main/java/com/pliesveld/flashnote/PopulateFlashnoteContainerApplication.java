package com.pliesveld.flashnote;

import com.pliesveld.flashnote.domain.*;
import com.pliesveld.flashnote.repository.*;
import com.pliesveld.flashnote.util.generator.StudentGenerator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;
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

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
@EnableAutoConfiguration //(exclude = {SecurityAutoConfiguration.class, WebMvcAutoConfiguration.class, ErrorMvcAutoConfiguration.class, DispatcherServletAutoConfiguration.class, EmbeddedServletContainerAutoConfiguration.class})
@ComponentScan(basePackages = { "com.pliesveld" })
//@EnableScheduling
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

	public static void main(String[] args) {
//        System.setProperty("spring.profiles.default", System.getProperty("spring.profiles.default", "local"));
//        System.setProperty("spring.profiles.active", "local");
        LOG.info("Starting ApplicationContext with initially populated data");

        SpringApplication application = new SpringApplication(PopulateFlashnoteContainerApplication.class);
        //application.setWebEnvironment(false);
        //application.addListeners(new ApplicationPidFileWriter("./bin/app.pid"));
        application.run(args);
	}

    @Bean
    @Order(3)
    public CommandLineRunner populateDefaultCategories() {
        return (args) -> {
            makeCategory("Computer Programming", "Broadly related computer programming category.  Flashnote decks with this category may covers topics such as programming language definitions, data structures, algorithms, operating system principles, and computer science theory");
            makeCategory("Software Development","The software development category focuses on the application of computer technology in the workplace.   Study common libraries and web development frameworks.  Software development theory includes topics like: object oriented programming, design patterns, user experience, and agile development.");
            makeCategory("Information Technology","Focus on the configuration and administration of operating systems, network components, and application services.");
            makeCategory("The Software Interview","Practice the topics commonly asked when applying for a software engineering position.  Can cover the technical explanation of definitions; includes commonly asked questions in algorithms, data structures, languages, modeling, databases, and networking.  May also include the soft interview questions.  Commonly asked questions designed to measure personality traits and team culture fits.");
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
    private Category makeCategory(String name, String description)
    {
        return makeCategory(name, description, null);

    }

    @Bean
    @Order(4)
    public CommandLineRunner populateDatabaseStatements() {
        LOG.info("Populating database with statements.");
        return (args) -> {


            Category category = new Category();
            category.setName("Basic HTTP flashnotes");
            category.setDescription("Discusses the topic of web servers, browsers and the HTTP specificatinon.");
            category = createCategoryIfNotExists(category);

            Stream<FlashCard> cards = Stream.of(
                new FlashCard(new Question("What is the difference between a HTTP Redirect and a HTTP Forward?"),new Answer("In an HTTP forward control is processed internally by the server.   A redirect instructs the browser to a different url, potentially on another host.")),
//                new FlashCard(new Question(),new Answer()),
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

            Deck deck = new Deck();
            deck.setFlashcards(flashCards);
            deck.setCategory(category);
            deck.setAuthor(studentDetails);

            try {
                deckRepository.save(deck);
            } catch(Exception pe) {
                pe.printStackTrace();
                LOG.catching(Level.WARN, pe.getCause());
            }

            QuestionBank questionBank = new QuestionBank();
            questionBank.setDescription("Commonly asked web developer questions.");
            questionBank.add(new Question("What is a RESTful web service?"));
            questionBank.add(new Question("What methods are supported by the HTTP specification?"));
            questionBank.add(new Question("What is a Cross-Origin-Request?"));
            questionBank.add(new Question("Give an example of a stateless request."));
            questionBank.setCategory(category);
            questionBankRepository.save(questionBank);

            questionBank = new QuestionBank();
            questionBank.setDescription("Object Oriented Programming");
            questionBank.add(new Question("What is encapsulation?"));
            questionBank.add(new Question("What is composition?"));
            questionBank.add(new Question("What is inheritence?"));
            questionBank.add(new Question("What is abstraction?"));
            questionBank.setCategory(category);
            questionBankRepository.save(questionBank);


        };
    }

    @Bean
    @Order(8)
    public CommandLineRunner populateStudents() {
        return (args) -> {
            for(int i = 0;i < 100;i++) {
                Student student = StudentGenerator.randomizedStudent(true);
                studentRepository.save(student);
            }

        };
    }

    @Transactional
    private Category createCategoryIfNotExists(Category category)
    {
        Category category1 = categoryRepository.findOneByNameEquals(category.getName());
        if(category1 != null)
        {
            Hibernate.initialize(category1);
            return category1;
        } else {
            return categoryRepository.save(category);
        }
    }

}
