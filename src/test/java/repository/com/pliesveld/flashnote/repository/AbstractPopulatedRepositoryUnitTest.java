package com.pliesveld.flashnote.repository;

import com.pliesveld.flashnote.spring.repository.CustomRepositoryPopulatorFactoryBean;
import com.pliesveld.tests.AbstractTransactionalRepositoryUnitTest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

@Configuration
public class AbstractPopulatedRepositoryUnitTest extends AbstractTransactionalRepositoryUnitTest {
    private static final Logger LOG = LogManager.getLogger();

    @Bean(name = "populator")
    protected CustomRepositoryPopulatorFactoryBean customRepositoryPopulatorFactoryBean()
    {
        CustomRepositoryPopulatorFactoryBean customRepositoryPopulatorFactoryBean = new CustomRepositoryPopulatorFactoryBean();
        customRepositoryPopulatorFactoryBean.setResources(this.repositoryProperties());
        return customRepositoryPopulatorFactoryBean;
    }

    protected Resource[] repositoryProperties() {
        return new Resource[]{ new ClassPathResource("test-data-blank.json", this.getClass()) };
    }

}