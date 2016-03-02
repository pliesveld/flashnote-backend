package com.pliesveld.flashnote.integration.config;

import com.pliesveld.flashnote.integration.util.TestApiConfig;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class TestContext {

  @Bean
  public EmbeddedServletContainerFactory servletContainer() {
    TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory();
    factory.setPort(TestApiConfig.PORT);
    factory.setSessionTimeout(10, TimeUnit.MINUTES);
    return factory;
  }

}

