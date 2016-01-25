package com.pliesveld.flashnote.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.text.SimpleDateFormat;

/**
 * Created by happs on 1/18/16.
 */
@Configuration
@EnableWebMvc
@ComponentScan({
        "com.pliesveld.flashnote.web.controller",
        "com.pliesveld.flashnote.web.domain",
        "com.pliesveld.flashnote.web.handler"
})
@PropertySource(value = { "classpath:application.properties" })
public class SpringWebConfig extends WebMvcConfigurerAdapter{

    /**
     * Static resource requests
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/index.html").addResourceLocations("/index.html"); //.setCachePeriod(31556926);
        registry.addResourceHandler("/error").addResourceLocations("/404.html"); //.setCachePeriod(31556926);
        registry.addResourceHandler("/js/**").addResourceLocations("/js/"); // .setCachePeriod(31556926);
        registry.addResourceHandler("/css/**").addResourceLocations("/css/"); // .setCachePeriod(31556926);
        registry.addResourceHandler("/img/**").addResourceLocations("/img/"); // .setCachePeriod(31556926);
    }


    @Bean
    MultipartResolver multipartResolver()
    {
        return new StandardServletMultipartResolver();
    }

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
