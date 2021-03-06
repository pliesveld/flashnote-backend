package com.pliesveld.flashnote.spring.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pliesveld.flashnote.logging.Markers;
import com.pliesveld.flashnote.spring.serializer.ObjectMapperDebug;
import com.pliesveld.flashnote.web.handler.RateLimitingHandlerInterceptor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.validator.HibernateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.embedded.MultipartConfigFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.SortHandlerMethodArgumentResolver;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.MultipartConfigElement;
import java.util.List;

//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.data.repository.support.DomainClassConverter;
//import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
//import org.springframework.data.web.SortHandlerMethodArgumentResolver;
//import org.springframework.format.FormatterRegistry;
//import org.springframework.format.support.FormattingConversionService;
//import org.springframework.web.method.support.HandlerMethodArgumentResolver;

@Configuration
//@EnableWebMvc
@ComponentScan({
        "com.pliesveld.flashnote.security",
        "com.pliesveld.flashnote.web.service",
        "com.pliesveld.flashnote.web.controller",
        "com.pliesveld.flashnote.web.handler",
        "com.pliesveld.flashnote.web.validator"

})
@PropertySource(value = {"classpath:application-local.properties"})
public class SpringWebConfig extends WebMvcConfigurerAdapter {
    private static final Logger LOG = LogManager.getLogger();

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    @Primary
    public javax.validation.Validator localValidatorFactoryBean() {
        LocalValidatorFactoryBean factoryBean = new LocalValidatorFactoryBean();
        factoryBean.setProviderClass(HibernateValidator.class);
        return factoryBean;
    }

    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        MethodValidationPostProcessor processor
                = new MethodValidationPostProcessor();

        processor.setValidator(this.localValidatorFactoryBean());
        return processor;
    }

//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**").allowedOrigins("http://localhost:8000");
//    }

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

    @Bean
    @ConditionalOnMissingBean(RequestContextListener.class)
    public RequestContextListener requestContextListener() {
        return new RequestContextListener();
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //registry.addInterceptor(this.rateLimitingInterceptor());
        super.addInterceptors(registry);
    }

    @Bean
    public RateLimitingHandlerInterceptor rateLimitingInterceptor() {
        return new RateLimitingHandlerInterceptor();
    }


/**  UGLY HACK **/
//    @Override
//    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
//        for (HttpMessageConverter<?> converter : converters) {
//            if (converter instanceof MappingJackson2HttpMessageConverter) {
//                MappingJackson2HttpMessageConverter jsonMessageConverter = (MappingJackson2HttpMessageConverter) converter;
//                ObjectMapper objectMapper = jsonMessageConverter.getObjectMapper();
//                Views.debug(WebMvcConfigurerAdapter.class,objectMapper);
//                configureJackson(objectMapper);
//                Views.debug(this,objectMapper);
//                break;
//            }
//        }
//    }

    /**
     * UGLY HACK *
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        LOG.debug(Markers.OBJECT_MAPPER_INIT, "Scanning SpringMVC HttpMessageConverters");

        int i = 0;
        for (HttpMessageConverter<?> converter : converters) {
            LOG.debug(Markers.OBJECT_MAPPER_INIT, "Converter {}: {} ", ++i, converter);
            if (converter instanceof MappingJackson2HttpMessageConverter) {
                MappingJackson2HttpMessageConverter jsonMessageConverter = (MappingJackson2HttpMessageConverter) converter;
                ObjectMapper objectMapper = jsonMessageConverter.getObjectMapper();
                LOG.debug(Markers.OBJECT_MAPPER_INIT, "ObjectMapper settings for MappingJackson2HttpMessageConverter");
                ObjectMapperDebug.debug(WebMvcConfigurerAdapter.class, objectMapper);
                //  break;
            }
        }
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        super.addArgumentResolvers(resolvers);

        Sort defaultSort = new Sort(new Sort.Order(Sort.Direction.ASC, "id"));
        Pageable defaultPageable = new PageRequest(0, 20, defaultSort);

        SortHandlerMethodArgumentResolver sortResolver = new SortHandlerMethodArgumentResolver();
//        sortResolver.setSortParameter("$paging.sort");
        sortResolver.setFallbackSort(defaultSort);

        PageableHandlerMethodArgumentResolver pageableResolver = new PageableHandlerMethodArgumentResolver(sortResolver);
        pageableResolver.setMaxPageSize(200);
        pageableResolver.setOneIndexedParameters(false);
//        pageableResolver.setPrefix("$paging.");
        pageableResolver.setFallbackPageable(defaultPageable);

        resolvers.add(sortResolver);
        resolvers.add(pageableResolver);
    }
//
//    @Override
//    public void addFormatters(FormatterRegistry registry) {
//        super.addFormatters(registry);
//        if (!(registry instanceof FormattingConversionService))
//        {
//            LOG.warn("Unable to register Spring Data JPA converter.");
//            return;
//        }
//
//        DomainClassConverter<FormattingConversionService> converter = new DomainClassConverter<>((FormattingConversionService) registry);
//        converter.setApplicationContext(this.applicationContext);
//    }
}
