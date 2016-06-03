package com.pliesveld.flashnote.spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({com.pliesveld.flashnote.spring.SpringSerializationTestConfig.class, com.pliesveld.flashnote.spring.SpringEntityTestConfig.class})
public class SpringDataTestConfig {

}
