package com.pliesveld.flashnote.unit.dao.config;

import com.pliesveld.flashnote.unit.dao.repository.SpringDataPopulatedRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.init.Jackson2RepositoryPopulatorFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = {
        "com.pliesveld.flashnote.spring.db",
        "com.pliesveld.flashnote.service",
        "com.pliesveld.flashnote.repository"
})
@EnableJpaRepositories({"com.pliesveld.flashnote.repository"})
public class SpringTestDataConfig {

    @Bean
    public Jackson2RepositoryPopulatorFactoryBean repositoryPopulator() {

        Resource sourceData = new ClassPathResource("test-data.json",SpringDataPopulatedRepository.class);

        Jackson2RepositoryPopulatorFactoryBean factory = new Jackson2RepositoryPopulatorFactoryBean();
        // Set a custom ObjectMapper if Jackson customization is needed
        // factory.setObjectMapper(…);
        factory.setResources(new Resource[] { sourceData });
        return factory;
    }

}
