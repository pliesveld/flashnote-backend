package com.pliesveld.flashnote.spring;

import com.pliesveld.flashnote.FlashnoteContainerApplication;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.transaction.Transactional;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@ActiveProfiles(Profiles.INTEGRATION_TEST)
@SpringApplicationConfiguration(classes = FlashnoteContainerApplication.class)
@ContextConfiguration(classes = { MockServletContext.class, SpringWebTestConfig.class, SpringEntityTestConfig.class }, loader = AnnotationConfigContextLoader.class)
@WebAppConfiguration
@Transactional
@Rollback
@TestPropertySource( locations = "classpath:test-datasource.properties" )
public @interface DefaultControllerTestAnnotations { }
