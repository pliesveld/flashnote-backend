package com.pliesveld.flashnote.spring.db;

import com.pliesveld.flashnote.spring.Profiles;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.persistence.EntityManagerFactory;
import javax.persistence.ValidationMode;
import javax.sql.DataSource;
import java.util.Properties;

@Profile(Profiles.LOCAL)
@Configuration
@PropertySource(value = { "classpath:dev-datasource.properties" })
@PropertySource(value = { "classpath:dev-datasource-override.properties" }, ignoreResourceNotFound = true)
public class DefaultDataSource {
    private static final Logger LOG = LogManager.getLogger();
    private static final String PROPERTY_NAME_ENTITYMANAGER_PACKAGES_TO_SCAN = "entitymanager.packages.to.scan";

    
    @Autowired
    private Environment environment;
    
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(environment.getRequiredProperty("jdbc.driverClassName"));
        dataSource.setUrl(environment.getRequiredProperty("jdbc.url"));
        dataSource.setUsername(environment.getRequiredProperty("jdbc.username"));
        dataSource.setPassword(environment.getRequiredProperty("jdbc.password"));

        if(LOG.isDebugEnabled())
        {
            LOG.debug("Initializing DataSource Bean with jdbc.url={}", dataSource.getUrl());
            LOG.debug("Initializing DataSource Bean with jdbc.username={}", dataSource.getUsername());
            /*
            dataSource.getConnectionProperties().entrySet().iterator().forEachRemaining((entry) ->            {
                LOG.debug("Initializing DataSource with property {}={}", entry.getKey(), entry.getValue());
            });
            */



        }

        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setAutoCommit(false);
        hikariDataSource.setDataSource( dataSource );
        return hikariDataSource;
    }    

    // TODO: http://www.jpab.org/Hibernate.html
    private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect",         environment.getRequiredProperty("hibernate.dialect"));
        properties.put("hibernate.show_sql",        environment.getRequiredProperty("hibernate.show_sql"));
        properties.put("hibernate.format_sql",      environment.getRequiredProperty("hibernate.format_sql"));
        properties.put("hibernate.use_sql_comments",environment.getRequiredProperty("hibernate.use_sql_comments"));
        properties.put("hibernate.hbm2ddl.auto",    environment.getRequiredProperty("hibernate.hbm2ddl.auto"));

        /*
        properties.put("hibernate.cache.use_second_level_cache",    environment.getRequiredProperty("hibernate.cache.use_second_level_cache"));
        properties.put("hibernate.cache.use_query_cache",           environment.getRequiredProperty("hibernate.cache.use_query_cache"));
        properties.put("hibernate.cache.region.factory_class",      environment.getRequiredProperty("hibernate.cache.region.factory_class"));
        */

        if(LOG.isDebugEnabled()) {
            properties.entrySet().iterator().forEachRemaining((entry) ->            {
                LOG.debug("Initializing hibernateProperties with property {}={}", entry.getKey(), entry.getValue());
            });
        }
        return properties;
    }


    @Bean
    @Autowired
    public EntityManagerFactory entityManagerFactory(DataSource dataSource) {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(true);
        vendorAdapter.setShowSql(true);
        vendorAdapter.setDatabasePlatform(environment.getRequiredProperty("hibernate.dialect"));
//        vendorAdapter.setDatabase(Database.POSTGRESQL);

        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        //        entityManagerFactoryBean.setPersistenceProviderClass(HibernatePersistenceProvider.class);
        //        entityManagerFactoryBean.setPersistenceUnitName("mainPU");
        entityManagerFactoryBean.setJpaVendorAdapter(vendorAdapter);
        entityManagerFactoryBean.setPackagesToScan(environment.getRequiredProperty(PROPERTY_NAME_ENTITYMANAGER_PACKAGES_TO_SCAN));
        entityManagerFactoryBean.setDataSource(dataSource);
        entityManagerFactoryBean.setJpaProperties(hibernateProperties());
        entityManagerFactoryBean.setValidationMode(ValidationMode.NONE);

        entityManagerFactoryBean.afterPropertiesSet();
        return entityManagerFactoryBean.getObject();
    }

}
