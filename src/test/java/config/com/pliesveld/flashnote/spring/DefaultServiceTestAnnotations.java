package com.pliesveld.flashnote.spring;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@ActiveProfiles(Profiles.INTEGRATION_TEST)
@ContextConfiguration(classes = com.pliesveld.flashnote.spring.SpringServiceTestConfig.class, loader = AnnotationConfigContextLoader.class)
public @interface DefaultServiceTestAnnotations { }
