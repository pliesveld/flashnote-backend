package com.pliesveld.flashnote.spring.serializer;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pliesveld.flashnote.logging.Markers;
import com.pliesveld.flashnote.serializer.module.MyPageModule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.text.DateFormat;
import java.util.TimeZone;

@Configuration
@ComponentScan({
        "com.pliesveld.flashnote.serializer"
})
@PropertySource(value = {"classpath:mapper.properties"})
public class SpringJacksonConfig {
    private static final Logger LOG = LogManager.getLogger();

    @Value("${mapper.force:false}")
    Boolean force;

    @Value("${mapper.trans:false}")
    Boolean trans;

    @Value("${mapper.ident:true}")
    Boolean ident;

    @Value("${mapper.marker:false}")
    Boolean marker;

    @Value("${mapper.prettyprint:true}")
    Boolean prettyprint;

    @Value("${mapper.defaultview:false}")
    Boolean defaultview;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public Jackson2ObjectMapperBuilder objectMapperBuilder() {

        DateFormat dateTime = new com.fasterxml.jackson.databind.util.ISO8601DateFormat();
        dateTime.setTimeZone(TimeZone.getTimeZone("UTC"));

        Jackson2ObjectMapperBuilder b = Jackson2ObjectMapperBuilder.json()
                .dateFormat(dateTime)
                .modules(this.jacksonHibernateModule(), this.jacksonJavaTimeModule(), this.myPageModule())
//                .modulesToInstall(this.jacksonHibernateModule(),this.jacksonJavaTimeModule(),this.myPageModule())

//                .serializationInclusion(JsonInclude.Include.NON_NULL)
                .serializationInclusion(JsonInclude.Include.ALWAYS)
                .defaultViewInclusion(defaultview)
                .failOnEmptyBeans(false)
                .indentOutput(prettyprint);

        return b;
    }

    @Bean
    public Hibernate5Module jacksonHibernateModule()
    {
        Hibernate5Module jacksonHibernateModule = new Hibernate5Module()
                .configure(Hibernate5Module.Feature.FORCE_LAZY_LOADING, force)
                .configure(Hibernate5Module.Feature.USE_TRANSIENT_ANNOTATION, trans)
                .configure(Hibernate5Module.Feature.SERIALIZE_IDENTIFIER_FOR_LAZY_NOT_LOADED_OBJECTS, ident)
                .configure(Hibernate5Module.Feature.REQUIRE_EXPLICIT_LAZY_LOADING_MARKER, marker);

        LOG.debug(Markers.OBJECT_MAPPER_INIT, "Configuring Hibernate module {}", jacksonHibernateModule);
        for (Hibernate5Module.Feature feature : Hibernate5Module.Feature.values())
        {
            LOG.debug(Markers.OBJECT_MAPPER_INIT, "{} = {}", feature, jacksonHibernateModule.isEnabled(feature));
        }
        return jacksonHibernateModule;
    }

    @Bean
    public Module jacksonJavaTimeModule() {
        return new JavaTimeModule();
    }

    @Bean
    public Module myPageModule() {
        return new MyPageModule();
    }

//    @Autowired
//    @Bean
//    public ObjectMapper configureJackson(Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder)
//    {
//        ObjectMapper jackson2ObjectMapper = jackson2ObjectMapperBuilder.build();
//        LOG.debug("configuring {}",jackson2ObjectMapper);
//        jackson2ObjectMapper.registerModule(this.jacksonHibernateModule());
//        //jackson2ObjectMapper.registerModule(this.jacksonJavaTimeModule());
//        jackson2ObjectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//        jackson2ObjectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
//
//        jackson2ObjectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//        jackson2ObjectMapper.enable(SerializationFeature.USE_EQUALITY_FOR_OBJECT_ID);
//        jackson2ObjectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
//        return jackson2ObjectMapper;
//    }
//    public void configureJackson(ObjectMapper jackson2ObjectMapper)
//    {
//        LOG.debug("configuring {}",jackson2ObjectMapper);
//        //jackson2ObjectMapper.registerModule(this.jacksonHibernateModule());
//        //jackson2ObjectMapper.registerModule(this.jacksonJavaTimeModule());
//        jackson2ObjectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//        jackson2ObjectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
//
//        jackson2ObjectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//        jackson2ObjectMapper.enable(SerializationFeature.USE_EQUALITY_FOR_OBJECT_ID);
//        jackson2ObjectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
//    }
}
