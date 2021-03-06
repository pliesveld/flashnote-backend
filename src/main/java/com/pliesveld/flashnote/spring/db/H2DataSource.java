package com.pliesveld.flashnote.spring.db;

import com.pliesveld.flashnote.spring.Profiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.persistence.EntityManagerFactory;
import javax.persistence.ValidationMode;
import javax.sql.DataSource;
import java.sql.Driver;
import java.util.Properties;

import static javax.persistence.SharedCacheMode.ENABLE_SELECTIVE;

@Profile(Profiles.INTEGRATION_TEST)
@Configuration
@PropertySource(value = {"classpath:test-datasource.properties"})
public class H2DataSource {

    private static final String PROPERTY_NAME_ENTITYMANAGER_PACKAGES_TO_SCAN = "entitymanager.packages.to.scan";

    @Autowired
    private Environment environment;

    @Bean
    public DataSource dataSource() {
/*
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        EmbeddedDatabase db = builder.setType(EmbeddedDatabaseType.H2)
                  .addScript("sql/embedded/h2-postgresql.sql")
//                .addScript("sql/insert-data.sql")
                .build();

        return db;
          */

        String url = environment.getRequiredProperty("jdbc.url");
        String driver = environment.getRequiredProperty("jdbc.driverClassName");
        String username = environment.getRequiredProperty("jdbc.username");
        String password = environment.getRequiredProperty("jdbc.username");

        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();

        try {
            @SuppressWarnings("unchecked")
            Class<? extends Driver> classz = (Class<? extends Driver>) Class.forName(driver);
            dataSource.setDriverClass(classz);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("failed to load class: " + driver, e);
        }

        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    // TODO: http://www.jpab.org/Hibernate.html
    private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", environment.getRequiredProperty("hibernate.dialect"));
        properties.put("hibernate.show_sql", environment.getRequiredProperty("hibernate.show_sql"));
        properties.put("hibernate.format_sql", environment.getRequiredProperty("hibernate.format_sql"));
        properties.put("hibernate.use_sql_comments", environment.getRequiredProperty("hibernate.use_sql_comments"));
        properties.put("hibernate.hbm2ddl.auto", environment.getRequiredProperty("hibernate.hbm2ddl.auto"));
        return properties;
    }


    @Bean
    @Autowired
    public EntityManagerFactory entityManagerFactory(DataSource dataSource) {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(true);
        vendorAdapter.setShowSql(false);
        vendorAdapter.setDatabasePlatform(environment.getRequiredProperty("hibernate.dialect"));
        vendorAdapter.setDatabase(Database.H2);

        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        //        entityManagerFactoryBean.setPersistenceProviderClass(HibernatePersistenceProvider.class);
        //        entityManagerFactoryBean.setPersistenceUnitName("mainPU");
        entityManagerFactoryBean.setJpaVendorAdapter(vendorAdapter);
        entityManagerFactoryBean.setPackagesToScan(environment.getRequiredProperty(PROPERTY_NAME_ENTITYMANAGER_PACKAGES_TO_SCAN));
        entityManagerFactoryBean.setDataSource(dataSource);
        entityManagerFactoryBean.setJpaProperties(hibernateProperties());
        entityManagerFactoryBean.setValidationMode(ValidationMode.NONE); // NONE, CALLBACK, AUTO
        entityManagerFactoryBean.setSharedCacheMode(ENABLE_SELECTIVE);
        entityManagerFactoryBean.afterPropertiesSet();
        return entityManagerFactoryBean.getObject();
    }

    /*
    // Start WebServer, access http://localhost:8082
    @Bean(initMethod = "start", destroyMethod = "stop")
    public Server startDBManager() throws SQLException {
        return Server.createWebServer();
    }*/

}
