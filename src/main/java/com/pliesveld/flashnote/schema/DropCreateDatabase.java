package com.pliesveld.flashnote.schema;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.postgresql.util.PSQLException;
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

import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

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

    class SQLExecutor implements Consumer<String> {

        private final Connection connection;

        SQLExecutor(Connection statement) {
            this.connection = statement;
        }


        @Override
        public void accept(String sql) {
            Statement statement = null;
            try {
                statement = connection.createStatement();
                statement.executeUpdate(sql);
//                LOG.info("{}",sql);

            } catch (SQLException e) {
                //e.printStackTrace();
                LOG.error("{}",sql);
        //        LOG.catching(Level.ERROR,e);
            } finally {
                if(statement != null)
                    try {
                        statement.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
            }
        }

        @Override
        public Consumer<String> andThen(Consumer<? super String> after) {
            return null;
        }
    }

    public void main_real(String[] args) {
        initFromEnvironment(environment);
        String DB_ROOT_URL = trimDB(DB_URL);
        LOG.info("JDBC: " + JDBC_DRIVER);
        LOG.info("DB_URL: " + DB_ROOT_URL);
        Connection conn = null;
        Statement stmt = null;

        try {
            //STEP 2: Register JDBC driver
            Class.forName(JDBC_DRIVER);

            //STEP 3: Open a connection
            LOG.info("Connecting to a selected database: " + DB_ROOT_URL);
            conn = DriverManager.getConnection(DB_ROOT_URL, USER, PASS);
            LOG.info("Connected database successfully...");

            //STEP 4: DROP ACTIVE DATABASE CONNECTIONS TO TARGET DATABASE
            stmt = conn.createStatement();
            String sql_connections = "SELECT pg_terminate_backend(pg_stat_activity.pid) FROM pg_stat_activity WHERE pg_stat_activity.datname = 'TARGET_DB' AND pid <> pg_backend_pid();"
                    .replaceFirst("TARGET_DB",DB_NAME);
            LOG.info("Dropping active connections to target database");
            stmt.executeQuery(sql_connections);
//            if (1/0 == 0)
//                return;

            //STEP 5: Execute a query
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
                Resource resource = new ClassPathResource(DB_INIT);
                if(!resource.exists())
                {
                    LOG.error("Could not find {}",DB_INIT);
                } else {
                    initializeDB(resource);
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
                    stmt.close();
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

    private void initializeDB(Resource resource) throws IOException {
        //STEP 3: Open a connection
        LOG.info("Connecting to a selected database: " + DB_URL);
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            LOG.info("Connected database successfully...");
            SQLExecutor sqlExecutor = new SQLExecutor(conn);
            LOG.info("Running db init", DB_INIT);

            List<String> db_init =
            Files.lines(resource.getFile().toPath()).filter(s -> !s.startsWith("--")).collect(Collectors.toList());

            db_init.forEach(LOG::info);

            db_init.forEach(sqlExecutor);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(conn != null)
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
    }

    public static void main(String[] args) throws IllegalAccessException {
        DDLExport.main(args);
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(DropCreateDatabase.class);
        ctx.refresh();

        DropCreateDatabase db = ctx.getBean(DropCreateDatabase.class);
        db.main_real(args);
    }


}
