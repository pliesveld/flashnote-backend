package com.pliesveld.flashnote.spring;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({com.pliesveld.flashnote.spring.SpringSerializationTestConfig.class, com.pliesveld.flashnote.spring.SpringEntityTestConfig.class})
public class SpringDataTestConfig {
    private static final Logger LOG = LogManager.getLogger();















//    CustomRepositoryPopulatorFactoryBean customRepositoryPopulatorFactoryBean

//    @Bean
//    @Autowired
//    public Jackson2RepositoryPopulatorFactoryBean repositoryPopulator(HibernateAwareObjectMapperImpl hibernateAwareObjectMapper) {
//
//        Resource sourceData_cat = new ClassPathResource("test-data-category.json", DbUnitTestExecutionListener.class);
////        Resource sourceData_quebank = new ClassPathResource("test-data-question-bank.json", DbUnitTestExecutionListener.class);
////        Resource sourceData_que = new ClassPathResource("test-data-questions.json", DbUnitTestExecutionListener.class);
//
//
//        CustomRepositoryPopulatorFactoryBean factory = new CustomRepositoryPopulatorFactoryBean();
//
//
//
//        factory.setResources(new Resource[] { sourceData_cat });
//        return factory;
//    }
////
//    static class MyJacksonAnnotationIntrospector extends JacksonAnnotationIntrospector {
//        @Override
//        public ObjectIdInfo findObjectIdInfo(final Annotated ann) {
//            if (DomainBaseEntity.class.isAssignableFrom(ann.getRawType())) {
//                LOG.debug(Markers.OBJECT_MAPPER_INIT, "Checking raw type: {}", ann.getRawType());
//
//                return new ObjectIdInfo(
//                        PropertyName.construct("@id", null),
//                        null,
//                        ObjectIdGenerators.IntSequenceGenerator.class,
//                        null);
//            }
//            return super.findObjectIdInfo(ann);
//        }
//
//    }

}
