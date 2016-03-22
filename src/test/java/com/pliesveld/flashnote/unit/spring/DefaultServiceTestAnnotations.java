package com.pliesveld.flashnote.unit.spring;

import com.pliesveld.flashnote.spring.Profiles;
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
@ContextConfiguration(classes = SpringServiceTestConfig.class, loader = AnnotationConfigContextLoader.class)
public @interface DefaultServiceTestAnnotations { }