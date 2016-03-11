package com.pliesveld.flashnote.spring;

/**
 * Defines legal Spring Profiles.  The following profiles determine the beans that are constructed
 * inside Springs Application Context.
 *
 * <ul>
 *     <li>The PRODUCTION profile enables connection pooling, and caching.</li>
 *     <li>The LOCAL profile enables verbose logging and allows JPA DDL schema updates</li>
 *     <li>The AUTH profile disables Spring Security on REST endpoints and within the service layer</li>
 *     <li>The INTEGRATION_TEST profile is used within integration tests.  Contains beans for embedded servlet containers
 *     and embedded database</li>
 * </ul>
 *
 * @author Petri Kainulainen
 * https://github.com/pkainulainen/spring-data-jpa-examples/blob/master/query-methods/src/main/java/net/petrikainulainen/springdata/jpa/config/Profiles.java
 */
public class Profiles {
    public static final String PRODUCTION = "production";
    public static final String LOCAL = "local";

    public static final String AUTH = "auth";
    public static final String OAUTH2 = "oauth2";

    public static final String INTEGRATION_TEST = "integration-test";
    public static final String NOT_INTEGRATION_TEST = "!integration-test";


    private Profiles() {} // prevent instantiation
}
