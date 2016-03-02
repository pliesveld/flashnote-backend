package com.pliesveld.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = {
        "com.pliesveld.flashnote.spring.db",
        "com.pliesveld.flashnote.service",
        "com.pliesveld.flashnote.repository"
})
@ActiveProfiles("h2")
@EnableJpaRepositories({"com.pliesveld.flashnote.repository"})
public class SpringTestConfig
{


/*  // for autowiring EntityManager
    @Bean
    public PersistenceAnnotationBeanPostProcessor persistenceAnnotationBeanPostProcessor()
    {
        return new PersistenceAnnotationBeanPostProcessor().;
    }
    */


    /*
    @Bean
    public LocalSessionFactoryBean sessionFactory()
    {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
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
