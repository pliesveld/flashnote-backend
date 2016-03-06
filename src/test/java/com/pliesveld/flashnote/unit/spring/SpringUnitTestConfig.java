package com.pliesveld.flashnote.unit.spring;

import com.pliesveld.flashnote.spring.Profiles;
import com.pliesveld.flashnote.spring.data.SpringDataConfig;
import com.pliesveld.flashnote.spring.db.PersistenceContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;


@Configuration
@ActiveProfiles(Profiles.INTEGRATION_TEST)
@Import({SpringDataConfig.class,PersistenceContext.class})
public class SpringUnitTestConfig
{


}
