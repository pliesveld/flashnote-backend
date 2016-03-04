package com.pliesveld.flashnote.unit.spring;

import com.pliesveld.flashnote.spring.Profiles;
import com.pliesveld.flashnote.spring.db.PersistenceContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Controller;
import org.springframework.test.context.ActiveProfiles;


@Configuration
@ActiveProfiles(Profiles.INTEGRATION_TEST)
@ComponentScan(basePackages = {
        "com.pliesveld.flashnote.spring.db",
        "com.pliesveld.flashnote.service",
        "com.pliesveld.flashnote.repository"
},
excludeFilters = @ComponentScan.Filter(Controller.class))
@Import(PersistenceContext.class)
public class SpringUnitTestConfig
{

}
