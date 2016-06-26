package com.pliesveld.populator.repository.reader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class RepositorySettings {
    private static final Logger LOG = LogManager.getLogger();
    private Resource[] resources;

    public RepositorySettings(String s) {
        this.resources = new Resource[] {new ClassPathResource(s)};
    }

    public RepositorySettings(Resource resource) {
        this.resources = new Resource[] {resource };
    }

    public Resource[] getResources() {
//        Asserts.notNull(this.resources, "RepositorySettings not populated");
        LOG.trace("Returning resources: {}", resources);

        return resources;
    }

    public RepositorySettings(Resource[] resources) {
        this.resources = resources.clone();
        LOG.trace("Setting resources: {}", resources);
    }

    public static RepositorySettings load(String s, Class<?> clazz) {
        RepositorySettings repositorySettings = new RepositorySettings(new ClassPathResource(s,clazz));
        return repositorySettings;
    }

    public void setResources(Resource[] resources) {
        this.resources = resources;
    }
}
