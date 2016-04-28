package com.pliesveld.flashnote.schema;

import com.pliesveld.flashnote.spring.Profiles;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.schema.TargetType;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;

import javax.persistence.Entity;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.EnumSet;
import java.util.Properties;

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
    String CREATE_EXPORT_FILE;

    @Value("${schema.export.file:db-drop.sql}")
    String DROP_EXPORT_FILE;

    @Value("${schema.export.dir:/resource/src/main/resources/sql/}")
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

    public static void main(String[] args) throws IllegalAccessException {

        System.setProperty("spring.profiles.active", System.getProperty("spring.profiles.active", Profiles.LOCAL));
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();

        ctx.register(DDLExport.class);
        ctx.refresh();

        DDLExport ddlExport = ctx.getBean(DDLExport.class);
        ddlExport.displayProfile(ctx);
        ddlExport.real_main(args,ctx);
    }

    public void real_main(String[] args,AnnotationConfigApplicationContext ctx) throws IllegalAccessException
    {
        try {

            writeDDLScript(PROPERTY_NAME_ENTITY_PACKAGES, this.hibernateProperties());

        } catch (Exception e) {
            e.printStackTrace();
        }

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

    void writeDDLScript(String packageName, Properties propertiesFile) throws IOException {

        MetadataSources metadata = new MetadataSources(
                new StandardServiceRegistryBuilder()
                        .applySettings(propertiesFile)
                        .build());

        new Reflections(packageName)
                .getTypesAnnotatedWith(Entity.class)
                .forEach(metadata::addAnnotatedClass);

        //STDOUT will export to output window, but other `TargetType` values are available to export to file or to the db.
        EnumSet<TargetType> targetTypes = EnumSet.of(TargetType.STDOUT, TargetType.SCRIPT);

        String filename_create = DIR_EXPORT_ROOT + DIR_EXPORT_PATH + CREATE_EXPORT_FILE;

        SchemaExport export = new SchemaExport();

        export.setDelimiter(";");
        export.setFormat(false);
        export.setHaltOnError(true);
        export.setOutputFile(filename_create);


        truncate(filename_create);

        export.createOnly(targetTypes, metadata.buildMetadata());

        LOG.info("Exported:" + filename_create);

        String filename_drop = DIR_EXPORT_ROOT + DIR_EXPORT_PATH + DROP_EXPORT_FILE;
        export.setOutputFile(filename_drop);

        truncate(filename_drop);

        export.drop(targetTypes, metadata.buildMetadata());
        LOG.info("Exported:" + filename_drop);

    }

    private void truncate(String filename) throws IOException {
        File f = new File(filename);

        if(!f.exists() || !f.isFile())
        {
            throw new IOException("file not found " + filename);
        }

        try { new FileOutputStream(filename).getChannel().truncate(0).close(); } catch (IOException e) { LOG.error(e); }
    }
/*
     private MetadataImplementor buildMetadata(StandardServiceRegistry serviceRegistry) {

        final MetadataSources metadataSources = new MetadataSources( serviceRegistry );
        final MetadataBuilder metadataBuilder = metadataSources.getMetadataBuilder();

        metadataSources.addPackage(PROPERTY_NAME_ENTITY_PACKAGES);
        return (MetadataImplementor) metadataBuilder.build();
    }
*/


}
