package com.pliesveld.flashnote.repository;

import com.pliesveld.populator.repository.reader.RepositorySettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

@Configuration
public class DefaultRepositorySettingsConfig {
    @Bean
    public RepositorySettings repositorySettings() {
        RepositorySettings repositorySettings = new RepositorySettings(new Resource[]{new ClassPathResource("test-data-blank.json", this.getClass())});
        return repositorySettings;
    }
}
