package com.pliesveld.populator.repository;

import com.pliesveld.flashnote.domain.*;
import com.pliesveld.flashnote.repository.*;
import com.pliesveld.flashnote.util.generator.StudentGenerator;
import com.pliesveld.populator.repository.reader.RepositorySettings;
import com.pliesveld.populator.spring.RepositoryPopulatorConfig;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolationException;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Configuration
@EnableAutoConfiguration //(exclude = {SecurityAutoConfiguration.class, WebMvcAutoConfiguration.class, ErrorMvcAutoConfiguration.class, DispatcherServletAutoConfiguration.class, EmbeddedServletContainerAutoConfiguration.class})
@ComponentScan(basePackages = { "com.pliesveld.flashnote", "com.pliesveld.populator.repository"})
@EnableTransactionManagement
public class PopulateFlashnoteContainerApplication {
    private static final Logger LOG = LogManager.getLogger("com.pliesveld.flashnote");

    @Autowired
    FlashCardRepository flashCardRepository;
    @Autowired
    DeckRepository deckRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    StudentRepository studentRepository;
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
        LOG.info("Arguments passed: {}", StringUtils.join(args,", "));

        SpringApplication application = new SpringApplication(PopulateFlashnoteContainerApplication.class);
        //application.setWebEnvironment(false);
//        application.addListeners(new ApplicationPidFileWriter("./bin/app.pid"));
        application.run(args);
	}



    @Transactional
    private void populateMessagesFor(Student studentDetails) {
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
                    Student student = StudentGenerator.randomizedStudent();
                    studentRepository.save(student);
                }
            } catch(ConstraintViolationException cve) {
                LOG.error("Could not save student: {}", cve);
            }
        };
    }

    @Bean
    public RepositorySettings repositorySettingsBean()
    {
        RepositorySettings settings = new RepositorySettings(new Resource[] {});
        return settings;
    }

    @Bean
    @Order(10)
    @Transactional
    public CommandLineRunner populateFromFiles(ApplicationContext parentCtx) {
        return (args) -> {
            LOG.debug("Current working dir: {}", System.getProperty("user.dir"));

            ArrayList<Resource> resources = new ArrayList<>();

            for(String arg : args) {
                if(!FileUtils.getFile(arg).exists())
                    continue;
                if(!FileUtils.getFile(arg).isDirectory())
                    continue;

                LOG.debug("Checking directory {}", arg);
                Collection<File> files = FileUtils.listFiles(FileUtils.getFile(arg),
                        FileFilterUtils.suffixFileFilter(".json"), TrueFileFilter.INSTANCE);

                files.forEach(file -> LOG.debug("Loading file {}", file));


                for(File file : files) {
                    resources.add(new FileSystemResource(file));
                }


            }

            Resource[] resources_array = new Resource[resources.size()];

            resources_array = (Resource[]) resources.toArray(resources_array);

            for(Resource res : resources_array)
            {
                LOG.info(res);
            }

            RepositorySettings settings = this.repositorySettingsBean();
            settings.setResources(resources_array);

            AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
            try {
                ctx.setParent(parentCtx);
                ctx.register(RepositoryPopulatorConfig.class);
                ctx.scan("com.pliesveld.populator.spring");
                ctx.refresh();
            } catch(PropertyReferenceException pre) {
                LOG.error("Registering populator {}", pre.getMessage());
                throw pre;
            } catch(BeanCreationException bce) {
                bce.printStackTrace();
                LOG.error("Bean creation: {}", bce.getMessage());
                throw bce;
            } catch(DataIntegrityViolationException dive) {
                LOG.warn("Failed to save {}", dive.getMessage());
            }




        };

    }


}
