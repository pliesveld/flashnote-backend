package spring;

import com.pliesveld.flashnote.spring.Profiles;
import com.pliesveld.flashnote.spring.SpringRootConfig;
import com.pliesveld.flashnote.spring.data.SetupDataLoader;
import com.pliesveld.flashnote.spring.data.SpringDataConfig;
import com.pliesveld.flashnote.spring.db.PersistenceContext;
import com.pliesveld.flashnote.spring.mail.SpringMailConfig;
import com.pliesveld.flashnote.spring.security.SpringSecurityConfig;
import com.pliesveld.flashnote.spring.serializer.SpringJacksonConfig;
import com.pliesveld.flashnote.spring.web.SpringWebConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles({Profiles.INTEGRATION_TEST, Profiles.AUTH, Profiles.LOCAL})
@SpringApplicationConfiguration(classes = {SpringRootConfig.class, SpringJacksonConfig.class, SpringMailConfig.class, SpringSecurityConfig.class, SpringWebConfig.class, SpringDataConfig.class, PersistenceContext.class, PythonAuthUnitTests.class})
@ComponentScan(basePackageClasses = SetupDataLoader.class)
@WebAppConfiguration
@IntegrationTest
@EnableAutoConfiguration
public class PythonAuthUnitTests extends PythonUnitTests {

    @Test
    public void whenPythonAuthUnitTests_thenCorrect() throws Exception {
        execPythonUnitTests();
    }

}
