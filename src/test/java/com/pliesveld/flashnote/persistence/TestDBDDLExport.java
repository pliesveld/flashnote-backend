package com.pliesveld.flashnote.persistence;

import com.pliesveld.config.SpringTestConfig;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.model.naming.ImplicitNamingStrategyJpaCompliantImpl;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.boot.spi.MetadataImplementor;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import static org.hibernate.tool.hbm2ddl.Target.NONE;

/**
 * Generates database schemas for annotated classes. Requires dialects and
 * naming schemes.
 *
 *
 */

public class TestDBDDLExport
{

    public void DDLExport()
    {
    }

    public static void main(String[] args) throws IllegalAccessException
    {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringTestConfig.class);

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

        String filename_export = System.getProperty("user.dir") + "/src/test/resources/sql/" + "test-db-init.sql";
        //String filename_export = SpringTestConfig.class.getClassLoader().getResource(".").getPath() + "test-db-init.sql";

        new SchemaExport((MetadataImplementor) metadata).setOutputFile(filename_export).setDelimiter(";")
                .setFormat(true).setHaltOnError(true).create(NONE);

        System.out.println("Exported DDL SQL to : " + filename_export);
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
