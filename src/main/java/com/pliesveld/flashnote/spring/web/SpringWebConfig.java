package com.pliesveld.flashnote.spring.web;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pliesveld.flashnote.web.controller.RateLimitingInterceptor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.validator.HibernateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.MultipartConfigFactory;
import org.springframework.context.annotation.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.MultipartConfigElement;
import java.text.DateFormat;
import java.util.List;
import java.util.TimeZone;

@Configuration
@EnableWebMvc
@ComponentScan({
        "com.pliesveld.flashnote.security",
        "com.pliesveld.flashnote.web.service",
        "com.pliesveld.flashnote.web.controller",
        "com.pliesveld.flashnote.web.handler",
        "com.pliesveld.flashnote.web.validator"

})
@PropertySource(value = { "classpath:application-local.properties" })
public class SpringWebConfig extends WebMvcConfigurerAdapter {
    private static final Logger LOG = LogManager.getLogger();


    @Bean
    @Primary
    public javax.validation.Validator localValidatorFactoryBean() {
        LocalValidatorFactoryBean factoryBean = new LocalValidatorFactoryBean();
        factoryBean.setProviderClass(HibernateValidator.class);
        return factoryBean;
    }

    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor()
    {
        MethodValidationPostProcessor processor
                = new MethodValidationPostProcessor();

        processor.setValidator(this.localValidatorFactoryBean());
        return processor;
    }


    /**
     * Static resource requests
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/index.html").addResourceLocations("/index.html"); //.setCachePeriod(31556926);
        registry.addResourceHandler("/login.html").addResourceLocations("/login.html");
        registry.addResourceHandler("/error").addResourceLocations("/404.html"); //.setCachePeriod(31556926);
        registry.addResourceHandler("/js/**").addResourceLocations("/js/"); // .setCachePeriod(31556926);
        registry.addResourceHandler("/css/**").addResourceLocations("/css/"); // .setCachePeriod(31556926);
        registry.addResourceHandler("/img/**").addResourceLocations("/img/"); // .setCachePeriod(31556926);
    }

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory multipartConfigFactory = new MultipartConfigFactory();
        multipartConfigFactory.setMaxFileSize("5MB");
        multipartConfigFactory.setMaxRequestSize("50MB");
        return multipartConfigFactory.createMultipartConfig();
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //registry.addInterceptor(this.rateLimitingInterceptor());
        super.addInterceptors(registry);
    }

    @Bean
    public RateLimitingInterceptor rateLimitingInterceptor()
    {
        return new RateLimitingInterceptor();
    }

    @Bean
    public Jackson2ObjectMapperBuilder objectMapperBuilder() {

        Jackson2ObjectMapperBuilder b = new Jackson2ObjectMapperBuilder();
        b.modulesToInstall(this.jacksonHibernateModule(),this.jacksonJavaTimeModule());
        return b;
    }

    @Bean
    public Module jacksonHibernateModule()
    {
        return new Hibernate5Module().enable(Hibernate5Module.Feature.SERIALIZE_IDENTIFIER_FOR_LAZY_NOT_LOADED_OBJECTS);
    }

    @Bean
    public Module jacksonJavaTimeModule() {
        return new JavaTimeModule();
    }

    @Autowired(required = true)
    public void configureJackson(ObjectMapper jackson2ObjectMapper)
    {
        jackson2ObjectMapper.registerModule(this.jacksonHibernateModule());
        jackson2ObjectMapper.registerModule(this.jacksonJavaTimeModule());
        jackson2ObjectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        DateFormat dateTime = new com.fasterxml.jackson.databind.util.ISO8601DateFormat();
        dateTime.setTimeZone(TimeZone.getTimeZone("UTC"));
        jackson2ObjectMapper.setDateFormat(dateTime);
    }

/**  UGLY HACK **/
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        for (HttpMessageConverter<?> converter : converters) {
            if (converter instanceof MappingJackson2HttpMessageConverter) {
                MappingJackson2HttpMessageConverter jsonMessageConverter = (MappingJackson2HttpMessageConverter) converter;
                ObjectMapper objectMapper = jsonMessageConverter.getObjectMapper();
                objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
                break;
            }
        }
    }
}
