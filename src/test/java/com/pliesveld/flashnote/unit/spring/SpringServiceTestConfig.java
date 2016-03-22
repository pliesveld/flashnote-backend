package com.pliesveld.flashnote.unit.spring;

import com.pliesveld.flashnote.service.FlashNoteService;
import com.pliesveld.flashnote.spring.Profiles;
import com.pliesveld.flashnote.spring.audit.SpringAuditConfig;
import com.pliesveld.flashnote.spring.data.SpringDataConfig;
import com.pliesveld.flashnote.spring.db.PersistenceContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;


@Configuration
@ActiveProfiles(Profiles.INTEGRATION_TEST)
@Import({SpringDataConfig.class, SpringAuditConfig.class, PersistenceContext.class})
@ComponentScan(basePackageClasses = FlashNoteService.class)
public class SpringServiceTestConfig
{


}
