package com.pliesveld.flashnote.spring;

import com.pliesveld.tests.beans.DomainEntities;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@ActiveProfiles(Profiles.INTEGRATION_TEST)
@ContextHierarchy({
        @ContextConfiguration(classes = SpringEntityTestConfig.class, loader = AnnotationConfigContextLoader.class),
        @ContextConfiguration(classes = DomainEntities.class, loader = AnnotationConfigContextLoader.class)
})
@TestPropertySource(locations = "classpath:test-datasource.properties")
public @interface DefaultEntityTestAnnotations {
}
