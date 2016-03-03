package com.pliesveld.flashnote.schema;

import com.pliesveld.flashnote.spring.Profiles;
import com.pliesveld.flashnote.spring.db.PersistenceContext;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.PropertyValueException;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.util.StringUtils;

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
    private static final Logger LOG = LogManager.getLogger();

    @Value("${entitymanager.packages.to.scan}")
    String PROPERTY_NAME_ENTITY_PACKAGES;

    @Value("${schema.export.file:db-init.sql}")
    String DIR_EXPORT_FILE;

    @Value("${schema.export.dir:/src/main/resources/sql/}")
    String DIR_EXPORT_PATH;

    @Value("${schema.export.root:#{systemProperties['user.dir']}}")
    String DIR_EXPORT_ROOT;

    @Value("${schema.export.format:false}")
    boolean FORMAT_EXPORT_OUTPUT;

    @Autowired
    Environment environment;

    /* so that Spring knows how to interpret ${} */
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigIn() {
        PropertySourcesPlaceholderConfigurer pspc = new PropertySourcesPlaceholderConfigurer();
//        pspc.setEnvironment(environment);
        return pspc;
    }


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
        if(!StringUtils.hasText(PROPERTY_NAME_ENTITY_PACKAGES))
            throw new PropertyValueException("Could not find entities to scan","entitymanager.packages.to.scan",PROPERTY_NAME_ENTITY_PACKAGES);
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan(new String[] { PROPERTY_NAME_ENTITY_PACKAGES });
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


    public static void main(String[] args) throws IllegalAccessException {
        System.setProperty("spring.profiles.active", Profiles.LOCAL);
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();

        ctx.register(PersistenceContext.class,DDLExport.class);
        ctx.refresh();

        DDLExport ddlExport = ctx.getBean(DDLExport.class);
        ddlExport.displayProfile(ctx);
        ddlExport.real_main(args,ctx);
    }


    public void real_main(String[] args,AnnotationConfigApplicationContext ctx) throws IllegalAccessException
    {

        LocalSessionFactoryBean sfb = (LocalSessionFactoryBean) ctx.getBean("&sessionFactory");
        LOG.info("sfb: " + sfb);

        Configuration configuration = sfb.getConfiguration();
        LOG.info("cfg: " + configuration);



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
                        environment.getRequiredProperty("jdbc.driverClassName"))
                .applySetting("hibernate.connection.url", environment.getRequiredProperty("jdbc.url"))
                .applySetting("hibernate.connection.username", environment.getRequiredProperty("jdbc.username"))
                .applySetting("hibernate.connection.password", environment.getRequiredProperty("jdbc.password"))
                .applySetting("hibernate.dialect", environment.getRequiredProperty("hibernate.dialect"));

        // metadataSources.addAnnotatedClass(Object.class)

        Metadata metadata = metadataSources.getMetadataBuilder(ssr_builder.build())
                .applyImplicitNamingStrategy(ImplicitNamingStrategyJpaCompliantImpl.INSTANCE)
                .applyPhysicalNamingStrategy(PhysicalNamingStrategyStandardImpl.INSTANCE)

                /*
                 * TODO: customizing naming strategy
                 * .applyPhysicalNamingStrategy(new MyPhysicalNamingStrategy())
                 */

                .build();

        LOG.debug("export root {}", DIR_EXPORT_ROOT);
        LOG.debug("export path {}", DIR_EXPORT_PATH);
        LOG.debug("export file {}", DIR_EXPORT_FILE);

        //String filename_export = System.getProperty("user.dir") + "/src/main/resources/sql/" + "db-init.sql";
        String filename_export = DIR_EXPORT_ROOT + DIR_EXPORT_PATH + DIR_EXPORT_FILE;

        new SchemaExport((MetadataImplementor) metadata).setOutputFile(filename_export).setDelimiter(";")
                .setFormat(FORMAT_EXPORT_OUTPUT).setHaltOnError(true).create(NONE);

        LOG.info("Exported:" + filename_export);
    }

    private void displayProfile(ApplicationContext ctx)
    {
        LOG.info("ApplicationContext: " + ctx.getDisplayName());

        StringBuilder sb = new StringBuilder();

        String[] profiles = (ctx.getEnvironment().getActiveProfiles().length > 0
                ? ctx.getEnvironment().getActiveProfiles() : ctx.getEnvironment().getDefaultProfiles());


        for (String profile : profiles)
            sb.append(profile);

        LOG.info("Exporting Schema for profile: " + sb.toString());
    }

}
