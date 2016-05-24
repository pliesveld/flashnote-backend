package com.pliesveld.flashnote.spring.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryPopulatorConfig {

    @Autowired
    @Bean(name = "populator")
    public CustomRepositoryPopulatorFactoryBean customRepositoryPopulatorFactoryBean(RepositorySettings repositorySettings)
    {
        CustomRepositoryPopulatorFactoryBean customRepositoryPopulatorFactoryBean = new CustomRepositoryPopulatorFactoryBean();
        customRepositoryPopulatorFactoryBean.setResources(repositorySettings.getResources());
        return customRepositoryPopulatorFactoryBean;
    }
}
