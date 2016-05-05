package com.pliesveld.flashnote.spring;


import com.pliesveld.flashnote.spring.serializer.SpringJacksonConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan( basePackageClasses = com.pliesveld.flashnote.serializer.HibernateAwareObjectMapper.class)
@Import(SpringJacksonConfig.class)
public class SpringSerializationTestConfig {
}
