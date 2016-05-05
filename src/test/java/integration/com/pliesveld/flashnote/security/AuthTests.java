package com.pliesveld.flashnote.security;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.filter.Filter;
import com.jayway.restassured.filter.FilterContext;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.FilterableRequestSpecification;
import com.jayway.restassured.specification.FilterableResponseSpecification;
import com.pliesveld.flashnote.repository.StudentRepository;
import com.pliesveld.flashnote.service.AccountRegistrationService;
import com.pliesveld.flashnote.spring.Profiles;
import com.pliesveld.flashnote.spring.SpringRootConfig;
import com.pliesveld.flashnote.spring.cache.SpringCacheConfig;
import com.pliesveld.flashnote.spring.data.SpringDataConfig;
import com.pliesveld.flashnote.spring.db.PersistenceContext;
import com.pliesveld.flashnote.spring.mail.SpringMailConfig;
import com.pliesveld.flashnote.spring.security.SpringSecurityConfig;
import com.pliesveld.flashnote.spring.web.SpringWebConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.Repeat;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.util.StopWatch;

import java.util.Set;

import static org.hamcrest.Matchers.isOneOf;
import static org.hamcrest.Matchers.notNullValue;


@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles({Profiles.INTEGRATION_TEST, Profiles.AUTH})
@SpringApplicationConfiguration(classes = {SpringRootConfig.class, SpringCacheConfig.class, SpringSecurityConfig.class, SpringWebConfig.class, SpringDataConfig.class, SpringMailConfig.class ,PersistenceContext.class, AuthTests.class})
@WebAppConfiguration
@IntegrationTest
@EnableAutoConfiguration
public class AuthTests {
    private static final Logger LOG = LogManager.getLogger();

    private StopWatch stopWatch = new StopWatch();


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


    RestAssured.config = RestAssuredConfig.config().objectMapperConfig(new ObjectMapperConfig(ObjectMapperType.JACKSON_2));

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

    @Autowired
    AccountRegistrationService registrationService;


    @Autowired
    StudentRepository studentRepository;


    @Before
    public void saveUser()
    {
        if(null == studentRepository.findOneByEmail("student@example.com"))
            registrationService.createStudent("newuser","student@example.com","password");

        stopWatch.start();
    }

    @After
    public void afterTest()
    {
        stopWatch.stop();
        LOG.debug("Authentication took: {}ms",stopWatch.getLastTaskTimeMillis());
    }

    @Test
    @Repeat(value = 10)
	public void contextLoad() {
        int port = Integer.valueOf(this.port);
        String URL_BASE = "http://localhost:" + port;
        String URL_AUTH = URL_BASE + "/auth";

        RestAssured.port = port;
        RestAssured.baseURI = URL_BASE;

        String token = RestAssured.given()
                .body(new AuthToken("student@example.com", "password"))
                .contentType(ContentType.JSON)
                .when().post("/auth")
                .then()
                .log().ifStatusCodeMatches(isOneOf(400,401,402,403))
                .statusCode(200)
                .body("token", notNullValue())
                .extract().path("token")
        ;

        Authorities authorities = RestAssured.given().header("X-AUTH-TOKEN", token)
                .when()
                    .get("/user")
                .then()
                .statusCode(200)
                .body("username", notNullValue())
                .body("authorities", notNullValue())
                .extract().as(Authorities.class);

        LOG.debug(authorities.getAuthorities());

	}

}

@JsonIgnoreProperties(ignoreUnknown = true)
class AuthorityExtractor implements GrantedAuthority
{
    String authority;

    @Override
    public String getAuthority() {
        return authority;
    }

    @Override
    public String toString() {
        return authority;
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
class Authorities
{
    private Set<AuthorityExtractor> authorities;

    public Authorities() {
    }

    public Set<AuthorityExtractor> getAuthorities() {
        return authorities;
    }
}


class SystemOutFilter implements Filter {
    private static final Logger LOG = LogManager.getLogger();

    public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec, FilterContext ctx) {

        final Response response = ctx.next(requestSpec, responseSpec); // Invoke the request by delegating to the next filter in the filter chain.
        LOG.debug(response::asString);
        return response;
    }
}