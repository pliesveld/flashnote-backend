package com.pliesveld.flashnote.schema;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;

import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@Configuration
@PropertySource(value = {"classpath:dev-datasource.properties"})
public class DropCreateDatabase {
    private static final Logger LOG = LogManager.getLogger();

    @Value("${jdbc.driverClassName}")
    private String JDBC_DRIVER = "org.postgresql.Driver";

    @Value("${jdbc.url}")
    private String DB_URL = "jdbc:postgresql://localhost/postgres";

    @Value("${jdbc.username}")
    private String USER = "happs";

    @Value("${jdbc.password}")
    private String PASS = "";

    private String DB_NAME = "learners";

    @Value("${db.init.sql:sql/db-init.sql}")
    private String DB_INIT = "";

    @Autowired
    Environment environment;

    /* so that Spring knows how to interpret ${} */
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigIn() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    public DropCreateDatabase()
    {
    }

    @Autowired
    public void initFromEnvironment(Environment environment)
    {
        assert environment != null;
        this.JDBC_DRIVER = environment.getRequiredProperty("jdbc.driverClassName");
        this.DB_URL = environment.getRequiredProperty("jdbc.url");
        this.USER = environment.getRequiredProperty("jdbc.username");
        this.PASS = environment.getRequiredProperty("jdbc.password");
    }

    String trimDB(String jdbcURL)
    {
        int last_slash = jdbcURL.lastIndexOf("/") + 1;

        String db = jdbcURL.substring(last_slash);
        LOG.info("db: " + db);
        DB_NAME = db;

        String newJdbcUrl = jdbcURL.substring(0,last_slash);

        if(jdbcURL.contains("postgres"))
        {
            newJdbcUrl = newJdbcUrl.concat("postgres");
        }
        return newJdbcUrl;
    }

    public void main_real(String[] args) {
        initFromEnvironment(environment);
        DB_URL = trimDB(DB_URL);
        LOG.info("JDBC: " + JDBC_DRIVER);
        LOG.info("DB_URL: " + DB_URL);
        Connection conn = null;
        Statement stmt = null;

        try {
            //STEP 2: Register JDBC driver
            Class.forName(JDBC_DRIVER);

            //STEP 3: Open a connection
            LOG.info("Connecting to a selected database: " + DB_URL);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            LOG.info("Connected database successfully...");

            //STEP 4: Execute a query
            LOG.info("Deleting database...");
            stmt = conn.createStatement();

            String sql = "DROP DATABASE " + DB_NAME;
            stmt.executeUpdate(sql);
            LOG.info("Database deleted successfully...");

            sql = "CREATE DATABASE learners";
            stmt.executeUpdate(sql);
            LOG.info("Database created successfully...");

            if(StringUtils.hasText(DB_INIT))
            {
                LOG.info(DB_INIT == null);
                LOG.info(DB_INIT.equals(""));
                LOG.info(DB_INIT.length());
                LOG.info(DB_INIT);
                Resource resource = new ClassPathResource(DB_INIT);
                if(!resource.exists())
                {
                    LOG.error("Could not find {}",DB_INIT);
                } else {
                    LOG.info("Running db init", DB_INIT);
                    Files.lines(resource.getFile().toPath()).filter(s -> !s.startsWith("--")).forEach(System.out::println);
                }


            }

        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null)
                    conn.close();
            } catch (SQLException se) {
            }// do nothing
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }//end finally try
        }//end try
    }//end main

    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(DropCreateDatabase.class);
        ctx.refresh();

        DropCreateDatabase db = ctx.getBean(DropCreateDatabase.class);
        db.main_real(args);
    }


}
