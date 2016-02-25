package com.pliesveld.flashnote.spring;

import org.hibernate.validator.HibernateValidator;
import org.springframework.boot.context.embedded.MultipartConfigFactory;
import org.springframework.context.annotation.*;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.MultipartConfigElement;
import java.text.SimpleDateFormat;

/**
 * Created by happs on 1/18/16.
 */
@Configuration
@EnableWebMvc
@ComponentScan({
        "com.pliesveld.flashnote.web.service",
        "com.pliesveld.flashnote.web.controller",
        "com.pliesveld.flashnote.web.handler",
        "com.pliesveld.flashnote.web.validator"

})
@PropertySource(value = { "classpath:application.properties" })
public class SpringWebConfig extends WebMvcConfigurerAdapter{


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
        multipartConfigFactory.setMaxFileSize("25MB");
        multipartConfigFactory.setMaxRequestSize("50MB");
        return multipartConfigFactory.createMultipartConfig();
    }

/*
    @Bean
    MultipartResolver multipartResolver() throws IOException {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxInMemorySize(1024*1024*5);
        multipartResolver.setMaxUploadSizePerFile(1024*1024*5*5);
        multipartResolver.setMaxUploadSize(1024*1024*5*5*5);
        multipartResolver.setUploadTempDir(new FileSystemResource("/tmp/upload/"));
        return multipartResolver;
    }
*/
    @Bean
    public Jackson2ObjectMapperBuilder jacksonBuilder() {
	    Jackson2ObjectMapperBuilder b = new Jackson2ObjectMapperBuilder();
	    b.indentOutput(true).dateFormat(new SimpleDateFormat("yyyy-MM-dd"));
	    return b;
    }

    /**
     * Default view for JSON rendering

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        registry.enableContentNegotiation(new MappingJackson2JsonView());
        registry.jsp();
    }
    */



}
