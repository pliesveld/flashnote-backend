package spring;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.EmbeddedServletContainerInitializedEvent;
import org.springframework.boot.context.embedded.EmbeddedWebApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;



public class PythonUnitTests {
    private static final Logger LOG = LogManager.getLogger();

    @Component
    static class EmbeddedServletInitializerListener implements ApplicationListener<EmbeddedServletContainerInitializedEvent> {
        public EmbeddedServletInitializerListener() {
        }

        @Autowired
        EmbeddedWebApplicationContext server;

        @Override
        public void onApplicationEvent(EmbeddedServletContainerInitializedEvent event) {
            LOG.info(event);
            int local_port;
            LOG.info("port: {}", (local_port = event.getEmbeddedServletContainer().getPort()));
            event.getApplicationContext().getEnvironment();
            System.setProperty("local.server.port", Integer.toString(local_port));

        }
    }

    @Autowired
    Environment environment;

    @Bean
    public PropertySourcesPlaceholderConfigurer propertyConfigIn() {
        PropertySourcesPlaceholderConfigurer pspc = new PropertySourcesPlaceholderConfigurer();
        pspc.setEnvironment(environment);
        return pspc;
    }

    @Value("${local.server.port}")
    String port;


    void execPythonUnitTests() throws Exception {

        assertNotNull(port);

        ClassLoader classLoader = PythonUnitTests.class.getClassLoader();
        File rootDir = new File(classLoader.getResource("./scripts/").getFile());
        System.out.println(rootDir);
        assert rootDir.exists();
        assert rootDir.isDirectory();

        //ProcessBuilder pb = new ProcessBuilder("python3","-m", "unittest", "tests.test_env");
        ProcessBuilder pb = new ProcessBuilder("python3", "-m", "unittest", "tests.test_upload");
//        ProcessBuilder pb = new ProcessBuilder("python3", "-m", "unittest", "tests.test_auth_jwt");
        Map<String, String> env = pb.environment();

        System.out.println("port=" + port);
        env.put("INTEGRATION_TEST_PORT", port);

        String profiles = StringUtils.arrayToCommaDelimitedString(environment.getActiveProfiles());
        env.put("INTEGRATION_TEST_PROFILES", profiles);

        env.put("DEBUG", "true");

        pb.directory(rootDir);
        Process p = pb.start();

        BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
        in.lines().forEach((line) -> System.out.println(line));

        BufferedReader in_err = new BufferedReader(new InputStreamReader(p.getErrorStream()));
        in_err.lines().forEach((line) -> System.out.println(line));

        int exit_value = p.waitFor();

        if (exit_value != 0) {
            fail("PythonUnitTest failed");
        }


    }


}
