package com.pliesveld.flashnote.schema;

import com.pliesveld.flashnote.spring.SpringRootConfig;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.model.naming.ImplicitNamingStrategyJpaCompliantImpl;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.boot.spi.MetadataImplementor;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import javax.sql.DataSource;

import java.util.Properties;

import static org.hibernate.tool.hbm2ddl.Target.NONE;

/**
 * Generates database schemas for annotated classes. Requires dialects and
 * naming schemes.
 *
 *
 */

@org.springframework.context.annotation.Configuration
@PropertySource(value = {"classpath:dev-datasource.properties"})
public class DDLExport
{

    @Autowired
    Environment environment;

    public void DDLExport()
    {
    }

    // TODO: http://www.jpab.org/Hibernate.html
    private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", environment.getRequiredProperty("hibernate.dialect"));
        properties.put("hibernate.show_sql", environment.getRequiredProperty("hibernate.show_sql"));
        properties.put("hibernate.format_sql", environment.getRequiredProperty("hibernate.format_sql"));
        properties.put("hibernate.hbm2ddl.auto", environment.getRequiredProperty("hibernate.hbm2ddl.auto"));
        return properties;
    }


    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(environment.getRequiredProperty("jdbc.driverClassName"));
        dataSource.setUrl(environment.getRequiredProperty("jdbc.url"));
        dataSource.setUsername(environment.getRequiredProperty("jdbc.username"));
        dataSource.setPassword(environment.getRequiredProperty("jdbc.password"));
        return dataSource;
    }

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
    }

    public static void main(String[] args) throws IllegalAccessException
    {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(SpringRootConfig.class,DDLExport.class);


        Environment properties = ctx.getBean(Environment.class);

        LocalSessionFactoryBean sfb = (LocalSessionFactoryBean) ctx.getBean("&sessionFactory");
        System.out.println("sfb: " + sfb);

        Configuration configuration = sfb.getConfiguration();
        System.out.println("cfg: " + configuration);

        displayProfile(ctx);

        MetadataSources metadataSources = (MetadataSources) FieldUtils.readField(configuration, "metadataSources",
                true);

        /*
         * StandardServiceRegistry registry =
         * configuration.getStandardServiceRegistryBuilder().build();
         * StandardServiceRegistry registry = new
         * StandardServiceRegistryBuilder()
         * .applySetting("hibernate.connection.driver_class",
         * properties.getRequiredProperty("jdbc.driverClassName"))
         * .applySetting("hibernate.connection.url",
         * properties.getRequiredProperty("jdbc.url"))
         * .applySetting("hibernate.connection.username",
         * properties.getRequiredProperty("jdbc.username"))
         * .applySetting("hibernate.connection.password",
         * properties.getRequiredProperty("jdbc.password"))
         * .applySetting("hibernate.dialect",
         * properties.getRequiredProperty("hibernate.dialect")).build();
         */

        StandardServiceRegistryBuilder ssr_builder = configuration.getStandardServiceRegistryBuilder();

        ssr_builder
                .applySetting("hibernate.connection.driver_class",
                        properties.getRequiredProperty("jdbc.driverClassName"))
                .applySetting("hibernate.connection.url", properties.getRequiredProperty("jdbc.url"))
                .applySetting("hibernate.connection.username", properties.getRequiredProperty("jdbc.username"))
                .applySetting("hibernate.connection.password", properties.getRequiredProperty("jdbc.password"))
                .applySetting("hibernate.dialect", properties.getRequiredProperty("hibernate.dialect"));

        // metadataSources.addAnnotatedClass(Object.class)

        Metadata metadata = metadataSources.getMetadataBuilder(ssr_builder.build())
                .applyImplicitNamingStrategy(ImplicitNamingStrategyJpaCompliantImpl.INSTANCE)
                .applyPhysicalNamingStrategy(PhysicalNamingStrategyStandardImpl.INSTANCE)

                /*
                 * TODO: customizing naming strategy
                 * .applyPhysicalNamingStrategy(new MyPhysicalNamingStrategy())
                 */

                .build();

        String filename_export = System.getProperty("user.dir") + "/src/main/resources/sql/" + "db-init.sql";

        new SchemaExport((MetadataImplementor) metadata).setOutputFile(filename_export).setDelimiter(";")
                .setFormat(true).setHaltOnError(true).create(NONE);

        System.out.println("Exported:" + filename_export);
    }

    private static void displayProfile(ApplicationContext ctx)
    {
        System.out.println("ApplicationContext: " + ctx.getDisplayName());

        StringBuilder sb = new StringBuilder();

        String[] profiles = (ctx.getEnvironment().getActiveProfiles().length > 0
                ? ctx.getEnvironment().getActiveProfiles() : ctx.getEnvironment().getDefaultProfiles());

        for (String profile : profiles)
            sb.append(profile);

        System.out.println("Exporting Schema for profile: " + sb.toString());
    }

}
