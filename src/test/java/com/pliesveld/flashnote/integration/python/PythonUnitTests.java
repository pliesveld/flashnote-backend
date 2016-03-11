package com.pliesveld.flashnote.integration.python;

import com.pliesveld.flashnote.spring.Profiles;
import com.pliesveld.flashnote.spring.SpringRootConfig;
import com.pliesveld.flashnote.spring.data.SpringDataConfig;
import com.pliesveld.flashnote.spring.db.PersistenceContext;
import com.pliesveld.flashnote.spring.web.SpringWebConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.EmbeddedServletContainerInitializedEvent;
import org.springframework.boot.context.embedded.EmbeddedWebApplicationContext;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles(Profiles.INTEGRATION_TEST)
@SpringApplicationConfiguration(classes = {SpringRootConfig.class, SpringWebConfig.class, SpringDataConfig.class, PersistenceContext.class, PythonUnitTests.class})
//@SpringApplicationConfiguration(classes = {PythonUnitTests.class, FlashnoteContainerApplication.class})
@WebAppConfiguration
@IntegrationTest
@EnableAutoConfiguration
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
/*
    @Bean
    EmbeddedServletInitializerListener EmbeddedServletInitializerListenerImpl()
    {
        return new EmbeddedServletInitializerListener();
    }
*/
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

    @Test
	public void pythonUnitTestTest() throws IOException, InterruptedException {

        assertNotNull(port);

		ClassLoader classLoader = PythonUnitTests.class.getClassLoader();
		File rootDir = new File(classLoader.getResource("./scripts/").getFile());
		System.out.println(rootDir);
		assert rootDir.exists();
		assert rootDir.isDirectory();

		//ProcessBuilder pb = new ProcessBuilder("python3","-m","unittest","tests.test_env");
        //ProcessBuilder pb = new ProcessBuilder("python3","-m","unittest","tests.test_upload");
        ProcessBuilder pb = new ProcessBuilder("python3","-m","unittest","tests.test_auth");
        Map<String,String> env = pb.environment();

        System.out.println("port=" + port);
        env.put("INTEGRATION_TEST_PORT",port);

        String profiles = StringUtils.arrayToCommaDelimitedString(environment.getActiveProfiles());
        env.put("INTEGRATION_TEST_PROFILES",profiles);

		pb.directory(rootDir);
		Process p = pb.start();

		BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
		in.lines().forEach((line) -> System.out.println(line));

		BufferedReader in_err = new BufferedReader(new InputStreamReader(p.getErrorStream()));
		in_err.lines().forEach((line) -> System.out.println(line));

        int exit_value = p.waitFor();
        assertEquals(0,exit_value);


	}





}
