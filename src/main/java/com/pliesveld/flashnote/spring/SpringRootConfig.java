package com.pliesveld.flashnote.spring;

import com.pliesveld.flashnote.service.DateTimeService;
import com.pliesveld.flashnote.service.DateTimeServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@ComponentScan(basePackages = {
        "com.pliesveld.flashnote.service",
        "com.pliesveld.flashnote.repository",
        "com.pliesveld.flashnote.security"
})
public class SpringRootConfig {

    @Profile({Profiles.PRODUCTION, Profiles.LOCAL})
    @Bean
    DateTimeService currentTimeDateTimeService()
    {
        return new DateTimeServiceImpl();
    }


    /*
     private static final String PROPERTY_NAME_ENTITYMANAGER_PACKAGES_TO_SCAN = "entitymanager.packages.to.scan";

    @Bean
    public LocalSessionFactoryBean sessionFactory()
    {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan(new String[] { "com.pliesveld.flashnote.domain" });
        sessionFactory.setHibernateProperties(hibernateProperties());
        return sessionFactory;
    }

    @Bean
    @Autowired
    public HibernateTransactionManager transactionManager(SessionFactory s)
    {
        HibernateTransactionManager txManager = new HibernateTransactionManager();
        txManager.setSessionFactory(s);
        return txManager;
    }*/

}
