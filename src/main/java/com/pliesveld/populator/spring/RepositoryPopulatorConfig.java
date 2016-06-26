package com.pliesveld.populator.spring;

import com.pliesveld.populator.repository.reader.CustomRepositoryPopulatorFactoryBean;
import com.pliesveld.populator.repository.reader.RepositorySettings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
public class RepositoryPopulatorConfig {
    private static final Logger LOG = LogManager.getLogger();

    @Autowired
    @Bean(name = "populator")
    public CustomRepositoryPopulatorFactoryBean customRepositoryPopulatorFactoryBean(RepositorySettings repositorySettings)
    {
        CustomRepositoryPopulatorFactoryBean customRepositoryPopulatorFactoryBean = new CustomRepositoryPopulatorFactoryBean();
        Resource[] resources = repositorySettings.getResources();
        if(resources == null) {
            LOG.error("repositorySettings returned null");
        } else if(resources.length == 0) {
            LOG.warn("repositorySettings returned zero-length array");
        } else {
            LOG.debug("Creating populator bean with resources:");
            for(Resource resource : resources) {
                LOG.debug("{}", resource);

            }
        }
        customRepositoryPopulatorFactoryBean.setResources(repositorySettings.getResources());
        return customRepositoryPopulatorFactoryBean;
    }
}
